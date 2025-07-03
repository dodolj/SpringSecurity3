package org.example.springsecurity4.controller;

import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.RoleType;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.RoleService;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public AdminController(UserService userService,
                           PasswordEncoder passwordEncoder,
                           RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("/user-list")
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user-list";
    }

    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", RoleType.values());
        return "user-create";
    }

    @PostMapping("/user-create")
    public String createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        List<Role> allRoles = roleService.findAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", allRoles);
        return "user-update";
    }

    @PutMapping("/user-update")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin/user-list";
    }
}