package com.epam.service.exceptions;

public class BookNotCreatedException extends RuntimeException {

    public BookNotCreatedException() {
        super("Book not created");
    }
}
