package org.example.springsecurity4.service;

import org.example.springsecurity4.model.User;
import org.example.springsecurity4.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteById(UUID id){
        userRepository.deleteById(id);
    }

    public User findById(UUID id) {
        return userRepository.findById(id).get();
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean userExists(String username) {
        return userRepository.existsById(UUID.fromString(username));
    }

    public void registerUser(User user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}