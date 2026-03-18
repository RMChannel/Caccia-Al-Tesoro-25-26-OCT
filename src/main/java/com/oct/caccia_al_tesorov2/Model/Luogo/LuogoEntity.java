package com.oct.caccia_al_tesorov2.Model.Luogo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "luoghi", schema = "cacciaaltesoro")
@IdClass(LuogoID.class)
public class LuogoEntity {
    @Id
    @Column(nullable = false)
    private String regione;

    @Id
    @Column(nullable = false)
    private int livello;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nome;

    @Column(nullable = false)
    private float coordX;

    @Column(nullable = false)
    private float coordY;

    @Column(nullable = false)
    private String codice;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String indizio;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(columnDefinition = "TEXT", name = "description_photo")
    private String descriptionPhoto;

    @OneToMany(mappedBy = "luogo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UnlockLuogoEntity> unlockLuogoEntities;

    public LuogoEntity() {
    }

    public LuogoEntity(String regione, int livello, String nome, float coordX, float coordY, String codice, String indizio) {
        this.regione = regione;
        this.livello = livello;
        this.nome = nome;
        this.coordX = coordX;
        this.coordY = coordY;
        this.codice = codice;
        this.indizio = indizio;
        this.description = "";
        this.descriptionPhoto = "";
    }

    public LuogoEntity(String regione, int livello, String nome, float coordX, float coordY, String codice, String indizio, String description, String descriptionPhoto) {
        this.regione = regione;
        this.livello = livello;
        this.nome = nome;
        this.coordX = coordX;
        this.coordY = coordY;
        this.codice = codice;
        this.indizio = indizio;
        this.description = description;
        this.descriptionPhoto = descriptionPhoto;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCoordX() {
        return coordX;
    }

    public void setCoordX(float coordX) {
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    public void setCoordY(float coordY) {
        this.coordY = coordY;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getIndizio() {
        return indizio;
    }

    public void setIndizio(String indizio) {
        this.indizio = indizio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionPhoto() {
        return descriptionPhoto;
    }

    public void setDescriptionPhoto(String descriptionPhoto) {
        this.descriptionPhoto = descriptionPhoto;
    }

    public List<UnlockLuogoEntity> getUnlockLuogoEntities() {
        return unlockLuogoEntities;
    }

    public void setUnlockLuogoEntities(List<UnlockLuogoEntity> unlockLuogoEntities) {
        this.unlockLuogoEntities = unlockLuogoEntities;
    }
}