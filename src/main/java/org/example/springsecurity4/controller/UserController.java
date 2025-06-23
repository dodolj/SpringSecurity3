package org.example.springsecurity4.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    @GetMapping("/user-current-user")
    public String userPage() {
        return "user-current-user";
    }
}