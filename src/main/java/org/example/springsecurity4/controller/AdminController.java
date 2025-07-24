package org.example.springsecurity4.controller;

import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.RoleService;
import org.example.springsecurity4.service.RoleServiceApi;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
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

    //region outdated
//    @GetMapping("/user-list")
//    public String findAll(Model model) {
//        model.addAttribute("users", userService.findAll());
//        return "user-list";
//    }
//
//    @GetMapping("/user-create")
//    public String createUserForm(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("roles", RoleType.values());
//        return "user-create";
//    }
//
//    @PostMapping("/user-create")
//    public String createUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userService.saveUser(user);
//        return "redirect:/admin/user-list";
//    }
//
//    @PostMapping("/user-delete/{id}")
//    public String deleteUser(@PathVariable UUID id) {
//        userService.deleteById(id);
//        return "redirect:/admin/user-list";
//    }
//
//    @GetMapping("/user-update/{id}")
//    public String updateUserForm(@PathVariable("id") UUID id, Model model) {
//        User user = userService.findById(id);
//        List<Role> allRoles = roleService.findAllRoles();
//
//        model.addAttribute("user", user);
//        model.addAttribute("roles", allRoles);
//        return "user-update";
//    }
//
//    @PutMapping("/user-update")
//    public String updateUser(@ModelAttribute User user) {
//        userService.updateUser(user);
//        return "redirect:/admin/user-list";
//    }
    //endregion

    @GetMapping("/admin")
    public String showAdminPanel(Model model, Principal principal) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentUserEmail", principal.getName());
        model.addAttribute("currentUserRoles", userService.getRolesAsString(principal.getName()));
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

}