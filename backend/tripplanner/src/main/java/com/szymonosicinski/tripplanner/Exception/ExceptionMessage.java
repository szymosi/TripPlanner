package com.szymonosicinski.tripplanner.Exception;

public enum ExceptionMessage {
    USERNAME_TAKEN("Username already exists"),
    PASSWORD_NOT_VALID("Passwod doesn't meet the requirements"),
    PASSWORDS_NOT_MATCH("Passwords are not the same");

    private final String exceptionMessage;

    ExceptionMessage(final String exceptionMessage){
        this.exceptionMessage=exceptionMessage;
    }

    @Override
    public String toString(){
        return exceptionMessage;
    }

}
