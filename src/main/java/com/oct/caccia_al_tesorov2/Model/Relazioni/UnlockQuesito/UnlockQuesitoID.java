package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockQuesito;

import java.io.Serializable;
import java.util.Objects;

public class UnlockQuesitoID implements Serializable {
    private String user;
    private int quesito;

    public UnlockQuesitoID() {}

    public UnlockQuesitoID(String user, int quesito) {
        this.user = user;
        this.quesito = quesito;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getQuesito() {
        return quesito;
    }

    public void setQuesito(int quesito) {
        this.quesito = quesito;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnlockQuesitoID that = (UnlockQuesitoID) o;
        return Objects.equals(user, that.user) && Objects.equals(quesito, that.quesito);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, quesito);
    }
}
