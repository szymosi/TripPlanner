package com.szymonosicinski.tripplanner.Exception;

public enum ExceptionMessage {
    USERNAME_TAKEN("Username already exists"),
    PASSWORD_NOT_VALID("Passwod doesn't meet the requirements"),
    PASSWORDS_NOT_MATCH("Passwords are not the same"),
    USER_NOT_EXIST("UserDTO does not exist"),
    USER_NOT_LOGGED_IN("User is not logged in"),
    RESOURCE_NOT_FOUND("Resource does not exist"),
    ACCESS_DENIED("Permision not granted for operation"),
    INPUT_ERROR("Wrong input data"),
    COST_ROOT("Cannot remove root expense"),
    NOT_PARTICIPANT("User is not participant"),
    IS_PARTICIPANT("User already is participant"),
    ORGANIZER_CANNOT_BE_PARTICIPANT("Organizer cannot be added as participant");

    private final String exceptionMessage;

    ExceptionMessage(final String exceptionMessage){
        this.exceptionMessage=exceptionMessage;
    }

    @Override
    public String toString(){
        return exceptionMessage;
    }

}
