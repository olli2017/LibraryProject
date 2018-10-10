package com.epam.service;

import com.epam.model.Book;
import com.epam.model.User;
import com.epam.persistence.jdbc.BookRepositoryJdbc;
import com.epam.service.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookManagerImpl implements BookManager {

    private BookRepositoryJdbc bookRepositoryJdbc;

    @Autowired
    public BookManagerImpl(BookRepositoryJdbc bookRepositoryJdbc) {
        this.bookRepositoryJdbc = bookRepositoryJdbc;
    }

    @Override
    public Book addBook(Book book, User currentUser) {
        if (!currentUser.getName().equals("Librarian")) {
            log.error("Permission for User (id = " + currentUser.getId() + ") is denied");
            throw new NoPermissionException();
        }
        Book testBook = bookRepositoryJdbc.create(book);
        if (testBook != null) {
            log.info("The book (id = " + book.getId() + ") has been added to the Library List");
            return testBook;
        }
        log.error("The Book (id = " + book.getId() + ") is not created");
        throw new BookNotCreatedException();
    }

    @Override
    public Book rentBook(Book book, User user) {
        if (user.getName().equals("Librarian")) {
            log.error("Permission for User (id = " + user.getId() + ") is denied");
            throw new NoPermissionException();
        }
        if (book.getState() == Book.State.LIBRARY) {
            Book testBook;
            book.setUserId(user.getId());
            book.setState(Book.State.RENT_PENDING);
            testBook = bookRepositoryJdbc.update(book);
            if (testBook != null) {
                addBookToList(testBook, user);
                log.info("The requested book (id =" + book.getId() + ")is now pending before rent");
                return testBook;
            }
            log.error("There is no such book (id =" + book.getId() + ")");
            throw new NoSuchBookException();
        }
        log.error("This book is occupied (id =" + book.getId() + ")");
        throw new BookIsOccupiedException();
    }

    @Override
    public Book readBook(Book book, User user) {
        if (user.getName().equals("Librarian")) {
            log.error("Permission for User (id = " + user.getId() + ") is denied");
            throw new NoPermissionException();
        }
        if (book.getState() == Book.State.LIBRARY) {
            Book testBook;
            book.setUserId(user.getId());
            book.setState(Book.State.READ_ROOM_PENDING);
            testBook = bookRepositoryJdbc.update(book);
            if (testBook != null) {
                addBookToList(testBook, user);
                log.info("The requested book (id =" + book.getId() + ")is now pending before read");
                return testBook;
            }
            log.error("There is no such book (id =" + book.getId() + ")");
            throw new NoSuchBookException();
        }
        log.error("This book is occupied (id =" + book.getId() + ")");
        throw new BookIsOccupiedException();
    }

    @Override
    public Book giveBook(Book book, User currentUser) {
        if (!currentUser.getName().equals("Librarian")) {
            log.error("Permission for User (id = " + currentUser.getId() + ") is denied");
            throw new NoPermissionException();
        }
        if (book.getState() == Book.State.RENT_PENDING
                || book.getState() == Book.State.READ_ROOM_PENDING) {
            Book testBook;
            book.setState(Book.State.RENTED);
            testBook = bookRepositoryJdbc.update(book);
            if (testBook != null) {
                removeBookFromList(testBook, currentUser);
                log.info("The book (id = " + book.getId() + ") has been given to User (id = " + currentUser.getId() + ")");
                return testBook;
            }
            log.error("There is no such book (id =" + book.getId() + ")");
            throw new NoSuchBookException();
        }
        log.error("This book is occupied (id =" + book.getId() + ")");
        throw new BookIsOccupiedException();
    }

    @Override
    public Book denyRequest(Book book, User currentUser) {
        if (!currentUser.getName().equals("Librarian")) {
            log.error("Permission denied to User (id = " + currentUser.getId() + ")");
            throw new NoPermissionException();
        }
        if (book.getState() == Book.State.RENT_PENDING
                || book.getState() == Book.State.READ_ROOM_PENDING) {
            Book testBook;
            book.setUserId(0);
            book.setState(Book.State.LIBRARY);
            testBook = bookRepositoryJdbc.update(book);
            if (testBook == null) {
                log.error("There is no such book (id =" + book.getId() + ")");
                throw new NoSuchBookException();
            }
            removeBookFromList(testBook, currentUser);
            log.info("Book request denied (id =" + book.getId() + ")");
            return testBook;
        }
        log.error("No request pending for book (id =" + book.getId() + ")");
        throw new BookNotRequestedException();
    }

    @Override
    public Book returnBook(Book book, User currentUser) {
        List<Book> bookList = currentUser.getBookList();
        if (!bookList.contains(book)) {
            log.error("The book (id = " + book.getId() + ") can't be returned by this User (id = " + currentUser.getId() + ")");
            throw new NotYourBookException();
        }
        Book testBook;
        book.setUserId(0);
        book.setState(Book.State.LIBRARY);
        testBook = bookRepositoryJdbc.update(book);
        if (testBook != null) {
            removeBookFromList(testBook, currentUser);
            log.info("The book (id = " + book.getId() + ") has been returned to the library");
            return testBook;
        }
        log.error("The book (id = " + book.getId() +") is not created");
        throw new BookNotCreatedException();
    }

    @Override
    public Book giveBookToReadRoom(Book book, User currentUser) {
        if (!currentUser.getName().equals("Librarian")) {
            log.error("Permission for this User (id = " + currentUser.getId() + ") is denied");
            throw new NoPermissionException();
        }
        if (book.getState() == Book.State.READ_ROOM_PENDING) {
            Book testBook;
            book.setState(Book.State.READ_ROOM);
            testBook = bookRepositoryJdbc.update(book);
            if (testBook != null) {
                log.info("The book (id= " + book.getId() + " has been given to User (id = " + currentUser.getId() + ") in the ReadRoom");
                return testBook;
            }
            log.error("There is no such book (id =" + book.getId() + ")");
            throw new NoSuchBookException();
        }
        log.error("The book (id = " + book.getId() +") is not requested");
        throw new BookNotRequestedException();
    }

    @Override
    public List<Book> searchBook(String isbn) {
        List<Book> bookList = bookRepositoryJdbc.findByIsbn(isbn);
        if (bookList.isEmpty()) {
            log.error("There is no such book with requested ISBN = " + isbn);
            throw new NoSuchBookException();
        }
        return bookList;
    }

    @Override
    public Book searchById(int id) {
        if (bookRepositoryJdbc.findById(id) != null) return bookRepositoryJdbc.findById(id);
        log.error("There is no such book with requested ISBN = " + id);
        throw new NoSuchBookException();
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> testList = bookRepositoryJdbc.getAll();
        if (testList != null) return testList;
        log.error("There is no such book");
        throw new NoSuchBookException();
    }

    @Override
    public void addBookToList(Book testBook, User user) {
            List<Book> updatedBookList = new ArrayList<>(user.getBookList());
            updatedBookList.add(testBook);
            user.setBookList(updatedBookList);
            log.info("The requested book (id = " + testBook.getId() + ") is added to User's (id = " + user.getId() + ") List");
    }

    @Override
    public void removeBookFromList(Book testBook, User currentUser) {
            List<Book> updatedBookList = new ArrayList<>(currentUser.getBookList());
            Book deletedBook = null;
            for (Book book1 : updatedBookList) {
                if (book1.getId() == testBook.getId())
                    deletedBook = book1;
            }
            if (deletedBook != null)
                updatedBookList.remove(deletedBook);

            currentUser.setBookList(updatedBookList);
            log.info("The book (id = " + testBook.getId() + ") order has been removed");
    }

    public List<Book> getRequestedBooks() {
        List<Book> testList = bookRepositoryJdbc.findRequestedBooks();
        if (testList != null) return testList;
        log.error("There is no such book");
        throw new NoSuchBookException();
    }

    @Override
    public List<Book> searchBookEngine(String field) {
        field = field.trim();
        List<Book> resultList = new ArrayList<>();
        if (bookRepositoryJdbc.findByIsbn(field) != null) {
            List<Book> list = bookRepositoryJdbc.findByIsbn(field);
            for (Book book : list) {
                String result = book.getIsbn();
                if (result.equals(field)) {
                    resultList.add(book);
                }
            }
        }
        if (bookRepositoryJdbc.findByAuthor(field) != null) {
            List<Book> list = bookRepositoryJdbc.findByAuthor(field);
            for (Book book : list) {
                String result = book.getAuthor();
                if (result.equals(field)) {
                    resultList.add(book);
                }
            }
        }
        if (bookRepositoryJdbc.findByTitle(field) != null) {
            List<Book> list = bookRepositoryJdbc.findByTitle(field);
            for (Book book : list) {
                String result = book.getTitle();
                if (result.equals(field)) {
                    resultList.add(book);
                }
            }
        }
        return resultList;
    }
}