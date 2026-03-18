package com.oct.caccia_al_tesorov2.Controller.Support;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.Support.SupportService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class SupportController extends AbstractController {
    private final SupportService supportService;

    public SupportController(CustomUserDetailsService customUserDetailsService, SupportService supportService) {
        super(customUserDetailsService);
        this.supportService = supportService;
    }

    @GetMapping("/support")
    public String support(Principal principal, Model model) {
        if(principal != null) {
            UserEntity user = checkUser(principal.getName());
            model.addAttribute("user", user);
        }
        model.addAttribute("supportForm", new SupportForm());
        return "support/support";
    }

    @PostMapping("/support")
    public String sendMessage(Principal principal, Model model, @Valid @ModelAttribute SupportForm supportForm, BindingResult bindingResult) {
        UserEntity user = null;
        if(principal != null) {
            user = checkUser(principal.getName());
            model.addAttribute("user", user);
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("supportForm", supportForm);
            return "support/support";
        }
        if(!supportService.getCategories().contains(supportForm.getCategoria())) {
            bindingResult.rejectValue("categoria","categoria.notfound","La categoria inserita non è valida.");
            model.addAttribute("supportForm", supportForm);
            return "support/support";
        }
        supportService.addNewMessage(supportForm.getEmail(),supportForm.getMessaggio(),supportForm.getCategoria());
        return "support/confirmsupport";
    }
}
