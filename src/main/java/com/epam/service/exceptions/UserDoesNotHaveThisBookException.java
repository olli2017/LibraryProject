package com.epam.service.exceptions;

public class UserDoesNotHaveThisBookException extends RuntimeException {

    public UserDoesNotHaveThisBookException() {
        super("This user doesn't have this book");
    }
}

