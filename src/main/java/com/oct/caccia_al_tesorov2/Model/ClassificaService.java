package com.oct.caccia_al_tesorov2.Model;

import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassificaService {
    private final CustomUserDetailsService customUserDetailsService;
    private final UnlockQuesitoService unlockQuesitoService;
    private final UnlockLuogoService unlockLuogoService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    public ClassificaService(CustomUserDetailsService customUserDetailsService, UnlockQuesitoService unlockQuesitoService, UnlockLuogoService unlockLuogoService) {
        this.customUserDetailsService = customUserDetailsService;
        this.unlockQuesitoService = unlockQuesitoService;
        this.unlockLuogoService = unlockLuogoService;
    }

    public List<Map<String, Object>> getLeaderboard() {
        return customUserDetailsService.getAllUsers().stream()
                .filter(u -> !"ADMIN".equals(u.getRole()))
                .map(u -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("login_username", u.getUsername());
                    entry.put("username", u.getRealName());
                    entry.put("score", unlockQuesitoService.getCountCompleted(u)+unlockLuogoService.getCountCompleted(u));
                    entry.put("regione",u.getRegione());
                    entry.put("completed", u.isCompleted());
                    entry.put("completedAt", u.getCompleted() != null ? u.getCompleted().format(formatter) : null);
                    entry.put("rawTime", u.getCompleted());
                    return entry;
                })
                .sorted((a, b) -> {
                    int scoreDiff = (Integer) b.get("score") - (Integer) a.get("score");
                    if (scoreDiff != 0) return scoreDiff;
                    
                    // Tie breaker by completion time (if both completed)
                    java.time.LocalDateTime timeA = (java.time.LocalDateTime) a.get("rawTime");
                    java.time.LocalDateTime timeB = (java.time.LocalDateTime) b.get("rawTime");
                    
                    if (timeA == null && timeB == null) return 0;
                    if (timeA == null) return 1;
                    if (timeB == null) return -1;
                    
                    return timeA.compareTo(timeB);
                })
                .collect(Collectors.toList());
    }

    public int getPositionGlobally(UserEntity user) {
        List<Map<String, Object>> leaderboard = getLeaderboard();
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).get("login_username").equals(user.getUsername())) {
                return i + 1;
            }
        }
        return -1;
    }

    public int getPositionInRegion(UserEntity user) {
        List<Map<String, Object>> leaderboard = getLeaderboard().stream()
                .filter(e -> e.get("regione").equals(user.getRegione()))
                .collect(Collectors.toList());
        
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).get("login_username").equals(user.getUsername())) {
                return i + 1;
            }
        }
        return -1;
    }
}
