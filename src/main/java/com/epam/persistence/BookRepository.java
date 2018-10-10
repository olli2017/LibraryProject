package com.epam.persistence;

import com.epam.model.Book;

import java.util.List;

public interface BookRepository {
    Book create(Book newBookInstance);

    List<Book> findByIsbn(String isbn);

    Book findById(int id);

    Book update(Book updatedBook);

    void delete(Book book);

    List<Book> findByUserId(int userId);

    List<Book> findRequestedBooks();

    List<Book> getAll();

    List<Book> findByAuthor(String author);

    List<Book> findByTitle(String title);
}
