package com.oct.caccia_al_tesorov2.Controller.Support;

import com.oct.caccia_al_tesorov2.Model.Support.SupportService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import com.oct.caccia_al_tesorov2.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SupportController.class)
@Import(SecurityConfig.class)
class SupportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private SupportService supportService;

    @Test
    void testGetSupportWithoutAuth() throws Exception {
        mockMvc.perform(get("/support"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("supportForm"))
                .andExpect(view().name("support/support"));
    }

    @Test
    @WithMockUser(username = "team1", roles = {"USER"})
    void testGetSupportWithAuth() throws Exception {
        UserEntity user = new UserEntity("team1", "hash", "USER", "NORD-BRA", "Team 1");
        when(customUserDetailsService.findUser("team1")).thenReturn(user);

        mockMvc.perform(get("/support"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("supportForm"));
    }

    @Test
    void testSendMessageValid() throws Exception {
        when(supportService.getCategories()).thenReturn(List.of("Domande", "Suggerimenti", "Bug", "Altro"));

        // CSRF is disabled in SecurityConfig
        mockMvc.perform(post("/support")
                        .param("email", "user@test.com")
                        .param("categoria", "Bug")
                        .param("messaggio", "Ci sono problemi con il gioco"))
                .andExpect(status().isOk())
                .andExpect(view().name("support/confirmsupport"));

        verify(supportService).addNewMessage("user@test.com", "Ci sono problemi con il gioco", "Bug");
    }

    @Test
    void testSendMessageInvalidCategory() throws Exception {
        when(supportService.getCategories()).thenReturn(List.of("Domande", "Suggerimenti", "Bug", "Altro"));

        mockMvc.perform(post("/support")
                        .param("email", "user@test.com")
                        .param("categoria", "CategoriaInesistente")
                        .param("messaggio", "Mi serve aiuto"))
                .andExpect(status().isOk())
                .andExpect(view().name("support/support"));

        verify(supportService, never()).addNewMessage(any(), any(), any());
    }

    @Test
    void testSendMessageValidationErrors() throws Exception {
        mockMvc.perform(post("/support")
                        .param("email", "ab")
                        .param("categoria", "")
                        .param("messaggio", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("support/support"));

        verify(supportService, never()).addNewMessage(any(), any(), any());
    }
}
