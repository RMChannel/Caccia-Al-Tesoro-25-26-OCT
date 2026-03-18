package com.oct.caccia_al_tesorov2.Model.Quesito;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuesitoService {
    private final QuesitoRepository quesitoRepository;

    public QuesitoService(QuesitoRepository quesitoRepository) {
        this.quesitoRepository = quesitoRepository;
    }

    @Transactional
    public void salva(QuesitoEntity qe) {
        quesitoRepository.save(qe);
    }

    public int getCount() {
        return quesitoRepository.findAll().size();
    }

    public Optional<QuesitoEntity> getQuesito(int livello) {
        return quesitoRepository.findByLivello(livello);
    }
}
