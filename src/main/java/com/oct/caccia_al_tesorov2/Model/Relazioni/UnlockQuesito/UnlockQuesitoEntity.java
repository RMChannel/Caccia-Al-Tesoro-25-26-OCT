package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "unlockquesiti", schema = "cacciaaltesoro")
@IdClass(UnlockQuesitoID.class)
public class UnlockQuesitoEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumn(name = "livello", referencedColumnName = "livello", nullable = false)
    private QuesitoEntity quesito;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    public UnlockQuesitoEntity() {}

    public UnlockQuesitoEntity(UserEntity user, QuesitoEntity quesito, boolean completed) {
        this.user = user;
        this.quesito = quesito;
        this.completed = completed;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuesitoEntity getQuesito() {
        return quesito;
    }

    public void setQuesito(QuesitoEntity quesito) {
        this.quesito = quesito;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
