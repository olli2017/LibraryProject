package com.epam.controllers;

import com.epam.model.User;
import com.epam.service.UserManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {
    @Autowired
    private UserManagerImpl userService;

    @RequestMapping(value = "/users/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ModelAndView registerUser(@ModelAttribute User user, @RequestParam String confirmPassword, @RequestParam String password) {
        if (!confirmPassword.equals(password))
            return new ModelAndView("register").addObject("tryAgain", new Boolean(true));
        user.setEmail(user.getEmail().toLowerCase());
        userService.registerUser(user);
        return new ModelAndView("login");
    }
}
