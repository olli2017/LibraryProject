package com.epam.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Book {

    private int id;
    private String isbn;
    private String title;
    private String author;
    private int userId;
    private State state;

    public Book(String isbn, String title, String author, int userId, State state) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.userId = userId;
        this.state = state;
    }

    public enum State {
        LIBRARY, READ_ROOM, RENTED, RENT_PENDING, READ_ROOM_PENDING;
    }
}