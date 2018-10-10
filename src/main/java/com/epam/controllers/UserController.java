package com.epam.controllers;

import com.epam.model.Book;
import com.epam.model.User;
import com.epam.service.BookManagerImpl;
import com.epam.service.UserManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserManagerImpl userService;

    @Autowired
    private BookManagerImpl bookService;
    private User currentUser;

    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView loginUser(@RequestParam String email, @RequestParam String password) {
        currentUser = userService.logInUser(email.toLowerCase(), password);
        ModelAndView modelAndView;
        if (currentUser.getName().equals("Librarian")) {
            modelAndView = new ModelAndView("welcomeLibrarian");
            List<Book> requestedBooks = bookService.getRequestedBooks();
            modelAndView.addObject("requestedBooks", requestedBooks);
            modelAndView.addObject("userService", userService);
        } else {
            modelAndView = new ModelAndView("welcome");
            List<Book> bookList = currentUser.getBookList();
            List<Book> allBooks = bookService.getAllBooks();
            modelAndView.addObject("loggedUser", currentUser);
            modelAndView.addObject("bookList", bookList);
            modelAndView.addObject("allBooks", allBooks);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView loginUser(@RequestParam String search) {
        ModelAndView modelAndView = new ModelAndView("searchPage");
        List<Book> searchedBooks = bookService.searchBookEngine(search);
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("searchedBooks", searchedBooks);
        return modelAndView;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView welcomeGet() {
        ModelAndView modelAndView = new ModelAndView("welcome");
        List<Book> bookList = currentUser.getBookList();
        List<Book> allBooks = bookService.getAllBooks();
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        modelAndView.addObject("allBooks", allBooks);
        return modelAndView;
    }

    @RequestMapping(value = "/page/{i}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView page(@PathVariable Integer i) {
        ModelAndView modelAndView = new ModelAndView("welcome");
        List<Book> bookList = currentUser.getBookList();
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> subList;
        if (i == (allBooks.size() / 10 + 1)) subList = bookService.getAllBooks().subList((i - 1) * 10, allBooks.size());
        else subList = bookService.getAllBooks().subList((i - 1) * 10, (i - 1) * 10 + 10);
        modelAndView.addObject("page", i);
        modelAndView.addObject("subList", subList);
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        modelAndView.addObject("allBooks", allBooks);
        return modelAndView;
    }

    @RequestMapping(value = "/yourPage/{i}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView yourPage(@PathVariable Integer i) {
        ModelAndView modelAndView = new ModelAndView("yourBooks");
        List<Book> bookList = currentUser.getBookList();
        List<Book> subList;
        if (i == (bookList.size() / 10 + 1)) subList = currentUser.getBookList().subList((i - 1) * 10, bookList.size());
        else subList = currentUser.getBookList().subList((i - 1) * 10, (i - 1) * 10 + 10);
        modelAndView.addObject("page", i);
        modelAndView.addObject("subList", subList);
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        return modelAndView;
    }

    @RequestMapping(value = "/YourBooks", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView yourBooks() {
        ModelAndView modelAndView = new ModelAndView("yourBooks");
        List<Book> bookList = currentUser.getBookList();
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        return modelAndView;
    }

    @RequestMapping(value = "/return", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView returnBook(@RequestParam int returnId) {
        bookService.returnBook(bookService.searchById(returnId), currentUser);
        return getYourBooksModelAndView();
    }

    @RequestMapping(value = "/rent", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView rentBook(@RequestParam int rentId) {
        bookService.rentBook(bookService.searchById(rentId), currentUser);
        return getAllBooksModelAndView();
    }

    @RequestMapping(value = "/read", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readBook(@RequestParam int readId) {
        bookService.readBook(bookService.searchById(readId), currentUser);
        return getAllBooksModelAndView();
    }

    @RequestMapping(value = "/rent/{page}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView rentPage(@RequestParam int rentId, @PathVariable Integer page) {
        bookService.rentBook(bookService.searchById(rentId), currentUser);
        ModelAndView modelAndView = new ModelAndView("welcome");
        List<Book> bookList = currentUser.getBookList();
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> subList;
        if (page == (allBooks.size() / 10 + 1))
            subList = bookService.getAllBooks().subList((page - 1) * 10, allBooks.size());
        else subList = bookService.getAllBooks().subList((page - 1) * 10, (page - 1) * 10 + 10);
        modelAndView.addObject("page", page);
        modelAndView.addObject("subList", subList);
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        modelAndView.addObject("allBooks", allBooks);
        return modelAndView;
    }

    @RequestMapping(value = "/return/{page}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView returnPage(@RequestParam int returnId, @PathVariable Integer page) {
        bookService.returnBook(bookService.searchById(returnId), currentUser);
        ModelAndView modelAndView = new ModelAndView("yourBooks");
        List<Book> bookList = currentUser.getBookList();
        List<Book> subList;
        if (page == (bookList.size() / 10 + 1))
            subList = currentUser.getBookList().subList((page - 1) * 10, bookList.size());
        else subList = currentUser.getBookList().subList((page - 1) * 10, (page - 1) * 10 + 10);
        modelAndView.addObject("page", page);
        modelAndView.addObject("subList", subList);
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        return modelAndView;
    }

    @RequestMapping(value = "/read/{page}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView readPage(@RequestParam int readId, @PathVariable Integer page) {
        bookService.readBook(bookService.searchById(readId), currentUser);
        ModelAndView modelAndView = new ModelAndView("welcome");
        List<Book> bookList = currentUser.getBookList();
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> subList;
        if (page == (allBooks.size() / 10 + 1))
            subList = bookService.getAllBooks().subList((page - 1) * 10, allBooks.size());
        else subList = bookService.getAllBooks().subList((page - 1) * 10, (page - 1) * 10 + 10);
        modelAndView.addObject("page", page);
        modelAndView.addObject("subList", subList);
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        modelAndView.addObject("allBooks", allBooks);
        return modelAndView;
    }

    @RequestMapping(value = "/grant", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView grantBook(@RequestParam int bookId) {
        if (bookService.searchById(bookId).getState().ordinal() == 4)
            bookService.giveBookToReadRoom(bookService.searchById(bookId), currentUser);
        else bookService.giveBook(bookService.searchById(bookId), currentUser);
        return getRequestedBooks();
    }

    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView refuseBook(@RequestParam int bookId) {
        bookService.denyRequest(bookService.searchById(bookId), currentUser);
        return getRequestedBooks();
    }

    @RequestMapping(value = "/users/getBooks", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getUsersBooks() {
        return userService.getBooks(userService.getCurrentUser());
    }

    @RequestMapping(value = "/users/getRentedBooks", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getRentBooks() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("usersBookList", userService.getBooks(userService.getCurrentUser()));
        modelAndView.setViewName("rentedBooks");
        return modelAndView;
    }

    @GetMapping(value = "/logout")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView logOut() {
        return new ModelAndView("login");
    }

    private ModelAndView getAllBooksModelAndView() {
        ModelAndView modelAndView = new ModelAndView("welcome");
        List<Book> bookList = currentUser.getBookList();
        List<Book> allBooks = bookService.getAllBooks();
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        modelAndView.addObject("allBooks", allBooks);
        return modelAndView;
    }

    private ModelAndView getYourBooksModelAndView() {
        ModelAndView modelAndView = new ModelAndView("yourBooks");
        List<Book> bookList = currentUser.getBookList();
        modelAndView.addObject("loggedUser", currentUser);
        modelAndView.addObject("bookList", bookList);
        return modelAndView;
    }

    private ModelAndView getRequestedBooks() {
        ModelAndView modelAndView = new ModelAndView("welcomeLibrarian");
        List<Book> requestedBooks = bookService.getRequestedBooks();
        modelAndView.addObject("userService", userService);
        modelAndView.addObject("requestedBooks", requestedBooks);
        return modelAndView;
    }
}