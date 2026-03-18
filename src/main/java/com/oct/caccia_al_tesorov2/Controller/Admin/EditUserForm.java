package com.oct.caccia_al_tesorov2.Controller.Admin;

import jakarta.validation.constraints.Size;

public class EditUserForm {
    private String oldUsername;
    @Size(min = 3, max = 50, message = "Il nome utente deve essere compreso tra 3 e 50 caratteri")
    private String username;
    private String regione;
    private boolean reset;
    private boolean admin;
    private boolean resetPassword;

    public String getOldUsername() {
        return oldUsername;
    }

    public void setOldUsername(String oldUsername) {
        this.oldUsername = oldUsername;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(boolean resetPassword) {
        this.resetPassword = resetPassword;
    }
}
