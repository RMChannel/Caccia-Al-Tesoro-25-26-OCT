package com.oct.caccia_al_tesorov2.Model;

import org.springframework.stereotype.Service;

@Service
public class ActivateGameService {
    private static boolean game;

    public static boolean isGameActive() {
        return game;
    }

    public static void setGameActive(boolean game) {
        ActivateGameService.game = game;
    }
}
