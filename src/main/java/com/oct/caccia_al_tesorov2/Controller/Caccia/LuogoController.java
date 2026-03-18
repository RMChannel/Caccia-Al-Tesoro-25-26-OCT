package com.oct.caccia_al_tesorov2.Controller.Caccia;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
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
public class LuogoController extends AbstractController {
    private final LuogoService luogoService;
    private final UnlockLuogoService unlockLuogoService;
    private final UnlockQuesitoService unlockQuesitoService;

    public LuogoController(CustomUserDetailsService customUserDetailsService, LuogoService luogoService, UnlockLuogoService unlockLuogoService, UnlockQuesitoService unlockQuesitoService) {
        super(customUserDetailsService);
        this.luogoService = luogoService;
        this.unlockLuogoService = unlockLuogoService;
        this.unlockQuesitoService = unlockQuesitoService;
    }

    @GetMapping("/unlock-luogo")
    public String getUnlockLuogo(Principal principal, Model model, @RequestParam("livello") int livello) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        else if(!ActivateGameService.isGameActive()) return "redirect:/gamedisabled";
        else if (unlockLuogoService.isUnlockedOrNotCompleted(livello, user)) {
            Optional<LuogoEntity> luogo = luogoService.getLuogo(livello, user.getRegione());
            if (luogo.isEmpty()) return "redirect:/caccia";
            else model.addAttribute("luogo", luogo.get());
            model.addAttribute("user", user);
            return "CacciaAlTesoro/unlock-luogo";
        }
        else {
            return "redirect:/caccia";
        }
    }

    @GetMapping("/luogo-description")
    public String getLuogoDescription(Principal principal, Model model, @RequestParam("livello") int livello) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        else if(!ActivateGameService.isGameActive()) return "redirect:/gamedisabled";
        else if (unlockLuogoService.itsUnlocked(user, livello)) {
            Optional<LuogoEntity> luogo = luogoService.getLuogo(livello, user.getRegione());
            if (luogo.isEmpty()) return "redirect:/caccia";
            LuogoEntity l = luogo.get();
            if (l.getDescription() == null || l.getDescription().isEmpty()) return "redirect:/caccia?unlockLuogo=true";
            model.addAttribute("luogo", l);
            model.addAttribute("user", user);
            return "CacciaAlTesoro/luogo-description";
        }
        else {
            return "redirect:/caccia";
        }
    }

    @PostMapping("/unlock-luogo")
    public String tryUnlockLuogo(Principal principal, Model model, @RequestParam("livello") int livello, @RequestParam("codice") String codice) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        else if(!ActivateGameService.isGameActive()) return "redirect:/gamedisabled";
        else if (unlockLuogoService.isUnlockedOrNotCompleted(livello, user)) {
            Optional<LuogoEntity> luogo = luogoService.getLuogo(livello, user.getRegione());
            if (luogo.isEmpty()) return "redirect:/caccia";
            LuogoEntity l = luogo.get();
            if (!l.getCodice().equals(codice)) {
                model.addAttribute("codeNotCorrect", true);
                model.addAttribute("luogo", l);
                model.addAttribute("user", user);
                return "CacciaAlTesoro/unlock-luogo";
            } else {
                unlockQuesitoService.unlockNext(user);
                unlockLuogoService.setCompleted(livello, user);
                if (l.getDescription() == null || l.getDescription().isEmpty()) return "redirect:/caccia?unlockLuogo=true";
                else {
                    return "redirect:/luogo-description?livello=" + livello;
                }
            }
        }
        else {
            return "redirect:/caccia";
        }
    }
}
