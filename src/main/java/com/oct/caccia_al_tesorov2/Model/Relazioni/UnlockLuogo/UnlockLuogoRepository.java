package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo;

import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnlockLuogoRepository extends JpaRepository<UnlockLuogoEntity, UnlockLuogoID> {
    List<UnlockLuogoEntity> getAllByUser(UserEntity user);

    UnlockLuogoEntity getByUser(UserEntity user);

    @Query("SELECT MAX(ul.luogo.livello) FROM UnlockLuogoEntity ul WHERE ul.user = :user AND ul.luogo.regione = :regione")
    Optional<Integer> findMaxLivelloByUserAndRegione(@Param("user") UserEntity user, @Param("regione") String regione);

    List<UnlockLuogoEntity> findAllByUserAndCompleted(UserEntity user, boolean completed);
}
