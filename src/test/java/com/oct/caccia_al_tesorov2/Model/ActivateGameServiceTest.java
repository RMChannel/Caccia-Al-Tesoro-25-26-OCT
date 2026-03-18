package com.oct.caccia_al_tesorov2.Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActivateGameServiceTest {

    @AfterEach
    void resetGameState() {
        ActivateGameService.setGameActive(false);
    }

    @Test
    void testGameIsInactiveByDefault() {
        ActivateGameService.setGameActive(false);
        assertFalse(ActivateGameService.isGameActive());
    }

    @Test
    void testActivateGame() {
        ActivateGameService.setGameActive(true);
        assertTrue(ActivateGameService.isGameActive());
    }

    @Test
    void testDeactivateGame() {
        ActivateGameService.setGameActive(true);
        assertTrue(ActivateGameService.isGameActive());
        ActivateGameService.setGameActive(false);
        assertFalse(ActivateGameService.isGameActive());
    }

    @Test
    void testToggleGame() {
        ActivateGameService.setGameActive(false);
        boolean newState = !ActivateGameService.isGameActive();
        ActivateGameService.setGameActive(newState);
        assertTrue(ActivateGameService.isGameActive());

        newState = !ActivateGameService.isGameActive();
        ActivateGameService.setGameActive(newState);
        assertFalse(ActivateGameService.isGameActive());
    }
}
