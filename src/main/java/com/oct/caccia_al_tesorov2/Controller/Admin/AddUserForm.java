package com.oct.caccia_al_tesorov2.Controller.Admin;

import jakarta.validation.constraints.Size;

public class AddUserForm {
    @Size(min = 3, max = 50, message = "Il nome utente deve essere compreso tra 3 e 50 caratteri")
    private String username;

    @Size(min = 3, max = 50, message = "Il nome deve essere compresa tra 3 e 50 caratteri")
    private String realName;
    private String regione;
    private boolean admin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
