package com.oct.caccia_al_tesorov2.Controller.Caccia;

import com.oct.caccia_al_tesorov2.Controller.AbstractController;
import com.oct.caccia_al_tesorov2.Model.ActivateGameService;
import com.oct.caccia_al_tesorov2.Model.ClassificaService;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class CacciaController extends AbstractController {
    private final UnlockLuogoService unlockLuogoService;
    private final UnlockQuesitoService unlockQuesitoService;
    private final LuogoService luogoService;
    private final QuesitoService quesitoService;
    private final ClassificaService classificaService;

    public CacciaController(CustomUserDetailsService customUserDetailsService, UnlockLuogoService unlockLuogoService, UnlockQuesitoService unlockQuesitoService, LuogoService luogoService, QuesitoService quesitoService, ClassificaService classificaService) {
        super(customUserDetailsService);
        this.unlockLuogoService = unlockLuogoService;
        this.unlockQuesitoService = unlockQuesitoService;
        this.luogoService = luogoService;
        this.quesitoService = quesitoService;
        this.classificaService = classificaService;
    }

    @GetMapping("/caccia")
    public String getHome(Principal principal, Model model) {
        UserEntity user=checkUser(principal.getName());
        if(user==null) return "redirect:/login";
        if(user.getRole().equals("ADMIN")) return "redirect:/admin";
        else if(!ActivateGameService.isGameActive()) return "redirect:/gamedisabled";
        else if(user.isFirstLogin()) {
            getCustomUserDetailsService().avoidFirstLogin(user);
            model.addAttribute("user", user);
            LuogoEntity l=new LuogoEntity();
            l.setLivello(0);
            switch (user.getRegione()) {
                case "NORD-BRA":
                    l.setNome("Bra");
                    l.setDescription("Bra è situata nel cuore del Roero, a pochi chilometri dalle Langhe, in Piemonte. È una città ricca di storia, arte e cultura, famosa in tutto il mondo per essere la culla del movimento Slow Food e per la sua eccellente gastronomia, tra cui spicca la rinomata Salsiccia di Bra. Il simbolo indiscusso della città è la Zizzola, una villa ottocentesca a pianta ottagonale situata sul punto più alto del colle Monte Guglielmo, da cui si gode una vista mozzafiato sull'arco alpino e sulle colline circostanti. Bra vanta anche splendidi esempi di barocco piemontese, come il Palazzo Comunale e la Chiesa di Santa Chiara, progettati dall'architetto Bernardo Antonio Vittone.\n");
                    l.setDescriptionPhoto("https://upload.wikimedia.org/wikipedia/commons/5/51/Bra-panorama.jpg");
                    break;
                case "CENTRO-CIVITAVECCHIA":
                    l.setNome("Civitavecchia");
                    l.setDescription("Civitavecchia nasce come piccolo insediamento etrusco, per poi assumere definitivamente il nome di Centumcellae in epoca romana, quando l'imperatore Traiano (106 d.C.) intuì che il litorale scoglioso con le sue molte insenature era il luogo adatto per la costruzione di un grande porto. Centumcellae conobbe il periodo di massimo splendore in età imperiale, dal 314 al 538 d.C. Successivamente la città andò sotto il comando dell'impero dei Bizantini, per poi passare nell' VIII secolo sotto il più mite governo dei papi. La maggior parte dei grandi monumenti cittadini presenti nel porto ed in città si devono proprio al volere dei molti Pontefici che si sono succeduti al comando. Nell'828 la città fu occupata dai Saraceni che la distrussero quasi completamente. I superstiti si rifugiarono in un piccolo borgo nei boschi di Tolfa e fondarono un nuovo centro che prese il nome di Leopoli.\n");
                    l.setDescriptionPhoto("https://www.italia.it/content/dam/tdh/it/destinations/lazio/civitavecchia/media/2480X1000_civitavecchia_destination.jpg");
                    break;
                case "SUD-MELFI":
                    l.setNome("Melfi");
                    l.setDescription("Città medioevale che sorge sul territorio vulcanico alle pendici nord del Monte Vulture. Melfi fu abitata sin dal neolitico e subì l'influenza romana, come è confermato dall'esistenza di alcuni ruderi di una villa romana con mosaici. Successivamente al dominio di Roma, subì l'influenza longobarda, poi quella bizantina, e nel 1041 divenne la prima contea dei Normanni in Italia. \n" +
                            "Guglielmo d'Altavilla vi fece costruire un Castello, che è senza dubbio il più noto della regione. Ampliato dagli Svevi e poi dagli Angioini e Federico II nel 1231, qui furono promulgate le Costitutiones Augustales, il primo testo organico di leggi scritte dell'età medioevale e di contenuto sia penale che civile.\n" +
                            "Negli ultimi anni Melfi è diventata un attivo centro industriale e agricolo.\n");
                    l.setDescriptionPhoto("https://t3.ftcdn.net/jpg/02/13/07/24/360_F_213072462_dWO6ELsXr9k3m7MuwwBwa6cVw0m0w5jP.jpg");
                    break;
                case "SUD-ENNA":
                    l.setNome("Enna");
                    l.setDescription("Enna fu abitata fin dalla preistoria dai Sicani e poi dai Siculi. La sua posizione, a oltre 900 metri di altezza, la rendeva una fortezza naturale difficile da conquistare. Dall’alto si potevano controllare le vie interne della Sicilia e le terre agricole, che erano fondamentali per l’economia antica. Per questo Enna ebbe fin dalle origini un ruolo strategico e importante nella storia dell’isola.");
                    l.setDescriptionPhoto("https://www.sicilia.info/wp-content/uploads/sites/91/enna-castello-lombardi-hd.jpg");
            }
            model.addAttribute("luogo",l);
            return "CacciaAlTesoro/luogo-description";
        }
        else if(getCustomUserDetailsService().haveWon(user)) {
            model.addAttribute("user",user);
            model.addAttribute("posizione_globale",classificaService.getPositionGlobally(user));
            model.addAttribute("posizione_locale",classificaService.getPositionInRegion(user));
            return "CacciaAlTesoro/completed";
        }
        else {
            model.addAttribute("user",user);
            model.addAttribute("luoghi",unlockLuogoService.getAllLuoghi(user));
            model.addAttribute("quesiti",unlockQuesitoService.getAllQuesiti(user));
            model.addAttribute("maxLuoghi",luogoService.getCount(user.getRegione()));
            model.addAttribute("maxQuesiti",quesitoService.getCount());
            model.addAttribute("completedQuesiti",unlockQuesitoService.getCountCompleted(user));
            model.addAttribute("completedLuoghi",unlockLuogoService.getCountCompleted(user));
            return "CacciaAlTesoro/dashboard";
        }
    }
}
