package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo;

import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoID;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UnlockLuogoService {
    private final UnlockLuogoRepository unlockLuogoRepository;
    private final LuogoService luogoService;

    public UnlockLuogoService(UnlockLuogoRepository unlockLuogoRepository, LuogoService luogoService) {
        this.unlockLuogoRepository = unlockLuogoRepository;
        this.luogoService = luogoService;
    }

    @Transactional
    public void salva(UnlockLuogoEntity ule) {
        unlockLuogoRepository.save(ule);
    }

    public List<UnlockLuogoEntity> getAllLuoghi(UserEntity user) {
        return unlockLuogoRepository.getAllByUser(user);
    }

    public int getCountCompleted(UserEntity user) {
        return unlockLuogoRepository.findAllByUserAndCompleted(user,true).size();
    }

    public boolean isUnlockedOrNotCompleted(int livello, UserEntity user) {
        List<UnlockLuogoEntity> list=unlockLuogoRepository.getAllByUser(user);
        for(UnlockLuogoEntity luogo:list) {
            if(luogo.getLuogo().getLivello()==livello) {
                return !luogo.isCompleted();
            }
        }
        return false;
    }

    public int findMaxLivello(UserEntity user) {
        return unlockLuogoRepository.findMaxLivelloByUserAndRegione(user,user.getRegione()).orElse(0);
    }

    @Transactional
    public void setCompleted(int livello, UserEntity user) {
        List<UnlockLuogoEntity> list=unlockLuogoRepository.getAllByUser(user);
        for(UnlockLuogoEntity luogo:list) {
            if(luogo.getLuogo().getLivello()==livello) {
                luogo.setCompleted(true);
                return;
            }
        }
    }

    @Transactional
    public void unlockNext(UserEntity user) {
        Optional<LuogoEntity> opLuogo = luogoService.getLuogo(findMaxLivello(user)+1,user.getRegione());
        opLuogo.ifPresent(luogoEntity -> salva(new UnlockLuogoEntity(user,luogoEntity,false)));
    }

    public boolean itsUnlocked(UserEntity user, int livello) {
        Optional<UnlockLuogoEntity> unlockLuogoEntity=unlockLuogoRepository.findById(new UnlockLuogoID(user.getUsername(),new LuogoID(user.getRegione(),livello)));
        return unlockLuogoEntity.isPresent();
    }
}