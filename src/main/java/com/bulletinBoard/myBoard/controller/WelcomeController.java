package com.bulletinBoard.myBoard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WelcomeController {

    @GetMapping("")
    public String hello(String name, Model model) {

        return "index";
    }
}
