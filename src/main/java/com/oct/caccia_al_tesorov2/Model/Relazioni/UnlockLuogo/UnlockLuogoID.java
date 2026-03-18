package com.oct.caccia_al_tesorov2.Model.Relazioni.UnlockLuogo;

import com.oct.caccia_al_tesorov2.Model.Luogo.LuogoID;
import java.io.Serializable;
import java.util.Objects;

public class UnlockLuogoID implements Serializable {
    private String user;
    private LuogoID luogo;

    public UnlockLuogoID() {}

    public UnlockLuogoID(String user, LuogoID luogo) {
        this.user = user;
        this.luogo = luogo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LuogoID getLuogo() {
        return luogo;
    }

    public void setLuogo(LuogoID luogo) {
        this.luogo = luogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnlockLuogoID that = (UnlockLuogoID) o;
        return Objects.equals(user, that.user) && Objects.equals(luogo, that.luogo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, luogo);
    }
}
