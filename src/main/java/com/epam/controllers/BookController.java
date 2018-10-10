package com.epam.controllers;

import com.epam.model.Book;
import com.epam.service.BookManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {

    @Autowired
    private BookManagerImpl bookService;

    /*
    @RequestMapping(value = "/books", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }
*/
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getAllBook() {
        ModelAndView modelAndView = new ModelAndView("libraryBooks");
        modelAndView.addObject("list", bookService.getAllBooks());
        return modelAndView;
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(@PathVariable int id) {
        return bookService.searchById(id);
    }

//    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteBookById(@PathVariable int id) {
//        throw new UnsupportedOperationException();
//    }
}