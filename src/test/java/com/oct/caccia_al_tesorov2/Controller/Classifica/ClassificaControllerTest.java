package com.oct.caccia_al_tesorov2.Controller.Classifica;

import com.oct.caccia_al_tesorov2.Model.ClassificaService;
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

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClassificaController.class)
@Import(SecurityConfig.class)
class ClassificaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private ClassificaService classificaService;

    @Test
    void testGetClassificaWithoutAuth() throws Exception {
        when(classificaService.getLeaderboard()).thenReturn(List.of());

        mockMvc.perform(get("/classifica"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("leaderboard"))
                .andExpect(view().name("Classifica/classifica"));
    }

    @Test
    @WithMockUser(username = "team1", roles = {"USER"})
    void testGetClassificaWithAuth() throws Exception {
        UserEntity user = new UserEntity("team1", "hash", "USER", "NORD-BRA", "Team 1");
        when(customUserDetailsService.findUser("team1")).thenReturn(user);
        when(classificaService.getLeaderboard()).thenReturn(List.of());

        mockMvc.perform(get("/classifica"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("leaderboard"));
    }

    @Test
    void testUpdateClassificaReturnsJson() throws Exception {
        Map<String, Object> entry = new HashMap<>();
        entry.put("login_username", "team1");
        entry.put("username", "Squadra 1");
        entry.put("score", 10);
        entry.put("regione", "NORD-BRA");
        entry.put("completed", false);
        entry.put("completedAt", null);
        entry.put("rawTime", null);

        when(classificaService.getLeaderboard()).thenReturn(List.of(entry));

        // CSRF is disabled in SecurityConfig so no need for .with(csrf())
        mockMvc.perform(post("/update-classifica"))
                .andExpect(status().isOk());
    }
}
