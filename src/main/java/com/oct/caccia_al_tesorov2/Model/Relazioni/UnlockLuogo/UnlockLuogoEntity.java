package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "unlockluogo", schema = "cacciaaltesoro")
@IdClass(UnlockLuogoID.class)
public class UnlockLuogoEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "regione", referencedColumnName = "regione", nullable = false),
        @JoinColumn(name = "livello", referencedColumnName = "livello", nullable = false)
    })
    private LuogoEntity luogo;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    public UnlockLuogoEntity() {}

    public UnlockLuogoEntity(UserEntity user, LuogoEntity luogo, boolean completed) {
        this.user = user;
        this.luogo = luogo;
        this.completed = completed;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LuogoEntity getLuogo() {
        return luogo;
    }

    public void setLuogo(LuogoEntity luogo) {
        this.luogo = luogo;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
