package com.oct.caccia_al_tesorov2.Model.Quesito;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface QuesitoRepository extends JpaRepository<QuesitoEntity, Integer> {
    Optional<QuesitoEntity> findByLivello(int livello);
}
