package com.epam.service.exceptions;

public class NoSuchBookException extends RuntimeException {

    public NoSuchBookException() {
        super("There is no such book");
    }
}
