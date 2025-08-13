package org.example.springsecurity4.domain.service;

import org.example.springsecurity4.domain.model.Role;
import org.example.springsecurity4.domain.model.RoleType;
import org.example.springsecurity4.domain.model.User;
import org.example.springsecurity4.domain.repository.RoleRepository;
import org.example.springsecurity4.domain.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserService implements UserServiceApi {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    //region readOnly
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean userExists(UUID id) {
        return userRepository.findById(id).isPresent();
    }

    @Override
    public String getRolesAsString(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "";
        }
        User user = userOptional.get();
        return user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(", "));

    }
    //endregion

    //region @Transactional
    @Transactional
    @Override
    public void saveUser(User user, List<UUID> selectedRolesIds) {
        user.setRoles(new ArrayList<>(roleRepository.findAllById(selectedRolesIds)));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUser(User updatedUser, List<String> selectedRoles) {
        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow();
        user.setUsername(updatedUser.getUsername());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (selectedRoles != null && !selectedRoles.isEmpty()) {
            List<Role> roles = roleRepository.findByNameIn(selectedRoles);
            user.setRoles(roles);
        } else {
            user.setRoles(Collections.emptyList());
        }

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void registerUser(User user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            user.setRoles(List.of(defaultRole));
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(UUID id){
        userRepository.deleteById(id);
    }
    //endregion
}