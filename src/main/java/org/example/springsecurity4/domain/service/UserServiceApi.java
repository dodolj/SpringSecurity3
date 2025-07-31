package org.example.springsecurity4.domain.service;

import org.example.springsecurity4.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UserServiceApi extends UserDetailsService {

    //region readOnly
    List<User> findAll();

    User findById(UUID id);

    User findByUsername(String username);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    boolean userExists(String username);

    String getRolesAsString(String username);

    //region @Transactional
    @Transactional
    void saveUser(User user, List<UUID> selectedRolesIds);

    @Transactional
    void saveUser(User user);

    @Transactional
    void updateUser(User updatedUser, List<String> selectedRoles);

    @Transactional
    void registerUser(User user);

    @Transactional
    void deleteById(UUID id);
}
