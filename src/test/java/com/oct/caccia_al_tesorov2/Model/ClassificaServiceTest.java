package com.oct.caccia_al_tesorov2.Model;

import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService;
import com.oct.caccia_al_tesorov2.Model.Users.CustomUserDetailsService;
import com.oct.caccia_al_tesorov2.Model.Users.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificaServiceTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private UnlockQuesitoService unlockQuesitoService;

    @Mock
    private UnlockLuogoService unlockLuogoService;

    @InjectMocks
    private ClassificaService classificaService;

    private UserEntity createUser(String username, String role, String regione, String realName) {
        return new UserEntity(username, "hash", role, regione, realName);
    }

    @Test
    void testGetLeaderboardFiltersAdmins() {
        UserEntity admin = createUser("admin", "ADMIN", "NORD-BRA", "Admin");
        UserEntity user1 = createUser("team1", "USER", "NORD-BRA", "Squadra 1");

        when(customUserDetailsService.getAllUsers()).thenReturn(Arrays.asList(admin, user1));
        when(unlockQuesitoService.getCountCompleted(user1)).thenReturn(3);
        when(unlockLuogoService.getCountCompleted(user1)).thenReturn(2);

        List<Map<String, Object>> leaderboard = classificaService.getLeaderboard();
        assertEquals(1, leaderboard.size());
        assertEquals("team1", leaderboard.get(0).get("login_username"));
        assertEquals(5, leaderboard.get(0).get("score"));
    }

    @Test
    void testGetLeaderboardSortsByScore() {
        UserEntity user1 = createUser("team1", "USER", "NORD-BRA", "Squadra 1");
        UserEntity user2 = createUser("team2", "USER", "SUD-MELFI", "Squadra 2");

        when(customUserDetailsService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(unlockQuesitoService.getCountCompleted(user1)).thenReturn(3);
        when(unlockLuogoService.getCountCompleted(user1)).thenReturn(2);
        when(unlockQuesitoService.getCountCompleted(user2)).thenReturn(8);
        when(unlockLuogoService.getCountCompleted(user2)).thenReturn(7);

        List<Map<String, Object>> leaderboard = classificaService.getLeaderboard();
        assertEquals(2, leaderboard.size());
        // team2 has higher score (15) than team1 (5)
        assertEquals("team2", leaderboard.get(0).get("login_username"));
        assertEquals("team1", leaderboard.get(1).get("login_username"));
    }

    @Test
    void testGetLeaderboardTieBreakerByTime() {
        UserEntity user1 = createUser("team1", "USER", "NORD-BRA", "Squadra 1");
        UserEntity user2 = createUser("team2", "USER", "NORD-BRA", "Squadra 2");

        // Same score
        when(customUserDetailsService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(unlockQuesitoService.getCountCompleted(user1)).thenReturn(10);
        when(unlockLuogoService.getCountCompleted(user1)).thenReturn(10);
        when(unlockQuesitoService.getCountCompleted(user2)).thenReturn(10);
        when(unlockLuogoService.getCountCompleted(user2)).thenReturn(10);

        // team2 completed earlier
        user1.setCompleted(LocalDateTime.of(2026, 3, 18, 14, 0));
        user2.setCompleted(LocalDateTime.of(2026, 3, 18, 12, 0));

        List<Map<String, Object>> leaderboard = classificaService.getLeaderboard();
        // team2 should be first because completed earlier
        assertEquals("team2", leaderboard.get(0).get("login_username"));
        assertEquals("team1", leaderboard.get(1).get("login_username"));
    }

    @Test
    void testGetPositionGlobally() {
        UserEntity user1 = createUser("team1", "USER", "NORD-BRA", "Squadra 1");
        UserEntity user2 = createUser("team2", "USER", "SUD-MELFI", "Squadra 2");

        when(customUserDetailsService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));
        when(unlockQuesitoService.getCountCompleted(user1)).thenReturn(8);
        when(unlockLuogoService.getCountCompleted(user1)).thenReturn(7);
        when(unlockQuesitoService.getCountCompleted(user2)).thenReturn(3);
        when(unlockLuogoService.getCountCompleted(user2)).thenReturn(2);

        assertEquals(1, classificaService.getPositionGlobally(user1));
        assertEquals(2, classificaService.getPositionGlobally(user2));
    }

    @Test
    void testGetPositionInRegion() {
        UserEntity user1 = createUser("team1", "USER", "NORD-BRA", "Squadra 1");
        UserEntity user2 = createUser("team2", "USER", "NORD-BRA", "Squadra 2");
        UserEntity user3 = createUser("team3", "USER", "SUD-MELFI", "Squadra 3");

        when(customUserDetailsService.getAllUsers()).thenReturn(Arrays.asList(user1, user2, user3));
        when(unlockQuesitoService.getCountCompleted(user1)).thenReturn(8);
        when(unlockLuogoService.getCountCompleted(user1)).thenReturn(7);
        when(unlockQuesitoService.getCountCompleted(user2)).thenReturn(3);
        when(unlockLuogoService.getCountCompleted(user2)).thenReturn(2);
        when(unlockQuesitoService.getCountCompleted(user3)).thenReturn(10);
        when(unlockLuogoService.getCountCompleted(user3)).thenReturn(10);

        // In NORD-BRA: team1 is first (15), team2 is second (5)
        assertEquals(1, classificaService.getPositionInRegion(user1));
        assertEquals(2, classificaService.getPositionInRegion(user2));
        // In SUD-MELFI: team3 is first (solo)
        assertEquals(1, classificaService.getPositionInRegion(user3));
    }

    @Test
    void testGetPositionUserNotFound() {
        UserEntity user1 = createUser("team1", "USER", "NORD-BRA", "Squadra 1");
        UserEntity unknown = createUser("unknown", "USER", "NORD-BRA", "Unknown");

        when(customUserDetailsService.getAllUsers()).thenReturn(List.of(user1));
        when(unlockQuesitoService.getCountCompleted(user1)).thenReturn(5);
        when(unlockLuogoService.getCountCompleted(user1)).thenReturn(5);

        assertEquals(-1, classificaService.getPositionGlobally(unknown));
    }

    @Test
    void testLeaderboardEntryContainsAllFields() {
        UserEntity user = createUser("team1", "USER", "NORD-BRA", "Squadra 1");
        user.setCompleted(LocalDateTime.of(2026, 3, 18, 14, 30));

        when(customUserDetailsService.getAllUsers()).thenReturn(List.of(user));
        when(unlockQuesitoService.getCountCompleted(user)).thenReturn(10);
        when(unlockLuogoService.getCountCompleted(user)).thenReturn(10);

        List<Map<String, Object>> leaderboard = classificaService.getLeaderboard();
        Map<String, Object> entry = leaderboard.get(0);

        assertEquals("team1", entry.get("login_username"));
        assertEquals("Squadra 1", entry.get("username"));
        assertEquals(20, entry.get("score"));
        assertEquals("NORD-BRA", entry.get("regione"));
        assertEquals(true, entry.get("completed"));
        assertNotNull(entry.get("completedAt"));
        assertEquals("18/03 14:30", entry.get("completedAt"));
    }
}
