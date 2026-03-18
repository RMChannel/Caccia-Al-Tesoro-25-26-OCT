package com.oct.caccia_al_tesorov2.Controller.Login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.security.Principal;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage(Principal principal) throws IOException {
        if(principal==null) return "redirect:/";
        else return "redirect:/home";
    }
}