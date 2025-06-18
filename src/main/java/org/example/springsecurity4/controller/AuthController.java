package org.example.springsecurity4.controller;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (userService.userExists(user.getUsername())) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "registration";
        }

        try {
            userService.registerUser(user);
            return "redirect:/login?registrationSuccess";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "registration";
        }
    }
}