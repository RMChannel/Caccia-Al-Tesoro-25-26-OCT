package com.oct.caccia_al_tesorov2.Model.Users;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class dPasswordUtilityTest {

    @Test
    void testHashPasswordReturnsNonNull() {
        String hash = PasswordUtility.hashPassword("testPassword");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void testHashPasswordProducesDifferentHashesForSameInput() {
        String hash1 = PasswordUtility.hashPassword("samePassword");
        String hash2 = PasswordUtility.hashPassword("samePassword");
        // BCrypt genera salt diversi ogni volta
        assertNotEquals(hash1, hash2);
    }

    @Test
    void testCheckPasswordCorrect() {
        String plainPassword = "MySecretPassword123!";
        String hash = PasswordUtility.hashPassword(plainPassword);
        assertTrue(PasswordUtility.checkPassword(plainPassword, hash));
    }

    @Test
    void testCheckPasswordIncorrect() {
        String hash = PasswordUtility.hashPassword("correctPassword");
        assertFalse(PasswordUtility.checkPassword("wrongPassword", hash));
    }

    @Test
    void testHashStartsWithBcryptPrefix() {
        String hash = PasswordUtility.hashPassword("test");
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
    }
}
