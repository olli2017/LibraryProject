package com.epam.service;

import com.epam.model.Book;
import com.epam.model.User;
import com.epam.persistence.jdbc.UserRepositoryJdbc;
import com.epam.service.exceptions.NoSuchUserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserManagerImplTest {

    @Autowired
    private UserManagerImpl userManagerImpl;
    @Autowired
    private BookManagerImpl bookManagerImpl;
    @Autowired
    private UserRepositoryJdbc userRepositoryJdbc;
    private Book book;
    private User user;
    private User currentUser;
    private BCryptPasswordEncoder encoder;

    @Before
    public void before() {
        book = new Book();
        book.setId(1);
        book.setAuthor("TestAuthor");
        book.setTitle("TestTitle");
        book.setIsbn("TestIsbn");
        book.setState(Book.State.RENTED);
        book.setUserId(2);

        user = new User();
        user.setId(2);
        user.setEmail("TestEmail@test.com");
        user.setName("TestUser");
        user.setPassword("TestPassword");
        List<Book> bookList = new ArrayList<>();
        bookList.add(book);
        user.setBookList(bookList);

        encoder = new BCryptPasswordEncoder(5);
        userManagerImpl = Mockito.mock(UserManagerImpl.class);
        bookManagerImpl = Mockito.mock(BookManagerImpl.class);
        userRepositoryJdbc = Mockito.mock(UserRepositoryJdbc.class);
    }

    @Test
    public void registerUser() {
        when(userManagerImpl.registerUser(user)).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) {
                user = (User) invocation.getArguments()[0];
                if (user == null) throw new NoSuchUserException();
                user.setPassword(encoder.encode(user.getPassword()));
                return user;
            }
        });
        assertTrue(encoder.matches("TestPassword", userManagerImpl.registerUser(user).getPassword()));
    }

    @Test
    public void logInUser() {
        when(userManagerImpl.logInUser(anyString(), anyString())).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                String email = (String) args[0];
                String password = (String) args[1];
                return userRepositoryJdbc.findByEmail(email);
            }
        });
        assertEquals(userManagerImpl.logInUser("TestEmail@test.com", "TestPassword"), userRepositoryJdbc.findByEmail("TestEmail@test.com"));
    }

    @Test
    public void getCurrentUser() {
        when(userManagerImpl.logInUser("TestEmail@test.com", "TestPassword")).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                currentUser = user;
                return currentUser;
            }
        });
        when(userManagerImpl.getCurrentUser()).thenAnswer(new Answer<User>() {
            @Override
            public User answer(InvocationOnMock invocation) {
                currentUser = userManagerImpl.logInUser("TestEmail@test.com", "TestPassword");
                return currentUser;
            }
        });
        assertEquals(user, userManagerImpl.getCurrentUser());
    }

    @Test
    public void getBooks() {
        when(userManagerImpl.getBooks(user)).then(new Answer<List<Book>>() {
            @Override
            public List<Book> answer(InvocationOnMock invocation) throws Throwable {
                return ((User) invocation.getArguments()[0]).getBookList();
            }
        });
        assertEquals(user.getBookList(), userManagerImpl.getBooks(user));
    }
}