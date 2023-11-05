package com.bulletinBoard.myBoard.controller;

import com.bulletinBoard.myBoard.domain.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("")
    public String hello(Model model) {
        model.addAttribute("questions", questionRepository.findAll());
//        model.addAttribute("LocalDateTime", LocalDateTime.now());
        return "index";
    }
}
