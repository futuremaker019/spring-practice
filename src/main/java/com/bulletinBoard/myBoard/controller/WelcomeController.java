package com.bulletinBoard.myBoard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WelcomeController {

    @GetMapping("/hello")
    public String hello(String name, Model model) {
        System.out.println("name : " + name);
        model.addAttribute("name", name);
        return "welcome";
    }

}
