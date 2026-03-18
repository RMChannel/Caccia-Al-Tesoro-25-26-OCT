package com.oct.caccia_al_tesorov2.Controller.Login;

import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    @GetMapping({"/home","/"})
    public String HomePage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()  // prendi il primo ruolo
                .orElse("USER");

        return switch (role) {
            case "ROLE_ADMIN" -> "redirect:/admin";
            case "USER", "ROLE_USER" -> "redirect:/caccia";
            default -> "index";
        };
    }

    @GetMapping("/gamedisabled")
    public String gameDisabled() {
        if(ActivateGameService.isGameActive()) return "redirect:/caccia";
        else return "gamedisabled";
    }
}
