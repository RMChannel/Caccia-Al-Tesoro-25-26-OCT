package com.oct.caccia_al_tesorov2.Model.Luogo;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LuogoService {
    private final LuogoRepository luogoRepository;

    public LuogoService(LuogoRepository luogoRepository) {
        this.luogoRepository = luogoRepository;
    }

    public List<LuogoEntity> getAllUnlocked(int livello) {
        return luogoRepository.getLuogoEntitiesByLivelloLessThanEqual(livello);
    }

    public void salva(LuogoEntity luogo) {
        luogoRepository.save(luogo);
    }

    public int getCount(String regione) {
        return luogoRepository.findAllByRegione(regione).size();
    }

    public Optional<LuogoEntity> getLuogo(int livello, String regione) {
        return luogoRepository.findById(new LuogoID(regione, livello));
    }

    @Transactional
    public void update(LuogoEntity l) {
        Optional<LuogoEntity> opt=getLuogo(l.getLivello(),l.getRegione());
        if(opt.isPresent()) luogoRepository.save(l);
        else throw new LuogoNotFoundException();
    }

    public List<LuogoEntity> getAll() {
        return luogoRepository.findAll();
    }
}