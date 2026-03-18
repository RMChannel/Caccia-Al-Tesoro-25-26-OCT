package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito;

import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UnlockQuesitoRepository extends JpaRepository<UnlockQuesitoEntity, UnlockQuesitoID> {
    List<UnlockQuesitoEntity> findAllByUser(UserEntity user);

    @Query("SELECT MAX(uq.quesito.livello) FROM UnlockQuesitoEntity uq WHERE uq.user = :user")
    Optional<Integer> findMaxLivelloByUser(@Param("user") UserEntity user);

    List<UnlockQuesitoEntity> findAllByUserAndCompleted(UserEntity user, boolean completed);
}
