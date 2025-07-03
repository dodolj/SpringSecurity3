package org.example.springsecurity4.service;

import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.RoleType;
import org.example.springsecurity4.model.User;
import org.example.springsecurity4.repository.RoleRepository;
import org.example.springsecurity4.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

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
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    //endregion

    //region @Transactional
    @Transactional
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User user) {
        User existing = userRepository.findById(user.getId()).orElseThrow();
        existing.setUsername(user.getUsername());
        existing.setPassword(passwordEncoder.encode(user.getPassword()));
        existing.setRoles(user.getRoles());
    }

    @Transactional
    public void registerUser(User user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName(RoleType.USER)
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            user.setRoles(List.of(defaultRole));
        }
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(UUID id){
        userRepository.deleteById(id);
    }
    //endregion
}