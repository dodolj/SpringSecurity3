package org.example.springsecurity4;

import org.example.springBootCrud.model.User;
import org.example.springBootCrud.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

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
}