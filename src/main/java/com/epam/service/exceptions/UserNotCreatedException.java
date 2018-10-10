package com.epam.service.exceptions;

public class UserNotCreatedException extends RuntimeException {

    public UserNotCreatedException() {
        super("User not created");
    }
}
