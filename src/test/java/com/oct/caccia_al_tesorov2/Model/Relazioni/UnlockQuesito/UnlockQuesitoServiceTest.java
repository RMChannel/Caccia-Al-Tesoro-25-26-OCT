package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito;

import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnlockQuesitoServiceTest {

    @Mock
    private UnlockQuesitoRepository unlockQuesitoRepository;

    @Mock
    private QuesitoService quesitoService;

    @InjectMocks
    private UnlockQuesitoService unlockQuesitoService;

    private UserEntity createUser() {
        return new UserEntity("testUser", "hash", "USER", "NORD-BRA", "Test Team");
    }

    private QuesitoEntity createQuesito(int livello) {
        return new QuesitoEntity(livello, "page" + livello, "password" + livello);
    }

    @Test
    void testSalva() {
        UserEntity user = createUser();
        QuesitoEntity q = createQuesito(1);
        UnlockQuesitoEntity uqe = new UnlockQuesitoEntity(user, q, false);

        unlockQuesitoService.salva(uqe);
        verify(unlockQuesitoRepository).save(uqe);
    }

    @Test
    void testGetAllQuesiti() {
        UserEntity user = createUser();
        List<UnlockQuesitoEntity> list = Arrays.asList(
                new UnlockQuesitoEntity(user, createQuesito(1), true),
                new UnlockQuesitoEntity(user, createQuesito(2), false)
        );
        when(unlockQuesitoRepository.findAllByUser(user)).thenReturn(list);

        List<UnlockQuesitoEntity> result = unlockQuesitoService.getAllQuesiti(user);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCountCompleted() {
        UserEntity user = createUser();
        when(unlockQuesitoRepository.findAllByUserAndCompleted(user, true))
                .thenReturn(Arrays.asList(new UnlockQuesitoEntity(), new UnlockQuesitoEntity(), new UnlockQuesitoEntity()));

        assertEquals(3, unlockQuesitoService.getCountCompleted(user));
    }

    @Test
    void testFindMaxLivello() {
        UserEntity user = createUser();
        when(unlockQuesitoRepository.findMaxLivelloByUser(user)).thenReturn(Optional.of(5));

        assertEquals(5, unlockQuesitoService.findMaxLivello(user));
    }

    @Test
    void testFindMaxLivelloEmpty() {
        UserEntity user = createUser();
        when(unlockQuesitoRepository.findMaxLivelloByUser(user)).thenReturn(Optional.empty());

        assertEquals(0, unlockQuesitoService.findMaxLivello(user));
    }

    @Test
    void testUnlockNext() {
        UserEntity user = createUser();
        QuesitoEntity nextQ = createQuesito(4);
        when(unlockQuesitoRepository.findMaxLivelloByUser(user)).thenReturn(Optional.of(3));
        when(quesitoService.getQuesito(4)).thenReturn(Optional.of(nextQ));

        unlockQuesitoService.unlockNext(user);
        verify(unlockQuesitoRepository).save(any(UnlockQuesitoEntity.class));
    }

    @Test
    void testUnlockNextNoMoreQuesiti() {
        UserEntity user = createUser();
        when(unlockQuesitoRepository.findMaxLivelloByUser(user)).thenReturn(Optional.of(10));
        when(quesitoService.getQuesito(11)).thenReturn(Optional.empty());

        unlockQuesitoService.unlockNext(user);
        verify(unlockQuesitoRepository, never()).save(any());
    }

    @Test
    void testItsUnlocked_unlockedAndNotCompleted() {
        UserEntity user = createUser();
        UnlockQuesitoEntity uqe = new UnlockQuesitoEntity(user, createQuesito(3), false);
        when(unlockQuesitoRepository.findById(new UnlockQuesitoID("testUser", 3)))
                .thenReturn(Optional.of(uqe));

        assertTrue(unlockQuesitoService.itsUnlocked(user, 3));
    }

    @Test
    void testItsUnlocked_unlockedButCompleted() {
        UserEntity user = createUser();
        UnlockQuesitoEntity uqe = new UnlockQuesitoEntity(user, createQuesito(3), true);
        when(unlockQuesitoRepository.findById(new UnlockQuesitoID("testUser", 3)))
                .thenReturn(Optional.of(uqe));

        // itsUnlocked returns true only if present AND not completed
        assertFalse(unlockQuesitoService.itsUnlocked(user, 3));
    }

    @Test
    void testItsUnlocked_notUnlocked() {
        UserEntity user = createUser();
        when(unlockQuesitoRepository.findById(new UnlockQuesitoID("testUser", 5)))
                .thenReturn(Optional.empty());

        assertFalse(unlockQuesitoService.itsUnlocked(user, 5));
    }

    @Test
    void testSetCompleted() {
        UserEntity user = createUser();
        QuesitoEntity q2 = createQuesito(2);
        UnlockQuesitoEntity uqe = new UnlockQuesitoEntity(user, q2, false);
        when(unlockQuesitoRepository.findAllByUser(user)).thenReturn(List.of(uqe));

        unlockQuesitoService.setCompleted(user, 2);
        assertTrue(uqe.isCompleted());
    }

    @Test
    void testSetCompletedNoMatch() {
        UserEntity user = createUser();
        QuesitoEntity q2 = createQuesito(2);
        UnlockQuesitoEntity uqe = new UnlockQuesitoEntity(user, q2, false);
        when(unlockQuesitoRepository.findAllByUser(user)).thenReturn(List.of(uqe));

        // Trying to complete livello 5, which doesn't exist - should not throw, just do nothing
        unlockQuesitoService.setCompleted(user, 5);
        assertFalse(uqe.isCompleted()); // livello 2 should remain unchanged
    }
}
