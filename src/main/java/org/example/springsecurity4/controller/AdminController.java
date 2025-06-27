package org.example.springsecurity4.controller;

import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-list")
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "user-create";
    }

    @PostMapping("/user-create")
    public String createUser(User user) {
        userService.saveUser(user);
        return "redirect:/admin/user-list";
    }

    @PostMapping("/user-delete/{id}")
    public String deleteUser(@PathVariable UUID id) {
        userService.deleteById(id);
        return "redirect:/admin/user-list";
    }

    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") UUID id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "user-update";
    }

    @PutMapping("/user-update")
    public String updateUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin/user-list";
    }
}