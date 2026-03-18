package com.oct.caccia_al_tesorov2.Controller.Admin;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.PasswordUtility;
import com.oct.caccia_al_tesorov2.Model.Users.UserAlreadyExistException;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Random;

@Controller
public class ManageUsersController extends AbstractController {
    public ManageUsersController(CustomUserDetailsService customUserDetailsService) {
        super(customUserDetailsService);
    }

    private String generatePassword() {
        String text="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@$";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<10;i++) sb.append(text.charAt(random.nextInt(text.length())));
        return sb.toString();
    }

    @PostMapping("/admin/remove-user")
    public String removeUser(Principal principal, Model model, @RequestParam("username") String username) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        try {
            getCustomUserDetailsService().deleteUser(username);
        } catch (UsernameNotFoundException e) {
            return "redirect:/admin?error=user_not_found";
        }
        return "redirect:/admin?confirm=user_removed";
    }

    @PostMapping("/admin/edit-user")
    public String editUser(Principal principal, Model model, @Valid @ModelAttribute EditUserForm editUserForm, BindingResult bindingResult) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        //Utente non esistente
        UserEntity userEntity=getCustomUserDetailsService().findUser(editUserForm.getOldUsername());
        if(userEntity==null) return "redirect:/admin?error=user_not_found";
        //Verifica regione
        else if(!editUserForm.getRegione().equals("NORD-BRA") && !editUserForm.getRegione().equals("CENTRO-CIVITAVECCHIA") && !editUserForm.getRegione().equals("SUD-MELFI") && !editUserForm.getRegione().equals("SUD-ENNA")) return "redirect:/admin?error=regione_not_valid";
        //Controllo lunghezza nome utente
        else if(bindingResult.hasErrors()) return "redirect:/admin?error="+bindingResult.getAllErrors().get(0).getDefaultMessage();
        else {
            if(editUserForm.isReset() || !editUserForm.getRegione().equals(userEntity.getRegione())) getCustomUserDetailsService().resetUser(userEntity.getUsername());
            
            userEntity.setRegione(editUserForm.getRegione());
            
            if(editUserForm.isAdmin()) userEntity.setRole("ADMIN");
            else userEntity.setRole("USER");
            
            if(editUserForm.isResetPassword())  {
                String password=generatePassword();
                userEntity.setHashPassword(PasswordUtility.hashPassword(password));
                getCustomUserDetailsService().updateUser(userEntity);
                return "redirect:/admin?confirm=password_reset&password="+password;
            }
            
            getCustomUserDetailsService().updateUser(userEntity);
            return "redirect:/admin?confirm=user_edited";
        }
    }

    @PostMapping("/admin/add-user")
    public String addUser(Principal principal, Model model, @Valid @ModelAttribute AddUserForm addUserForm, BindingResult bindingResult) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        if(bindingResult.hasErrors()) return "redirect:/admin?error="+bindingResult.getAllErrors().get(0).getDefaultMessage();
        else {
            UserEntity userEntity;
            String password=generatePassword();
            if(addUserForm.isAdmin()) {
                userEntity=new UserEntity(addUserForm.getUsername(),PasswordUtility.hashPassword(password),"ADMIN");
            }
            else {
                userEntity=new UserEntity(addUserForm.getUsername(),PasswordUtility.hashPassword(password),"USER",addUserForm.getRegione(),addUserForm.getRealName());
            }
            try {
                getCustomUserDetailsService().addNewUser(userEntity);
                return "redirect:/admin?confirm=user_added&password="+password;
            } catch (UserAlreadyExistException e) {
                return "redirect:/admin?error=user_already_exist";
            }
        }
    }

    @PostMapping("/admin/add-users")
    public ResponseEntity<byte[]> addUsers(Principal principal, Model model, @RequestParam("file") MultipartFile file) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return ResponseEntity.badRequest().build();
        if(file.isEmpty()) return ResponseEntity.badRequest().build();
        try (// Lettura del CSV in ingresso
             Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

             // Scrittura del CSV in uscita (in memoria)
             ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader("Squadra", "Nome_Utente", "Regione", "Password")))
            {
                for (CSVRecord record : csvParser) {
                    String username=record.get(1);
                    String nomeReale=record.get(0);
                    String regione=record.get(2);
                    if(username.isEmpty() || nomeReale.isEmpty() || regione.isEmpty()) {
                        System.out.println("Porco dio");
                        return ResponseEntity.status(601).build();
                    }
                    if(!regione.equals("NORD-BRA") && !regione.equals("CENTRO-CIVITAVECCHIA") && !regione.equals("SUD-MELFI") && !regione.equals("SUD-ENNA")) {
                        System.out.println("Porca madonna");
                        return ResponseEntity.status(601).build();
                    }
                    String password=generatePassword();
                    UserEntity userEntity=new UserEntity(username,PasswordUtility.hashPassword(password),"USER",regione,nomeReale);
                    try {
                        getCustomUserDetailsService().addNewUser(userEntity);
                    } catch (UserAlreadyExistException e) {
                        return ResponseEntity.status(602).build();
                    }
                    csvPrinter.printRecord(username,record.get(0),record.get(2),password);
                }
                csvPrinter.flush();
                byte[] csvData = out.toByteArray();
                // Configurazione Headers per la risposta
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("text/csv"));
                headers.setContentDisposition(ContentDisposition.attachment().filename("squadre.csv").build());
                return ResponseEntity.ok().headers(headers).body(csvData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
