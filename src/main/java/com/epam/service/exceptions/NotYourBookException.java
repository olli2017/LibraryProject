package com.epam.service.exceptions;

public class NotYourBookException extends RuntimeException {

    public NotYourBookException() {
        super("You can not return the book you do not own");
    }
}
