package com.oct.caccia_al_tesorov2.Controller.Login;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegolamentoController extends AbstractController {

    @Autowired
    public RegolamentoController(CustomUserDetailsService customUserDetailsService) {
        super(customUserDetailsService);
    }

    @GetMapping("/regolamento")
    public String showRegolamento(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            UserEntity user = checkUser(auth.getName());
            model.addAttribute("user", user);
        }
        return "regolamento";
    }
}
