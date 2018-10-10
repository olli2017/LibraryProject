package com.epam.service.exceptions;

public class BookIsOccupiedException extends RuntimeException {

    public BookIsOccupiedException() { super("This book is occupied");}
}