package com.oct.caccia_al_tesorov2.Controller.Login;

import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import com.oct.caccia_al_tesorov2.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "adminUser", roles = {"ADMIN"})
    void testHomePageRedirectsAdminToAdmin() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));
    }

    @Test
    @WithMockUser(username = "normalUser", roles = {"USER"})
    void testHomePageRedirectsUserToCaccia() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/caccia"));
    }

    @Test
    @WithMockUser(username = "normalUser", roles = {"USER"})
    void testRootRedirectsUserToCaccia() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/caccia"));
    }

    @Test
    @WithMockUser(username = "normalUser", roles = {"USER"})
    void testGameDisabledRedirectsToCacciaWhenActive() throws Exception {
        ActivateGameService.setGameActive(true);
        mockMvc.perform(get("/gamedisabled"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/caccia"));
        ActivateGameService.setGameActive(false);
    }

    @Test
    @WithMockUser(username = "normalUser", roles = {"USER"})
    void testGameDisabledShowsPageWhenInactive() throws Exception {
        ActivateGameService.setGameActive(false);
        mockMvc.perform(get("/gamedisabled"))
                .andExpect(status().isOk());
    }
}
