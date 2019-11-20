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
    public String updateForm(@PathVariable Long id, Model model, HttpSession session) {
        Object tempUser = session.getAttribute("sessionedUser");

        if (tempUser == null) {
            return "redirect:/users/loginForm";
        }

        User sessionedUser = (User) tempUser;

        User user = userRepository.findById(sessionedUser.getId())
                .orElseThrow(() -> new IllegalStateException("자신의 정보만 수정 할 수 있습니다."));

        model.addAttribute("user", user);

        return "/user/updateForm";
    }

    @PutMapping("/{id}")
    public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
        Object tempUser = session.getAttribute("sessionedUser");

        if (tempUser == null) {
            return "redirect:/users/loginForm";
        }

        User sessionedUser = (User) tempUser;

        User user = userRepository.findById(sessionedUser.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "You are not authorized to change another one's"));

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
        session.setAttribute("sessionedUser", user);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("sessionedUser");

        return "redirect:/";
    }

}
