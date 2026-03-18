package com.oct.caccia_al_tesorov2.Model.Luogo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LuogoRepository extends JpaRepository<LuogoEntity, LuogoID> {
    List<LuogoEntity> getLuogoEntitiesByLivelloLessThanEqual(int livello);

    Collection<Object> findAllByRegione(String regione);
}
