package com.epam.persistence.jdbc;

import com.epam.model.User;
import com.epam.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryJdbc implements UserRepository {
    protected JdbcTemplate jdbcTemplate;
    private RowMapper<User> rowMapper = new UserRowMapper();
    private static final String INSERT_USER_QUERY = "INSERT INTO users (name, password, email) VALUES (?, ?, ?)";
    private static final String UPDATE_USER_QUERY = "UPDATE users (name, password, email) VALUES (?, ?, ?) WHERE id= ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id= ?";
    private static final String GET_ALL_USER_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID = "SELECT id, name, password, email FROM users WHERE id= ?";
    private static final String FIND_BY_EMAIL = "SELECT id, name, password, email FROM users WHERE email= ?";

    @Autowired
    public UserRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        jdbcTemplate.update(INSERT_USER_QUERY, user.getName(), user.getPassword(), user.getEmail());
        return findByEmail(user.getEmail());
    }

    @Override
    public User findById(int userId) {
        return jdbcTemplate.queryForObject(FIND_BY_ID, rowMapper, userId);
    }

    @Override
    public User findByEmail(String email) {
        return jdbcTemplate.queryForObject(FIND_BY_EMAIL, rowMapper, email);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(jdbcTemplate.query(GET_ALL_USER_QUERY, rowMapper));
    }

    @Override
    public User update(User updatedUser) {
        jdbcTemplate.update(UPDATE_USER_QUERY, updatedUser.getId(), updatedUser.getName(), updatedUser.getPassword(), updatedUser.getEmail());
        return findById(updatedUser.getId());
    }

    @Override
    public void delete(User user) {
        jdbcTemplate.update(DELETE_USER_QUERY, user.getId());
    }

    public static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();

            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            String password = rs.getString("password");
            String email = rs.getString("email");
            user.setId(id);
            user.setName(name);
            user.setPassword(password);
            user.setEmail(email);
            return user;
        }
    }
}
