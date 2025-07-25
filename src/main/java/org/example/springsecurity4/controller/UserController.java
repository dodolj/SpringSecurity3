package org.example.springsecurity4.controller;

import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.RoleServiceApi;
import org.example.springsecurity4.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleServiceApi roleServiceApi;

    public UserController(UserService userService, RoleServiceApi roleServiceApi) {
        this.userService = userService;
        this.roleServiceApi = roleServiceApi;
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("currentUserEmail", principal.getName());
        model.addAttribute("currentUserRoles", userService.getRolesAsString(principal.getName()));
        model.addAttribute("allRoles", roleServiceApi.findAllRoles());
        return "user";
    }
}