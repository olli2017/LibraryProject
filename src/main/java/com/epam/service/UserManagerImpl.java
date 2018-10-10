package com.epam.service;

import com.epam.model.Book;
import com.epam.model.User;
import com.epam.persistence.jdbc.BookRepositoryJdbc;
import com.epam.persistence.jdbc.UserRepositoryJdbc;
import com.epam.service.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserManagerImpl implements UserManager {

    private UserRepositoryJdbc userRepositoryJdbc;
    private BookRepositoryJdbc bookRepositoryJdbc;
    private BCryptPasswordEncoder encoder;
    private User currentUser;

    @Autowired
    public UserManagerImpl(UserRepositoryJdbc userRepositoryJdbc,
                           BookRepositoryJdbc bookRepositoryJdbc,
                           BCryptPasswordEncoder encoder) {
        this.userRepositoryJdbc = userRepositoryJdbc;
        this.bookRepositoryJdbc = bookRepositoryJdbc;
        this.encoder = encoder;
    }

    @Override
    public User registerUser(User user) {
        if ("".equals(user.getEmail())
                || "".equals(user.getPassword())
                || "".equals(user.getName())
                || !user.getEmail().matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
                || user.getPassword().length() < 5
                || !user.getPassword().matches("[a-zA-Z0-9]*")) {
            log.error("Wrong input data");
            throw new WrongInputDataException();
        }
        try {
            userRepositoryJdbc.findByEmail(user.getEmail());
        } catch (org.springframework.dao.EmptyResultDataAccessException ex) {
            user.setPassword(encoder.encode(user.getPassword()));
            currentUser = userRepositoryJdbc.create(user);
            if (currentUser != null) {
                log.info("New User (id = " + currentUser.getId() + ") is registered");
                return currentUser;
            }
            log.error("User is not created");
            throw new UserNotCreatedException();
        }
        throw new UserAlreadyRegisteredException();
    }

    @Override
    public User logInUser(String email, String password) throws WrongInputDataException, NoSuchUserException {
        if ("".equals(email)
                || "".equals(password)
                || !email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
                || password.length() < 5
                || !password.matches("[a-zA-Z0-9]*")) {
            log.error("Wrong input data");
            throw new WrongInputDataException();
        }
        currentUser = userRepositoryJdbc.findByEmail(email);
        if (currentUser == null
                || !encoder.matches(password, currentUser.getPassword())) {
            log.error("There is no such user (id = " + currentUser.getId() + ")");
            throw new NoSuchUserException();
        }
        currentUser.setBookList(getBooks(currentUser));
        if (!currentUser.getName().equals("Librarian")) {
            log.info("User (id = " + currentUser.getId() + ") logged in");
        } else log.info("Librarian logged in");
        return currentUser;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public User findById(int id) {
        return userRepositoryJdbc.findById(id);
    }

    @Override
    public List<Book> getBooks(User user) {
        List<Book> checkedBookList = bookRepositoryJdbc.findByUserId(user.getId());
        if (checkedBookList != null) return checkedBookList;
        log.error("There is no such book");
        throw new NoSuchBookException();
    }
}

