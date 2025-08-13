package org.example.springsecurity4.core.controller;

import org.example.springsecurity4.domain.model.User;
import org.example.springsecurity4.domain.service.RoleServiceApi;
import org.example.springsecurity4.domain.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleServiceApi roleService;

    public AdminController(UserService userService,
                           PasswordEncoder passwordEncoder,
                           RoleServiceApi roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUserName", user.getUsername());

        String roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

        model.addAttribute("currentUserRoles", roles);
        model.addAttribute("allRoles", roleService.findAllRoles());
        return "admin";
    }


    @PostMapping("/admin/edit")
    public String editUser(
            @ModelAttribute User user,
            @RequestParam(required = false) List<String> selectedRoles) {
        userService.updateUser(user, selectedRoles);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@RequestParam UUID id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/create")
    public String createUser(
            @ModelAttribute User user,
            @RequestParam(required = false) List<UUID> selectedRoles) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user, selectedRoles);
        return "redirect:/admin";
    }
}