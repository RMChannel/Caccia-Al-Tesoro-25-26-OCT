package com.oct.caccia_al_tesorov2.Model.Users;

import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuesitoService quesitoService;

    @Mock
    private UnlockQuesitoService unlockQuesitoService;

    @Mock
    private LuogoService luogoService;

    @Mock
    private UnlockLuogoService unlockLuogoService;

    @InjectMocks
    private CustomUserDetailsService service;

    private UserEntity createUser(String username, String role) {
        return new UserEntity(username, PasswordUtility.hashPassword("password"), role, "NORD-BRA", "Team " + username);
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        UserEntity user = createUser("team1", "USER");
        when(userRepository.findByUsername("team1")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("team1");
        assertEquals("team1", details.getUsername());
        assertTrue(details instanceof CustomUserDetails);
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("nonexistent"));
    }

    @Test
    void testFindUserSuccess() {
        UserEntity user = createUser("team1", "USER");
        when(userRepository.findByUsername("team1")).thenReturn(Optional.of(user));

        UserEntity result = service.findUser("team1");
        assertEquals("team1", result.getUsername());
    }

    @Test
    void testFindUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.findUser("nonexistent"));
    }

    @Test
    void testGetAllUsers() {
        List<UserEntity> users = Arrays.asList(createUser("u1", "USER"), createUser("u2", "USER"));
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = service.getAllUsers();
        assertEquals(2, result.size());
    }

    @Test
    void testGetAllGiudici() {
        List<UserEntity> giudici = Arrays.asList(createUser("g1", "GIUDICE"));
        when(userRepository.getAllByRole("GIUDICE")).thenReturn(giudici);

        List<UserEntity> result = service.GetAllGiudici();
        assertEquals(1, result.size());
    }

    @Test
    void testAddUserSuccess() throws UserAlreadyExistException {
        UserEntity user = createUser("newUser", "USER");
        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());

        service.addUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void testAddUserAlreadyExists() {
        UserEntity existing = createUser("existingUser", "USER");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existing));

        UserEntity newUser = createUser("existingUser", "USER");
        assertThrows(UserAlreadyExistException.class, () -> service.addUser(newUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser() {
        UserEntity user = createUser("team1", "USER");
        service.updateUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void testAvoidFirstLogin() {
        UserEntity user = createUser("team1", "USER");
        user.setFirstLogin(true);

        service.avoidFirstLogin(user);
        assertFalse(user.isFirstLogin());
        verify(userRepository).save(user);
    }

    @Test
    void testDeleteUser() {
        service.deleteUser("team1");
        verify(userRepository).deleteByUsername("team1");
    }

    @Test
    void testHaveWonAlreadyCompleted() {
        UserEntity user = createUser("team1", "USER");
        user.setCompleted(LocalDateTime.now());

        assertTrue(service.haveWon(user));
        // Should not call any count services since already completed
        verify(unlockQuesitoService, never()).getCountCompleted(any());
    }

    @Test
    void testHaveWonReaches20() {
        UserEntity user = createUser("team1", "USER");
        assertNull(user.getCompleted());

        when(unlockQuesitoService.getCountCompleted(user)).thenReturn(10);
        when(unlockLuogoService.getCountCompleted(user)).thenReturn(10);

        assertTrue(service.haveWon(user));
        assertNotNull(user.getCompleted());
        verify(userRepository).save(user);
    }

    @Test
    void testHaveWonNotYet() {
        UserEntity user = createUser("team1", "USER");

        when(unlockQuesitoService.getCountCompleted(user)).thenReturn(5);
        when(unlockLuogoService.getCountCompleted(user)).thenReturn(3);

        assertFalse(service.haveWon(user));
        assertNull(user.getCompleted());
    }
}
