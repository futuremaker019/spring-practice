package com.bulletinBoard.myBoard.controller;

import com.bulletinBoard.myBoard.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

import static com.bulletinBoard.myBoard.service.HttpSessionUtils.getUserFromSession;
import static com.bulletinBoard.myBoard.service.HttpSessionUtils.isLoginUser;

@Controller
@RequestMapping("/questions/{questionId}/answer")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("")
    public String create(@PathVariable Long questionId, String contents, HttpSession session) {
        if(!isLoginUser(session)) {
            return "redirect:/users/loginForm";
        }

        User loginUser = getUserFromSession(session);

        Question question = questionRepository.findById(questionId).get();
        Answer answer = Answer.builder()
                .writer(loginUser)
                .contents(contents)
                .question(question)
                .build();

        answerRepository.save(answer);

        return String.format("redirect:/questions/%d", questionId);
    }
}
