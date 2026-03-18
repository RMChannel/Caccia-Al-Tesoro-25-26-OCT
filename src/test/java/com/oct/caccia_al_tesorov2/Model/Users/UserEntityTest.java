package com.oct.caccia_al_tesorov2.Model.Users;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {

    @Test
    void testDefaultConstructor() {
        UserEntity user = new UserEntity();
        assertNull(user.getUsername());
        assertNull(user.getHashPassword());
        assertNull(user.getRole());
    }

    @Test
    void testThreeArgConstructor() {
        UserEntity user = new UserEntity("admin", "hashedPwd", "ADMIN");
        assertEquals("admin", user.getUsername());
        assertEquals("hashedPwd", user.getHashPassword());
        assertEquals("ADMIN", user.getRole());
        assertEquals("", user.getRegione());
        assertFalse(user.isFirstLogin());
        assertEquals("", user.getRealName());
        assertNull(user.getCompleted());
        assertFalse(user.isCompleted());
    }

    @Test
    void testFiveArgConstructor() {
        UserEntity user = new UserEntity("team1", "hashedPwd", "USER", "NORD-BRA", "Squadra 1");
        assertEquals("team1", user.getUsername());
        assertEquals("hashedPwd", user.getHashPassword());
        assertEquals("USER", user.getRole());
        assertEquals("NORD-BRA", user.getRegione());
        assertTrue(user.isFirstLogin());
        assertEquals("Squadra 1", user.getRealName());
        assertNull(user.getCompleted());
        assertFalse(user.isCompleted());
    }

    @Test
    void testSettersAndGetters() {
        UserEntity user = new UserEntity();
        user.setUsername("testUser");
        user.setHashPassword("newHash");
        user.setRole("USER");
        user.setRegione("SUD-MELFI");
        user.setFirstLogin(true);
        user.setRealName("Test Team");

        assertEquals("testUser", user.getUsername());
        assertEquals("newHash", user.getHashPassword());
        assertEquals("USER", user.getRole());
        assertEquals("SUD-MELFI", user.getRegione());
        assertTrue(user.isFirstLogin());
        assertEquals("Test Team", user.getRealName());
    }

    @Test
    void testIsCompletedFalseWhenNull() {
        UserEntity user = new UserEntity("u", "p", "USER");
        assertFalse(user.isCompleted());
        assertNull(user.getCompleted());
    }

    @Test
    void testIsCompletedTrueWhenSet() {
        UserEntity user = new UserEntity("u", "p", "USER");
        LocalDateTime now = LocalDateTime.now();
        user.setCompleted(now);
        assertTrue(user.isCompleted());
        assertEquals(now, user.getCompleted());
    }

    @Test
    void testSetCompletedBackToNull() {
        UserEntity user = new UserEntity("u", "p", "USER");
        user.setCompleted(LocalDateTime.now());
        assertTrue(user.isCompleted());
        user.setCompleted(null);
        assertFalse(user.isCompleted());
    }
}
