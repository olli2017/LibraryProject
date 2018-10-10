package com.epam.service.exceptions;

public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException() {
        super("There is no such user");
    }
}
