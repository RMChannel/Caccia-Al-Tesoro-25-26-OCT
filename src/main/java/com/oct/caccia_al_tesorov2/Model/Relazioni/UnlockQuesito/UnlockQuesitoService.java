package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito;

import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnlockQuesitoService {
    private final UnlockQuesitoRepository unlockQuesitoRepository;
    private final QuesitoService quesitoService;

    public UnlockQuesitoService(UnlockQuesitoRepository unlockQuesitoRepository, QuesitoService quesitoService) {
        this.unlockQuesitoRepository = unlockQuesitoRepository;
        this.quesitoService = quesitoService;
    }

    @Transactional
    public void salva(UnlockQuesitoEntity uqe) {
        unlockQuesitoRepository.save(uqe);
    }

    public List<UnlockQuesitoEntity> getAllQuesiti(UserEntity user) {
        return unlockQuesitoRepository.findAllByUser(user);
    }

    public int getCountCompleted(UserEntity user) {
        return unlockQuesitoRepository.findAllByUserAndCompleted(user, true).size();
    }

    public int findMaxLivello(UserEntity user) {
        return unlockQuesitoRepository.findMaxLivelloByUser(user).orElse(0);
    }

    @Transactional
    public void unlockNext(UserEntity user) {
        Optional<QuesitoEntity> opq = quesitoService.getQuesito(findMaxLivello(user)+1);
        opq.ifPresent(quesitoEntity -> salva(new UnlockQuesitoEntity(user, quesitoEntity, false)));
    }

    public boolean itsUnlocked(UserEntity user, int livello) {
        Optional<UnlockQuesitoEntity> unlockQuesitoEntity=unlockQuesitoRepository.findById(new UnlockQuesitoID(user.getUsername(),livello));
        return unlockQuesitoEntity.isPresent() && !unlockQuesitoEntity.get().isCompleted();
    }

    @Transactional
    public void setCompleted(UserEntity user, int livello) {
        List<UnlockQuesitoEntity> list=unlockQuesitoRepository.findAllByUser(user);
        for(UnlockQuesitoEntity uqe:list) {
            if(uqe.getQuesito().getLivello()==livello) {
                uqe.setCompleted(true);
                return;
            }
        }
    }
}
