package com.oct.caccia_al_tesorov2.Controller.Classifica;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.ClassificaService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class ClassificaController extends AbstractController {
    private final ClassificaService classificaService;


    public ClassificaController(CustomUserDetailsService customUserDetailsService, ClassificaService classificaService) {
        super(customUserDetailsService);
        this.classificaService = classificaService;
    }

    @GetMapping("/classifica")
    public String getClassifica(Principal principal, Model model) {
        if(principal != null) {
            UserEntity user = checkUser(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("leaderboard", classificaService.getLeaderboard());
        return "Classifica/classifica";
    }

    @PostMapping("/update-classifica")
    @ResponseBody
    public List<Map<String, Object>> updateClassifica(Principal principal, Model model) {
        return classificaService.getLeaderboard();
    }
}
