package com.oct.caccia_al_tesorov2.Model.Quesito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuesitoServiceTest {

    @Mock
    private QuesitoRepository quesitoRepository;

    @InjectMocks
    private QuesitoService quesitoService;

    @Test
    void testSalva() {
        QuesitoEntity qe = new QuesitoEntity(1, "torre-hanoi", "password123");
        quesitoService.salva(qe);
        verify(quesitoRepository).save(qe);
    }

    @Test
    void testGetCount() {
        when(quesitoRepository.findAll()).thenReturn(Arrays.asList(
                new QuesitoEntity(1, "q1", "p1"),
                new QuesitoEntity(2, "q2", "p2"),
                new QuesitoEntity(3, "q3", "p3")
        ));

        assertEquals(3, quesitoService.getCount());
    }

    @Test
    void testGetCountEmpty() {
        when(quesitoRepository.findAll()).thenReturn(Arrays.asList());
        assertEquals(0, quesitoService.getCount());
    }

    @Test
    void testGetQuesitoFound() {
        QuesitoEntity qe = new QuesitoEntity(5, "canzone", "pswCanzone");
        when(quesitoRepository.findByLivello(5)).thenReturn(Optional.of(qe));

        Optional<QuesitoEntity> result = quesitoService.getQuesito(5);
        assertTrue(result.isPresent());
        assertEquals("canzone", result.get().getUrlPage());
        assertEquals("pswCanzone", result.get().getPassword());
    }

    @Test
    void testGetQuesitoNotFound() {
        when(quesitoRepository.findByLivello(99)).thenReturn(Optional.empty());

        Optional<QuesitoEntity> result = quesitoService.getQuesito(99);
        assertFalse(result.isPresent());
    }
}
