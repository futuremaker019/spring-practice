package com.bulletinBoard.myBoard.controller;

import com.bulletinBoard.myBoard.domain.User;
import com.bulletinBoard.myBoard.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/form")
    public String form() {
        return "/user/form";
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
    public String updateForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);

        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, Model model) {
        User user = userRepository.findById(id).orElse(null);
        user.userUpdate(updatedUser);
        userRepository.save(user);

        return "redirect:/users";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
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
        session.setAttribute("user", user);

        return "redirect:/";
    }

}
