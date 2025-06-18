package org.example.springsecurity4.init;

import lombok.RequiredArgsConstructor;
import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.repository.UserRepository;
import org.example.springsecurity4.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .build();
        userService.saveUser(admin);

        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .role(Role.USER)
                .build();
        userService.saveUser(user);
    }
}