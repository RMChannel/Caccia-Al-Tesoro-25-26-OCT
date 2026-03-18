package com.oct.caccia_al_tesorov2.Model.Luogo;

import java.io.Serializable;
import java.util.Objects;

public class LuogoID implements Serializable {
    private String regione;
    private int livello;

    public LuogoID() {}

    public LuogoID(String regione, int livello) {
        this.regione = regione;
        this.livello = livello;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LuogoID luogoID = (LuogoID) o;
        return livello == luogoID.livello && Objects.equals(regione, luogoID.regione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regione, livello);
    }
}
