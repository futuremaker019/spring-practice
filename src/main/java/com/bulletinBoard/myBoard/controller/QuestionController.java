package com.bulletinBoard.myBoard.controller;

import com.bulletinBoard.myBoard.domain.Question;
import com.bulletinBoard.myBoard.domain.QuestionRepository;
import com.bulletinBoard.myBoard.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import java.util.Properties;

import static com.bulletinBoard.myBoard.service.HttpSessionUtils.*;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/form")
    public String form(HttpSession session) {
        if (!isLoginUser(session)) {
            return "/users/loginForm";
        }

        return "/qna/questionForm";
    }

    @PostMapping("")
    public String create(String title, String contents, HttpSession session) {
        if (!isLoginUser(session)){
            return "/user/loginForm";
        }

        User sessionUser = getUserFromSession(session);
        Question question = Question.builder()
                .writer(sessionUser.getUserId())
                .title(title)
                .contents(contents)
                .build();

        questionRepository.save(question);

        return "redirect:/";
    }
}
