package org.example.springsecurity4.core.exception;

public class UserNameAlreadyExistException extends RuntimeException {

    private final String userName;

    public UserNameAlreadyExistException(String username) {
        this.userName = username;
    }

    @Override
    public String getMessage() {
        return "Username already exists: "+ userName;
    }

    public String getUserName() {
        return userName;
    }
}
