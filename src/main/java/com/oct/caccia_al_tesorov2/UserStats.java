package com.oct.caccia_al_tesorov2;

import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetails;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserStats {

    private final SessionRegistry sessionRegistry;

    public UserStats(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    public List<String> getUsersOnline() {
        List<String> names = new ArrayList<>();
        for (Object u : sessionRegistry.getAllPrincipals()) {
            if (!sessionRegistry.getAllSessions(u, false).isEmpty()) {
                if (u instanceof CustomUserDetails user) {
                    names.add(user.getUsername());
                } else if (u instanceof org.springframework.security.core.userdetails.User user) {
                    names.add(user.getUsername());
                } else if (u instanceof String username) {
                    names.add(username);
                }
            }
        }
        return names;
    }
}
