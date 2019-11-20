package com.bulletinBoard.myBoard.controller;

import com.bulletinBoard.myBoard.domain.User;
import com.bulletinBoard.myBoard.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.bulletinBoard.myBoard.service.HttpSessionUtils.getUserFromSession;
import static com.bulletinBoard.myBoard.service.HttpSessionUtils.isLoginUser;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String signInForm() {
        return "/user/signInForm";
    }

    @PostMapping("")
    public String create(User user) {
        System.out.println("user = " + user);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());

        return "/user/list";
    }

    @GetMapping("/{id}/form")
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!isLoginUser(session)) {
            return "redirect:/users/loginForm";
        }

        User loggedInUser = getUserFromSession(session);

        User user = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "You are not authorized to change other's information"));

        model.addAttribute("user", user);

        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
        if (!isLoginUser(session)) {
            return "redirect:/users/loginForm";
        }

        User loggedInUser = getUserFromSession(session);

        User user = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "You are not authorized to change other's information"));

        user.userUpdate(updatedUser);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/loginForm")
    public String loginForm(HttpSession session) {

        return "/user/login";
    }

    @PostMapping("/login")
    public String login(String userId, String password, HttpSession session) {
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            System.out.println("Login Failure!");
            return "redirect:/users/loginForm";
        }

        if (!password.equals(user.getPassword())) {
            System.out.println("Login Failure!");
            return "redirect:/users/loginForm";
        }

        System.out.println("Login Success");
        session.setAttribute("sessionedUser", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("sessionedUser");

        return "redirect:/";
    }

}
