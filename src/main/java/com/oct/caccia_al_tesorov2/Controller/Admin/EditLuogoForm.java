package com.oct.caccia_al_tesorov2.Controller.Admin;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class EditLuogoForm {
    private int livello;
    private String regione;
    @NotEmpty(message = "Il nome del luogo non può essere vuoto")
    private String nome;
    private float coordx;
    private float coordy;
    @Size(min = 7, max = 7, message = "Il codice deve essere di 6 caratteri")
    private String codice;
    @NotEmpty(message = "L'indizio non può essere vuoto")
    private String indizio;
    @NotEmpty(message = "La descrizione non può esssere vuota")
    private String description;
    @NotEmpty(message = "Il link per la foto non può essere vuota")
    private String descriptionPhoto;

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getCoordx() {
        return coordx;
    }

    public void setCoordx(float coordx) {
        this.coordx = coordx;
    }

    public float getCoordy() {
        return coordy;
    }

    public void setCoordy(float coordy) {
        this.coordy = coordy;
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
}
