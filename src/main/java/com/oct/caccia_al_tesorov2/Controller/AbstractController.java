package com.oct.caccia_al_tesorov2.Controller;

import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractController {
    private final CustomUserDetailsService customUserDetailsService;

    public AbstractController(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    protected UserEntity checkUser(String username) {
        try {
            return customUserDetailsService.findUser(username);
        } catch (UsernameNotFoundException ue) {
            return null;
        }
    }

    protected CustomUserDetailsService getCustomUserDetailsService() {
        return customUserDetailsService;
    }
}
