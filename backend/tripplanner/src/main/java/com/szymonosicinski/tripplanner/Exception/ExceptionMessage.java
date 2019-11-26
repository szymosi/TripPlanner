package com.szymonosicinski.tripplanner.Exception;

public enum ExceptionMessage {
    USERNAME_TAKEN("Username already exists"),
    PASSWORD_NOT_VALID("Passwod doesn't meet the requirements"),
    PASSWORDS_NOT_MATCH("Passwords are not the same"),
    USER_NOT_EXIST("UserDTO does not exist"),
    RESOURCE_NOT_FOUND("Resource does not exist"),
    ACCES_DENIED("Permision not granted for operation");

    private final String exceptionMessage;

    ExceptionMessage(final String exceptionMessage){
        this.exceptionMessage=exceptionMessage;
    }

    @Override
    public String toString(){
        return exceptionMessage;
    }

}
