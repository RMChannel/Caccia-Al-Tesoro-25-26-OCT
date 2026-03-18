package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo;

import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoID;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
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
class UnlockLuogoServiceTest {

    @Mock
    private UnlockLuogoRepository unlockLuogoRepository;

    @Mock
    private LuogoService luogoService;

    @InjectMocks
    private UnlockLuogoService unlockLuogoService;

    private UserEntity createUser() {
        return new UserEntity("testUser", "hash", "USER", "NORD-BRA", "Test Team");
    }

    private LuogoEntity createLuogo(int livello) {
        return new LuogoEntity("NORD-BRA", livello, "Luogo " + livello, 0, 0, "C" + livello, "I" + livello);
    }

    @Test
    void testSalva() {
        UserEntity user = createUser();
        LuogoEntity luogo = createLuogo(1);
        UnlockLuogoEntity ule = new UnlockLuogoEntity(user, luogo, false);

        unlockLuogoService.salva(ule);
        verify(unlockLuogoRepository).save(ule);
    }

    @Test
    void testGetAllLuoghi() {
        UserEntity user = createUser();
        LuogoEntity l1 = createLuogo(1);
        LuogoEntity l2 = createLuogo(2);
        List<UnlockLuogoEntity> list = Arrays.asList(
                new UnlockLuogoEntity(user, l1, true),
                new UnlockLuogoEntity(user, l2, false)
        );
        when(unlockLuogoRepository.getAllByUser(user)).thenReturn(list);

        List<UnlockLuogoEntity> result = unlockLuogoService.getAllLuoghi(user);
        assertEquals(2, result.size());
    }

    @Test
    void testGetCountCompleted() {
        UserEntity user = createUser();
        when(unlockLuogoRepository.findAllByUserAndCompleted(user, true))
                .thenReturn(Arrays.asList(new UnlockLuogoEntity(), new UnlockLuogoEntity()));

        assertEquals(2, unlockLuogoService.getCountCompleted(user));
    }

    @Test
    void testIsUnlockedOrNotCompleted_unlockedNotCompleted() {
        UserEntity user = createUser();
        LuogoEntity l3 = createLuogo(3);
        UnlockLuogoEntity ule = new UnlockLuogoEntity(user, l3, false);
        when(unlockLuogoRepository.getAllByUser(user)).thenReturn(List.of(ule));

        assertTrue(unlockLuogoService.isUnlockedOrNotCompleted(3, user));
    }

    @Test
    void testIsUnlockedOrNotCompleted_unlockedCompleted() {
        UserEntity user = createUser();
        LuogoEntity l3 = createLuogo(3);
        UnlockLuogoEntity ule = new UnlockLuogoEntity(user, l3, true);
        when(unlockLuogoRepository.getAllByUser(user)).thenReturn(List.of(ule));

        assertFalse(unlockLuogoService.isUnlockedOrNotCompleted(3, user));
    }

    @Test
    void testIsUnlockedOrNotCompleted_notUnlocked() {
        UserEntity user = createUser();
        when(unlockLuogoRepository.getAllByUser(user)).thenReturn(Collections.emptyList());

        assertFalse(unlockLuogoService.isUnlockedOrNotCompleted(5, user));
    }

    @Test
    void testFindMaxLivello() {
        UserEntity user = createUser();
        when(unlockLuogoRepository.findMaxLivelloByUserAndRegione(user, "NORD-BRA")).thenReturn(Optional.of(7));

        assertEquals(7, unlockLuogoService.findMaxLivello(user));
    }

    @Test
    void testFindMaxLivelloEmpty() {
        UserEntity user = createUser();
        when(unlockLuogoRepository.findMaxLivelloByUserAndRegione(user, "NORD-BRA")).thenReturn(Optional.empty());

        assertEquals(0, unlockLuogoService.findMaxLivello(user));
    }

    @Test
    void testSetCompleted() {
        UserEntity user = createUser();
        LuogoEntity l2 = createLuogo(2);
        UnlockLuogoEntity ule = new UnlockLuogoEntity(user, l2, false);
        when(unlockLuogoRepository.getAllByUser(user)).thenReturn(List.of(ule));

        unlockLuogoService.setCompleted(2, user);
        assertTrue(ule.isCompleted());
    }

    @Test
    void testUnlockNext() {
        UserEntity user = createUser();
        LuogoEntity nextLuogo = createLuogo(4);
        when(unlockLuogoRepository.findMaxLivelloByUserAndRegione(user, "NORD-BRA")).thenReturn(Optional.of(3));
        when(luogoService.getLuogo(4, "NORD-BRA")).thenReturn(Optional.of(nextLuogo));

        unlockLuogoService.unlockNext(user);
        verify(unlockLuogoRepository).save(any(UnlockLuogoEntity.class));
    }

    @Test
    void testUnlockNextNoMoreLuoghi() {
        UserEntity user = createUser();
        when(unlockLuogoRepository.findMaxLivelloByUserAndRegione(user, "NORD-BRA")).thenReturn(Optional.of(10));
        when(luogoService.getLuogo(11, "NORD-BRA")).thenReturn(Optional.empty());

        unlockLuogoService.unlockNext(user);
        verify(unlockLuogoRepository, never()).save(any());
    }

    @Test
    void testItsUnlocked() {
        UserEntity user = createUser();
        UnlockLuogoID id = new UnlockLuogoID("testUser", new LuogoID("NORD-BRA", 3));
        when(unlockLuogoRepository.findById(id)).thenReturn(Optional.of(new UnlockLuogoEntity()));

        assertTrue(unlockLuogoService.itsUnlocked(user, 3));
    }

    @Test
    void testItsNotUnlocked() {
        UserEntity user = createUser();
        UnlockLuogoID id = new UnlockLuogoID("testUser", new LuogoID("NORD-BRA", 5));
        when(unlockLuogoRepository.findById(id)).thenReturn(Optional.empty());

        assertFalse(unlockLuogoService.itsUnlocked(user, 5));
    }
}
