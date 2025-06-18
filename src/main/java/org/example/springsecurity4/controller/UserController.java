package org.example.springsecurity4.controller;

import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user-create")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user-delete/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") UUID id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user-update";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/user-update")
    public String updateUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }
}