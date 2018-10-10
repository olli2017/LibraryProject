package com.epam.service;

import com.epam.model.Book;
import com.epam.model.User;
import com.epam.service.exceptions.NoSuchBookException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

// TODO: 27.08.2018
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestContext.class, SpringAppConfig.class})
//@WebAppConfiguration
public class BookManagerImplTest {

    @Autowired
    private BookManagerImpl bookManagerImpl;
    private Book book;
    private Book testBook;
    private User user;
    private User librarian;

    @Before
    public void before() {
        book = new Book();
        testBook = new Book();
        book.setId(3);
        book.setAuthor("TestAuthor");
        book.setTitle("TestTitle");
        book.setIsbn("TestIsbn");
        book.setState(Book.State.LIBRARY);
        book.setUserId(0);

        user = new User();
        user.setId(2);
        user.setEmail("TestEmail@test.com");
        user.setName("TestUser");
        user.setPassword("TestPassword");

        librarian = new User();
        librarian.setId(1);
        librarian.setEmail("librarian@test.com");
        librarian.setName("Librarian");
        librarian.setPassword("TestPassword");

        bookManagerImpl = Mockito.mock(BookManagerImpl.class);
    }

    @Test
    public void addBook() {
        when(bookManagerImpl.addBook(book, librarian)).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testUser = (User) args[1];
                if (testUser.getName().equals("Librarian")) {
                    return testBook;
                }
                return null;
            }
        });
        assertEquals(book, bookManagerImpl.addBook(book, librarian));
    }

    @Test
    public void rentBook() {
        when(bookManagerImpl.rentBook(book, user)).then(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testUser = (User) args[1];
                if (testUser.getName().equals("Librarian")) {
                    return null;
                }
                testBook.setUserId(testUser.getId());
                testBook.setState(Book.State.RENT_PENDING);
                return testBook;
            }
        });
        testBook = bookManagerImpl.rentBook(book, user);
        assertEquals(book, testBook);
        assertEquals(Book.State.RENT_PENDING, testBook.getState());
        assertEquals(user.getId(), testBook.getUserId());
    }

    @Test
    public void readBook() {
        when(bookManagerImpl.readBook(book, user)).then(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testUser = (User) args[1];
                if (testUser.getName().equals("Librarian")) {
                    return null;
                }
                testBook.setUserId(testUser.getId());
                testBook.setState(Book.State.READ_ROOM_PENDING);
                return testBook;
            }
        });
        testBook = bookManagerImpl.readBook(book, user);
        assertEquals(book, testBook);
        assertEquals(Book.State.READ_ROOM_PENDING, testBook.getState());
        assertEquals(user.getId(), testBook.getUserId());
    }

    @Test
    public void giveBook() {
        book.setState(Book.State.RENT_PENDING);
        book.setUserId(user.getId());
        when(bookManagerImpl.giveBook(book, librarian)).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testCurrentUser = (User) args[1];
                if (testBook.getState() != Book.State.RENT_PENDING
                        || !testCurrentUser.getName().equals("Librarian")) return null;
                testBook.setState(Book.State.RENTED);
                return testBook;
            }
        });
        testBook = bookManagerImpl.giveBook(book, librarian);
        assertEquals(user.getId(), testBook.getUserId());
        assertEquals(Book.State.RENTED, testBook.getState());
    }

    @Test
    public void giveBookToReadRoom() {
        book.setState(Book.State.READ_ROOM_PENDING);
        book.setUserId(user.getId());
        when(bookManagerImpl.giveBookToReadRoom(book, librarian)).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testCurrentUser = (User) args[1];
                if (testBook.getState() != Book.State.READ_ROOM_PENDING
                        || !testCurrentUser.getName().equals("Librarian")) return null;
                testBook.setState(Book.State.READ_ROOM);
                return testBook;
            }
        });
        testBook = bookManagerImpl.giveBookToReadRoom(book, librarian);
        assertEquals(user.getId(), testBook.getUserId());
        assertEquals(Book.State.READ_ROOM, testBook.getState());
    }

    @Test
    public void denyRequest() {
        book.setState(Book.State.READ_ROOM_PENDING);
        book.setUserId(user.getId());
        when(bookManagerImpl.denyRequest(book, librarian)).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testCurrentUser = (User) args[1];
                if (testBook.getState() != Book.State.READ_ROOM_PENDING
                        && testBook.getState() != Book.State.RENT_PENDING
                        || !testCurrentUser.getName().equals("Librarian")) return null;
                testBook.setState(Book.State.LIBRARY);
                testBook.setUserId(0);
                return testBook;
            }
        });
        testBook = bookManagerImpl.denyRequest(book, librarian);
        System.out.println(testBook);
        assertEquals(0, testBook.getUserId());
        assertEquals(Book.State.LIBRARY, testBook.getState());
    }

    @Test
    public void returnBook() {
        book.setUserId(user.getId());
        book.setState(Book.State.RENTED);
        when(bookManagerImpl.returnBook(book, user)).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                Book testBook = (Book) args[0];
                User testCurrentUser = (User) args[1];
                testBook.setUserId(0);
                testBook.setState(Book.State.LIBRARY);
                return testBook;
            }
        });
        testBook = bookManagerImpl.returnBook(book, user);
        assertEquals(0, book.getUserId());
        assertEquals(Book.State.LIBRARY, book.getState());
    }

    @Test
    public void searchBook() {
        when(bookManagerImpl.searchBook("TestIsbn")).thenAnswer(new Answer<List<Book>>() {
            @Override
            public List<Book> answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                String isbn = (String) args[0];
                List<Book> bookList = new ArrayList<>();
                bookList.add(book);
                if (isbn.equals(book.getIsbn())) return bookList;
                throw new NoSuchBookException();
            }
        });
        assertEquals("TestIsbn", bookManagerImpl.searchBook("TestIsbn").remove(0).getIsbn());
    }

    @Test
    public void searchById() {
        when(bookManagerImpl.searchById(3)).thenAnswer(new Answer<Book>() {
            @Override
            public Book answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                int id = (int) args[0];
                if (id == book.getId()) return book;
                throw new NoSuchBookException();
            }
        });
        assertEquals(3, bookManagerImpl.searchById(3).getId());
    }

    @Test
    public void getAllBooks() {
        when(bookManagerImpl.getAllBooks()).thenAnswer(new Answer<ArrayList<Book>>() {
            @Override
            public ArrayList<Book> answer(InvocationOnMock invocation) {
                ArrayList<Book> list = new ArrayList<>();
                list.add(book);
                return list;
            }
        });
        assertTrue(bookManagerImpl.getAllBooks().remove(0) instanceof Book);
    }
}