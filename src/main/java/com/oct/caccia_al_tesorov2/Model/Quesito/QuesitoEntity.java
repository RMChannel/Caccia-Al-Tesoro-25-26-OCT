package com.oct.caccia_al_tesorov2.Model.Quesito;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "quesiti", schema = "cacciaaltesoro")
public class QuesitoEntity {
    @Id
    @Column(nullable = false)
    private int livello;

    @Column(nullable = false)
    private String urlPage;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "quesito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UnlockQuesitoEntity> unlockQuesitoEntities;

    public QuesitoEntity() {}

    public QuesitoEntity(int livello, String urlPage, String password) {
        this.livello = livello;
        this.urlPage = urlPage;
        this.password = password;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public String getUrlPage() {
        return urlPage;
    }

    public void setUrlPage(String urlPage) {
        this.urlPage = urlPage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UnlockQuesitoEntity> getUnlockQuesitoEntities() {
        return unlockQuesitoEntities;
    }

    public void setUnlockQuesitoEntities(List<UnlockQuesitoEntity> unlockQuesitoEntities) {
        this.unlockQuesitoEntities = unlockQuesitoEntities;
    }
}
