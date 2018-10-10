package com.epam.persistence;

import com.epam.model.User;

import java.util.List;

public interface UserRepository {

    User create(User user);

    User findById(int userId);

    User findByEmail(String email);

    List<User> getAll();

    User update(User updatedUser);

    void delete(User user);
}
