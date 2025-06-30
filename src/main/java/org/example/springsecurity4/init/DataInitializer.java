package org.example.springsecurity4.init;

import jakarta.annotation.PostConstruct;
import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init() {
        initUsers();
    }

    private void initUsers() {
        if (userService.findAll().isEmpty()) {
            if (!userService.userExists("admin")) {
                createAdminUser();
            }
            if (!userService.userExists("user")) {
                createRegularUser();
            }
        }
    }

    private void createAdminUser() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        userService.saveUser(admin);
    }

    private void createRegularUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRole(Role.USER);
        userService.saveUser(user);
    }
}