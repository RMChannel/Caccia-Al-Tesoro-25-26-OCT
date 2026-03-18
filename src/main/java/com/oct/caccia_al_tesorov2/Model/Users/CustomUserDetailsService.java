package com.oct.caccia_al_tesorov2.Model.Users;

import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoService;
import com.oct.caccia_al_tesorov2.Model.Quesito.QuesitoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoEntity;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo.UnlockLuogoService;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoEntity;
import com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito.UnlockQuesitoService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final QuesitoService quesitoService;
    private final UnlockQuesitoService unlockQuesitoService;
    private final LuogoService luogoService;
    private final UnlockLuogoService unlockLuogoService;

    public CustomUserDetailsService(UserRepository userRepository, QuesitoService quesitoService, UnlockQuesitoService unlockQuesitoService, LuogoService luogoService, UnlockLuogoService unlockLuogoService) {
        this.userRepository = userRepository;
        this.quesitoService = quesitoService;
        this.unlockQuesitoService = unlockQuesitoService;
        this.luogoService = luogoService;
        this.unlockLuogoService = unlockLuogoService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
        return new CustomUserDetails(user);
    }

    public UserEntity findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + username));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserEntity> GetAllGiudici() {
        return userRepository.getAllByRole("GIUDICE");
    }

    @Transactional
    public void addUser(UserEntity user) throws UserAlreadyExistException {
        try {
            findUser(user.getUsername());
            throw new UserAlreadyExistException();
        } catch (UsernameNotFoundException ue) {
            userRepository.save(user);
        }
    }

    protected void defaultAdd(UserEntity user) {
        unlockQuesitoService.salva(new UnlockQuesitoEntity(user,quesitoService.getQuesito(1).get(),false));
        unlockQuesitoService.salva(new UnlockQuesitoEntity(user,quesitoService.getQuesito(2).get(),false));
        unlockLuogoService.salva(new UnlockLuogoEntity(user,luogoService.getLuogo(1,user.getRegione()).get(),false));
        unlockLuogoService.salva(new UnlockLuogoEntity(user,luogoService.getLuogo(2,user.getRegione()).get(),false));
    }

    @Transactional
    public void addNewUser(UserEntity user) throws UserAlreadyExistException {
        try {
            findUser(user.getUsername());
            throw new UserAlreadyExistException();
        } catch (UsernameNotFoundException ue) {
            userRepository.save(user);
            defaultAdd(user);
        }
    }

    @Transactional
    public void updateUser(UserEntity user) {
        userRepository.save(user);
    }

    @Transactional
    public void avoidFirstLogin(UserEntity user) {
        user.setFirstLogin(false);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }

    @Transactional
    public void resetUser(String username) {
        UserEntity user = findUser(username);
        user.getUnlockLuogoEntities().removeAll(user.getUnlockLuogoEntities());
        user.getUnlockQuesitoEntities().removeAll(user.getUnlockQuesitoEntities());
        defaultAdd(user);
        updateUser(user);
    }

    public boolean haveWon(UserEntity user) {
        if(user.isCompleted()) return true;
        else if(unlockQuesitoService.getCountCompleted(user)+unlockLuogoService.getCountCompleted(user)==20) {
            LocalDateTime now = LocalDateTime.now().plusHours(1);
            user.setCompleted(now);
            userRepository.save(user);
            return true;
        }
        else return false;
    }
}