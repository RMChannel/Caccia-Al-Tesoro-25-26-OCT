package com.oct.caccia_al_tesorov2.Model.Users;

import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "utenti", schema = "cacciaaltesoro")
public class UserEntity {
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String hashPassword;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String regione;

    @Column(nullable = false)
    private boolean firstLogin;

    @Column(nullable = false)
    private String realName;

    @Column()
    private LocalDateTime completed;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnlockLuogoEntity> unlockLuogoEntities;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnlockQuesitoEntity> unlockQuesitoEntities;

    public UserEntity() {}

    public UserEntity(String username, String hashPassword, String role) {
        this.username = username;
        this.hashPassword = hashPassword;
        this.role = role;
        this.regione = "";
        this.firstLogin=false;
        this.realName="";
        this.completed=null;
    }

    public UserEntity(String username, String hashPassword, String role, String regione, String realName) {
        this.username = username;
        this.hashPassword = hashPassword;
        this.role = role;
        this.regione = regione;
        this.firstLogin = true;
        this.realName = realName;
        this.completed=null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<UnlockLuogoEntity> getUnlockLuogoEntities() {
        return unlockLuogoEntities;
    }

    public void setUnlockLuogoEntities(List<UnlockLuogoEntity> unlockLuogoEntities) {
        this.unlockLuogoEntities = unlockLuogoEntities;
    }

    public List<UnlockQuesitoEntity> getUnlockQuesitoEntities() {
        return unlockQuesitoEntities;
    }

    public void setUnlockQuesitoEntities(List<UnlockQuesitoEntity> unlockQuesitoEntities) {
        this.unlockQuesitoEntities = unlockQuesitoEntities;
    }

    public boolean isCompleted() {
        return completed != null;
    }

    public LocalDateTime getCompleted() {
        return completed;
    }

    public void setCompleted(LocalDateTime completed) {
        this.completed = completed;
    }
}
