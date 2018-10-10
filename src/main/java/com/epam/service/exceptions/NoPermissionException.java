package com.epam.service.exceptions;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException() {
        super("Permission denied");
    }
}
