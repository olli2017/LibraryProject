package com.epam.service;

import com.epam.model.Book;
import com.epam.model.User;

import java.util.List;

public interface BookManager {

    Book addBook(Book book, User currentUser);

//    Book rentBook(Book book, User user, User currentUser);

    //    public Book rentBook(Book book, User user, User currentUser) {
//        if (currentUser.getName().equals("Librarian")) {
//    Book rentBook(Book book, User user);

    //    public Book rentBook(Book book, User user, User currentUser) {
//        if (currentUser.getName().equals("Librarian")) {
    Book rentBook(Book book, User user);

//    Book readBook(Book book, User user, User currentUser);

    //    public Book readBook(Book book, User user, User currentUser) {
//        if (currentUser.getName().equals("Librarian")) {
    Book readBook(Book book, User user);

    Book giveBook(Book book, User currentUser);

    Book denyRequest(Book book, User currentUser);

    Book returnBook(Book book, User currentUser);

    Book giveBookToReadRoom(Book book, User currentUser);

    List<Book> searchBook(String query);

    Book searchById(int id);

    List<Book> getAllBooks();

    void addBookToList(Book book, User user);

    void removeBookFromList(Book book, User user);

    List<Book> searchBookEngine(String field);
}