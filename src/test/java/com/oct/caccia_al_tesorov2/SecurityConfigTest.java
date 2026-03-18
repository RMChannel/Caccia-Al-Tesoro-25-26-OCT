package com.oct.caccia_al_tesorov2;

import com.oct.caccia_al_tesorov2.Model.ClassificaService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private ClassificaService classificaService;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.UserStats userStats;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService luogoService;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService quesitoService;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService unlockLuogoService;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService unlockQuesitoService;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Support.SupportService supportService;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Email.EmailService emailService;

    @Test
    void testPublicPagesAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/classifica"))
                .andExpect(status().isOk());
    }

    @Test
    void testSupportPageAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/support"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminRequiresAuthentication() throws Exception {
        // Without auth, form login redirects to /login
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testCacciaRequiresAuthentication() throws Exception {
        // Without auth, form login redirects to /login
        mockMvc.perform(get("/caccia"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "normalUser", roles = {"USER"})
    void testAdminDeniedForNonAdminUser() throws Exception {
        // Spring Security blocks access with 403 before the controller is reached
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testAdminAccessibleForAdmin() throws Exception {
        UserEntity admin = new UserEntity("adminUser", "hash", "ADMIN");
        org.mockito.Mockito.when(customUserDetailsService.findUser("adminUser")).thenReturn(admin);
        org.mockito.Mockito.when(customUserDetailsService.getAllUsers()).thenReturn(java.util.List.of(admin));
        org.mockito.Mockito.when(userStats.getUsersOnline()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testCacciaAccessibleForUser() throws Exception {
        UserEntity user = new UserEntity("testUser", "hash", "USER", "NORD-BRA", "Test");
        user.setFirstLogin(false);
        org.mockito.Mockito.when(customUserDetailsService.findUser("testUser")).thenReturn(user);
        org.mockito.Mockito.when(unlockLuogoService.getAllLuoghi(user)).thenReturn(java.util.List.of());
        org.mockito.Mockito.when(unlockQuesitoService.getAllQuesiti(user)).thenReturn(java.util.List.of());
        org.mockito.Mockito.when(luogoService.getCount("NORD-BRA")).thenReturn(10);
        org.mockito.Mockito.when(quesitoService.getCount()).thenReturn(10);
        org.mockito.Mockito.when(unlockQuesitoService.getCountCompleted(user)).thenReturn(0);
        org.mockito.Mockito.when(unlockLuogoService.getCountCompleted(user)).thenReturn(0);
        com.oct.caccia_al_tesorov2.Model.ActivateGameService.setGameActive(true);

        mockMvc.perform(get("/caccia"))
                .andExpect(status().isOk());

        com.oct.caccia_al_tesorov2.Model.ActivateGameService.setGameActive(false);
    }
}
