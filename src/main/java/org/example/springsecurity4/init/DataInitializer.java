package org.example.springsecurity4.init;

import jakarta.annotation.PostConstruct;
import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.RoleType;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.repository.RoleRepository;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class DataInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public DataInitializer(UserService userService,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    //region pre-init methods
    @PostConstruct
    @Transactional
    public void init() {
        initRoles();
        initUsers();
    }

    public void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            roleRepository.findByName(roleType)
                    .orElseGet(() -> roleRepository.save(new Role(roleType)));
        }
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

    private Role getRole(RoleType roleType) {
        return roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException(roleType + " role not found"));
    }
    //endregion

    private User createUser(String username, String rawPassword, List<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(roles);
        return user;
    }

    private void createAdminUser() {
        User admin = createUser("admin", "admin", List.of(getRole(RoleType.ROLE_ADMIN), getRole(RoleType.ROLE_USER)));
        List<UUID> selectedRoles = List.of(getRole(RoleType.ROLE_USER).getId(), getRole(RoleType.ROLE_ADMIN).getId());
        userService.saveUser(admin, selectedRoles);
    }

    private void createRegularUser() {
        User user = createUser("user", "user", List.of(getRole(RoleType.ROLE_USER)));
        List<UUID> selectedRoles = List.of(getRole(RoleType.ROLE_USER).getId());
        userService.saveUser(user, selectedRoles);
    }
}