package com.epam.service;

import com.epam.model.Book;
import com.epam.model.User;

import java.util.List;

public interface UserManager {

    User registerUser(User user);

    User logInUser(String email, String password);

    User getCurrentUser();

    User findById(int id);

    List<Book> getBooks(User user);
}
