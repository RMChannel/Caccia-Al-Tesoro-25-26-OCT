package com.oct.caccia_al_tesorov2.Model.Users;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException() {
        super("Questo username risulta già registrato");
    }
}
