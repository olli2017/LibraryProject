package com.epam.service.exceptions;

public class BookNotRequestedException extends RuntimeException {

    public BookNotRequestedException() {
        super("No request pending");
    }
}
