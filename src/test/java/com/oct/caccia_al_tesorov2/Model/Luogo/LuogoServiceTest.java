package com.oct.caccia_al_tesorov2.Model.Luogo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LuogoServiceTest {

    @Mock
    private LuogoRepository luogoRepository;

    @InjectMocks
    private LuogoService luogoService;

    @Test
    void testGetAllUnlocked() {
        LuogoEntity l1 = new LuogoEntity("NORD-BRA", 1, "Luogo 1", 0, 0, "C1", "I1");
        LuogoEntity l2 = new LuogoEntity("NORD-BRA", 2, "Luogo 2", 0, 0, "C2", "I2");
        when(luogoRepository.getLuogoEntitiesByLivelloLessThanEqual(2)).thenReturn(Arrays.asList(l1, l2));

        List<LuogoEntity> result = luogoService.getAllUnlocked(2);
        assertEquals(2, result.size());
        verify(luogoRepository).getLuogoEntitiesByLivelloLessThanEqual(2);
    }

    @Test
    void testSalva() {
        LuogoEntity luogo = new LuogoEntity("NORD-BRA", 1, "Test", 0, 0, "C", "I");
        luogoService.salva(luogo);
        verify(luogoRepository).save(luogo);
    }

    @Test
    void testGetCount() {
        List<Object> mockList = Arrays.asList(new Object(), new Object(), new Object());
        when(luogoRepository.findAllByRegione("NORD-BRA")).thenReturn((Collection<Object>) (Collection<?>) mockList);

        int count = luogoService.getCount("NORD-BRA");
        assertEquals(3, count);
    }

    @Test
    void testGetLuogo() {
        LuogoEntity luogo = new LuogoEntity("SUD-MELFI", 5, "Test", 0, 0, "C", "I");
        when(luogoRepository.findById(new LuogoID("SUD-MELFI", 5))).thenReturn(Optional.of(luogo));

        Optional<LuogoEntity> result = luogoService.getLuogo(5, "SUD-MELFI");
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getNome());
    }

    @Test
    void testGetLuogoNotFound() {
        when(luogoRepository.findById(new LuogoID("SUD-MELFI", 99))).thenReturn(Optional.empty());

        Optional<LuogoEntity> result = luogoService.getLuogo(99, "SUD-MELFI");
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateSuccess() {
        LuogoEntity luogo = new LuogoEntity("NORD-BRA", 1, "Updated", 0, 0, "C", "I");
        when(luogoRepository.findById(new LuogoID("NORD-BRA", 1))).thenReturn(Optional.of(luogo));

        luogoService.update(luogo);
        verify(luogoRepository).save(luogo);
    }

    @Test
    void testUpdateThrowsLuogoNotFoundException() {
        LuogoEntity luogo = new LuogoEntity("NORD-BRA", 99, "Not found", 0, 0, "C", "I");
        when(luogoRepository.findById(new LuogoID("NORD-BRA", 99))).thenReturn(Optional.empty());

        assertThrows(LuogoNotFoundException.class, () -> luogoService.update(luogo));
        verify(luogoRepository, never()).save(any());
    }

    @Test
    void testGetAll() {
        LuogoEntity l1 = new LuogoEntity("NORD-BRA", 1, "L1", 0, 0, "C1", "I1");
        LuogoEntity l2 = new LuogoEntity("SUD-MELFI", 1, "L2", 0, 0, "C2", "I2");
        when(luogoRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

        List<LuogoEntity> result = luogoService.getAll();
        assertEquals(2, result.size());
    }
}
