package com.bulletinBoard.myBoard.controller;

import com.bulletinBoard.myBoard.domain.Question;
import com.bulletinBoard.myBoard.domain.QuestionRepository;
import com.bulletinBoard.myBoard.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.bulletinBoard.myBoard.service.HttpSessionUtils.getUserFromSession;
import static com.bulletinBoard.myBoard.service.HttpSessionUtils.isLoginUser;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/form")
    public String questionForm(HttpSession session) {

        System.out.println("++++++++++");
        if (!isLoginUser(session)) {
            System.out.println("---------");
            return "redirect:/users/loginForm";
        }

        return "/qna/questionForm";
    }

    @PostMapping("")
    public String create(String title, String contents, HttpSession session) {
        if (!isLoginUser(session)) {
            return "/user/loginForm";
        }

        User sessionUser = getUserFromSession(session);
        Question question = Question.builder()
                .writer(sessionUser)
                .title(title)
                .contents(contents)
                .build();

        questionRepository.save(question);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).get());

        return "/qna/show";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model) {
        model.addAttribute("question", questionRepository.findById(id).get());

        return "/qna/contentUpdateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, String title, String contents) {
        Question question = questionRepository.findById(id).get();
        question.update(title, contents);
        questionRepository.save(question);

        return String.format("redirect:/questions/%d", id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        questionRepository.deleteById(id);

        return "redirect:/";
    }
}
