package com.epam.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class User {

    private int id;
    private String password;
    private String name;
    private String email;
    private List<Book> bookList;

    public User(String name, String password, String email, List<Book> bookList) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.bookList = bookList;
    }
}