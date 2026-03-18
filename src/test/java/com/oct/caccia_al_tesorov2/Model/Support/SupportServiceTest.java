package com.oct.caccia_al_tesorov2.Model.Support;

import com.oct.caccia_al_tesorov2.Model.Email.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupportServiceTest {

    @Mock
    private SupportRepository supportRepository;

    @Mock
    private EmailService emailService;

    private SupportService createService() {
        return new SupportService(supportRepository, emailService, "sender@test.com", "admin@test.com");
    }

    @Test
    void testGetAllMessages() {
        SupportEntity msg1 = new SupportEntity("a@b.com", "Bug", "Errore", new Date());
        SupportEntity msg2 = new SupportEntity("c@d.com", "Domande", "Aiuto", new Date());
        when(supportRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(msg1, msg2));

        SupportService service = createService();
        List<SupportEntity> result = service.getAllMessages();
        assertEquals(2, result.size());
    }

    @Test
    void testRemoveMessage() {
        SupportService service = createService();
        service.removeMessage(1);
        verify(supportRepository).deleteById(1);
    }

    @Test
    void testRemoveAllMessages() {
        SupportService service = createService();
        service.removeAllMessages();
        verify(supportRepository).deleteAll();
    }

    @Test
    void testAddNewMessageSavesAndSendsEmail() {
        SupportService service = createService();

        service.addNewMessage("user@email.com", "Il mio messaggio", "Bug");

        // Verifica salvataggio nel DB
        ArgumentCaptor<SupportEntity> entityCaptor = ArgumentCaptor.forClass(SupportEntity.class);
        verify(supportRepository).save(entityCaptor.capture());
        SupportEntity saved = entityCaptor.getValue();
        assertEquals("user@email.com", saved.getEmail());
        assertEquals("Bug", saved.getCategoria());
        assertEquals("Il mio messaggio", saved.getMessaggio());
        assertNotNull(saved.getData());

        verify(supportRepository).flush();

        // Verifica invio email
        ArgumentCaptor<SimpleMailMessage> mailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(emailService).sendEmail(mailCaptor.capture());
        SimpleMailMessage mail = mailCaptor.getValue();
        assertEquals("sender@test.com", mail.getFrom());
        assertArrayEquals(new String[]{"admin@test.com"}, mail.getTo());
        assertTrue(mail.getSubject().contains("user@email.com"));
        assertTrue(mail.getSubject().contains("Bug"));
        assertEquals("Il mio messaggio", mail.getText());
    }

    @Test
    void testGetCategories() {
        SupportService service = createService();
        List<String> categories = service.getCategories();
        assertEquals(4, categories.size());
        assertTrue(categories.contains("Domande"));
        assertTrue(categories.contains("Suggerimenti"));
        assertTrue(categories.contains("Bug"));
        assertTrue(categories.contains("Altro"));
    }

    @Test
    void testGetNumberOfMessages() {
        when(supportRepository.findAll()).thenReturn(Arrays.asList(
                new SupportEntity("a@b.com", "Bug", "msg1", new Date()),
                new SupportEntity("c@d.com", "Bug", "msg2", new Date())
        ));

        SupportService service = createService();
        assertEquals(2, service.getNumberOfMessages());
    }
}
