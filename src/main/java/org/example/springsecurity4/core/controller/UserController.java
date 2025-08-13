package org.example.springsecurity4.core.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class UserController {

    @GetMapping("/users")
    public String userPage(Model model, Authentication authentication) {
        var principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        model.addAttribute("user", principal);
        model.addAttribute("currentUserEmail", principal.getUsername());
        model.addAttribute("currentUserRoles",
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(", ")));
        return "users";
    }
}