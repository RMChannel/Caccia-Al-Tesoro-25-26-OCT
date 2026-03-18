package com.oct.caccia_al_tesorov2.Controller.Admin;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoID;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import com.oct.caccia_al_tesorov2.UserStats;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController extends AbstractController {
    private final UserStats userStats;
    private final LuogoService luogoService;

    public AdminController(CustomUserDetailsService customUserDetailsService, UserStats userStats, LuogoService luogoService) {
        super(customUserDetailsService);
        this.userStats = userStats;
        this.luogoService = luogoService;
    }

    @GetMapping("/admin")
    public String getAdminPage(Principal principal, Model model) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        model.addAttribute("user",user);

        // Fetch all users
        List<UserEntity> allUsers = getCustomUserDetailsService().getAllUsers();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("totalusers", allUsers.size());
        List<String> activeUsers=userStats.getUsersOnline();
        model.addAttribute("usersonline", activeUsers);
        model.addAttribute("numberofactiveusers", activeUsers.size());
        model.addAttribute("gameActive", ActivateGameService.isGameActive());
        return "Admin/admin";
    }

    @GetMapping("/admin/update")
    public @ResponseBody ResponseEntity<List<UserEntity>> update(Principal principal, Model model) {
        UserEntity user=checkUser(principal.getName());
        if(user==null || !user.getRole().equals("ADMIN")) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(getCustomUserDetailsService().getAllUsers());
    }

    @GetMapping("/admin/updateonlineusers")
    public @ResponseBody ResponseEntity<List<String>> updateOnlineUsers(Principal principal, Model model) {
        UserEntity user=checkUser(principal.getName());
        if(user==null || !user.getRole().equals("ADMIN")) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userStats.getUsersOnline());
    }

    @PostMapping("/admin/activate-game")
    public ResponseEntity<Boolean> activateGame(Principal principal, Model model) {
        UserEntity user=checkUser(principal.getName());
        if(user==null || !user.getRole().equals("ADMIN")) return ResponseEntity.badRequest().build();
        boolean newState = !ActivateGameService.isGameActive();
        ActivateGameService.setGameActive(newState);
        return ResponseEntity.ok(newState);
    }

    @GetMapping("/admin/luoghi")
    public String getLuoghi(Principal principal, Model model) {
        UserEntity user=checkUser(principal.getName());
        if(user==null || !user.getRole().equals("ADMIN")) return "redirect:/";
        model.addAttribute("user",user);
        model.addAttribute("luoghi",luogoService.getAll());
        return "Admin/luoghi";
    }

    @PostMapping("/admin/update-luogo")
    public String updateLuogo(Principal principal, Model model, @Valid @ModelAttribute EditLuogoForm editLuogoForm, BindingResult bindingResult) {
        UserEntity user=checkUser(principal.getName());
        if(user==null || !user.getRole().equals("ADMIN")) return "redirect:/";
        else if(bindingResult.hasErrors()) return "redirect:/admin/luoghi?error="+bindingResult.getAllErrors().get(0).getDefaultMessage();
        else if(!editLuogoForm.getRegione().equals("NORD-BRA") && !editLuogoForm.getRegione().equals("CENTRO-CIVITAVECCHIA") && !editLuogoForm.getRegione().equals("SUD-MELFI") && !editLuogoForm.getRegione().equals("SUD-ENNA")) {
            return "redirect:/admin/luoghi?error=regione_not_valid";
        }
        Optional<LuogoEntity> luogo = luogoService.getLuogo(editLuogoForm.getLivello(), editLuogoForm.getRegione());
        if(luogo.isEmpty()) {
            return "redirect:/admin/luoghi?error=luogo_not_found";
        }
        else {
            LuogoEntity l=luogo.get();
            l.setNome(editLuogoForm.getNome());
            l.setCodice(editLuogoForm.getCodice());
            l.setIndizio(editLuogoForm.getIndizio());
            l.setDescription(editLuogoForm.getDescription());
            l.setDescriptionPhoto(editLuogoForm.getDescriptionPhoto());
            luogoService.update(l);
            return "redirect:/admin/luoghi";
        }
    }
}
