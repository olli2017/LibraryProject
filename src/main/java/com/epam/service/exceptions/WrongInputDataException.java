package com.epam.service.exceptions;

public class WrongInputDataException extends RuntimeException {

    public WrongInputDataException() {
        super("Wrong input data");
    }
}
