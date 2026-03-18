package com.oct.caccia_al_tesorov2.Controller.Caccia;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class QuesitoController extends AbstractController {
    private final UnlockQuesitoService unlockQuesitoService;
    private final QuesitoService quesitoService;
    private final UnlockLuogoService unlockLuogoService;

    public QuesitoController(CustomUserDetailsService customUserDetailsService, UnlockQuesitoService unlockQuesitoService, QuesitoService quesitoService, UnlockLuogoService unlockLuogoService) {
        super(customUserDetailsService);
        this.unlockQuesitoService = unlockQuesitoService;
        this.quesitoService = quesitoService;
        this.unlockLuogoService = unlockLuogoService;
    }

    @GetMapping("/quesito")
    public String getQuesito(Principal principal, Model model, @RequestParam("livello") int livello) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        else if(!ActivateGameService.isGameActive()) return "redirect:/gamedisabled";
        else if(unlockQuesitoService.itsUnlocked(user,livello)) {
            Optional<QuesitoEntity> q=quesitoService.getQuesito(livello);
            model.addAttribute("user",user);
            model.addAttribute("livello",livello);
            if(q.isEmpty()) return "redirect:/caccia";
            return "CacciaAlTesoro/livelli/"+q.get().getUrlPage();
        }
        else {
            return "redirect:/caccia";
        }
    }

    @PostMapping("/quesito")
    public String checkQuesito(Principal principal, @RequestParam("livello") int livello, @RequestParam("password") String password) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        else if(!ActivateGameService.isGameActive()) return "redirect:/gamedisabled";
        else if(unlockQuesitoService.itsUnlocked(user,livello)) {
            Optional<QuesitoEntity> q=quesitoService.getQuesito(livello);
            if(q.isEmpty()) return "redirect:/caccia";
            QuesitoEntity quesito=q.get();
            if(quesito.getPassword().contains(password)) {
                unlockQuesitoService.setCompleted(user,livello);
                unlockLuogoService.unlockNext(user);
                return "redirect:/caccia?unlockQuesito=true";
            }
            else {
                return "redirect:/quesito?livello="+livello+"&error=err";
            }
        }
        else {
            return "redirect:/caccia";
        }
    }
}
