package com.classroom.user.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {


    /**
     * logger for user controller.
     */
    private final Log log = LogFactory.getLog(UserController.class);

    @GetMapping(value = {"/", "/welcome"})
    public String welcome(final Model model) {
        log.info("received incoming traffic and redirected to welcome");
        return "welcome";
    }

    @GetMapping(value = "/login")
    public String login(final Model model, final String error, final String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping(value = "/user/preferences")
    public String preference(final Model model) {
        log.info("received incoming traffic and redirected to preferences");
        return "user/preferences";
    }
}
