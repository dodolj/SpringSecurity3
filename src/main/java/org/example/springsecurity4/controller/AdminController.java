package org.example.springsecurity4.controller;

import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.RoleServiceApi;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; //outdated
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
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUserEmail", authentication.getName());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        String roles = authorities.stream()
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