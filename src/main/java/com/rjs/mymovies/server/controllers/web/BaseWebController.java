package com.rjs.mymovies.server.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BaseWebController {
    @GetMapping("/home")
    public String getHome() {
        return "home";
    }

    @PostMapping("/home")
    public String postHome() {
        return "home";
    }
}
