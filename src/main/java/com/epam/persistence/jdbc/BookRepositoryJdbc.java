package com.epam.persistence.jdbc;


import com.epam.model.Book;
import com.epam.persistence.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryJdbc implements BookRepository {
    protected JdbcTemplate jdbcTemplate;
    private RowMapper<Book> rowMapper = new BookRowMapper();
    private static final String INSERT_BOOK_QUERY = "INSERT INTO books( isbn, title, author, state, user_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_BOOK_QUERY = "UPDATE books SET isbn = ?, title = ?, author = ?, state = ?, user_id = ? WHERE id= ?";
    private static final String DELETE_BOOK_QUERY = "DELETE FROM books WHERE id= ?";
    private static final String GET_ALL_BOOK_QUERY = "SELECT * FROM books";
    private static final String FIND_BY_ISBN = "SELECT id, isbn, title, author, state, user_id FROM books WHERE isbn= ?";
    private static final String FIND_BY_USERID = "SELECT * FROM books WHERE user_id= ?";
    private static final String FIND_REQUESTED_BOOKS = "SELECT * FROM books WHERE state = 3 OR state = 4";
    private static final String FIND_BY_AUTHOR = "SELECT id, isbn, title, author, state, user_id FROM books WHERE author= ?";
    private static final String FIND_BY_TITLE = "SELECT id, isbn, title, author, state, user_id FROM books WHERE title= ?";

    @Autowired
    public BookRepositoryJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book create(Book book) {
        jdbcTemplate.update(INSERT_BOOK_QUERY, book.getIsbn(), book.getTitle(), book.getAuthor(), book.getState().ordinal(), book.getUserId());
        return findById(book.getId());
    }

    @Override
    public List<Book> findByIsbn(String isbn) {
        return new ArrayList<>(jdbcTemplate.query(FIND_BY_ISBN, rowMapper, isbn));
    }

    @Override
    public Book findById(int id) {
        String sql = "SELECT id, isbn, title, author, state, user_id FROM books WHERE id= ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(jdbcTemplate.query(GET_ALL_BOOK_QUERY, rowMapper));
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return new ArrayList<>(jdbcTemplate.query(FIND_BY_AUTHOR, rowMapper, author));
    }

    @Override
    public List<Book> findByTitle(String title) {
        return new ArrayList<>(jdbcTemplate.query(FIND_BY_TITLE, rowMapper, title));
    }

    @Override
    public Book update(Book updatedBook) {
        jdbcTemplate.update(UPDATE_BOOK_QUERY, updatedBook.getIsbn(), updatedBook.getTitle(), updatedBook.getAuthor(), updatedBook.getState().ordinal(), updatedBook.getUserId(), updatedBook.getId());
        return findById(updatedBook.getId());
    }

    @Override
    public void delete(Book book) {
        jdbcTemplate.update(DELETE_BOOK_QUERY, book.getId());
    }

    @Override
    public List<Book> findByUserId(int userId) {
        return new ArrayList<>(jdbcTemplate.query(FIND_BY_USERID, rowMapper, userId));
    }

    @Override
    public List<Book> findRequestedBooks() {
        return jdbcTemplate.query(FIND_REQUESTED_BOOKS, rowMapper);
    }

    //This class performs mapping rows of a ResultSet to a result object row by row
    public static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();

            //all columns from jdbc table BOOK
            Integer id = rs.getInt("id");
            String isbn = rs.getString("isbn");
            String title = rs.getString("title");
            String author = rs.getString("author");
            Integer state = rs.getInt("state");
            Integer userId = rs.getInt("user_id");
            //setters for all filds of Book
            book.setId(id);
            book.setIsbn(isbn);
            book.setTitle(title);
            book.setAuthor(author);
            book.setState(Book.State.values()[state]);
            book.setUserId(userId);
            return book;
        }
    }
}
