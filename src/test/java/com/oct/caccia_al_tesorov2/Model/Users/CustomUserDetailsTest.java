package com.oct.caccia_al_tesorov2.Model.Users;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void testGetAuthoritiesUser() {
        UserEntity user = new UserEntity("testUser", "hash", "USER");
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetAuthoritiesAdmin() {
        UserEntity user = new UserEntity("admin", "hash", "ADMIN");
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetAuthoritiesLowerCase() {
        UserEntity user = new UserEntity("user", "hash", "giudice");
        CustomUserDetails details = new CustomUserDetails(user);

        Collection<? extends GrantedAuthority> authorities = details.getAuthorities();
        assertEquals("ROLE_GIUDICE", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetPassword() {
        UserEntity user = new UserEntity("user", "myHashedPassword", "USER");
        CustomUserDetails details = new CustomUserDetails(user);
        assertEquals("myHashedPassword", details.getPassword());
    }

    @Test
    void testGetUsername() {
        UserEntity user = new UserEntity("testUser", "hash", "USER");
        CustomUserDetails details = new CustomUserDetails(user);
        assertEquals("testUser", details.getUsername());
    }

    @Test
    void testAllBooleanMethodsReturnTrue() {
        UserEntity user = new UserEntity("user", "hash", "USER");
        CustomUserDetails details = new CustomUserDetails(user);

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }
}
