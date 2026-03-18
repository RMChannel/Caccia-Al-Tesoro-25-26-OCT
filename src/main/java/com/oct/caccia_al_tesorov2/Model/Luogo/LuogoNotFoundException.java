package com.oct.caccia_al_tesorov2.Model.Luogo;

public class LuogoNotFoundException extends RuntimeException {
    public LuogoNotFoundException() {
        super("Luogo non trovato");
    }
}
