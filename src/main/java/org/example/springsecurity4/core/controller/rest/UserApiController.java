package org.example.springsecurity4.core.controller.rest;

import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.core.mapper.ManuallyUserMapper;
import org.example.springsecurity4.core.mapper.User2GetUsersResponseUserMapper;
import org.example.springsecurity4.domain.model.Role;
import org.example.springsecurity4.domain.model.RoleType;
import org.example.springsecurity4.domain.model.User;
import org.example.springsecurity4.domain.service.RoleServiceApi;
import org.example.springsecurity4.domain.service.UserServiceApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserApiController {

    private final UserServiceApi userServiceApi;
    private final User2GetUsersResponseUserMapper user2GetUsersResponseUserMapper;
    private final RoleServiceApi roleServiceApi;
    private final ManuallyUserMapper manuallyUserMapper;

    public UserApiController(UserServiceApi userServiceApi,
                             User2GetUsersResponseUserMapper user2GetUsersResponseUserMapper,
                             RoleServiceApi roleServiceApi,
                             ManuallyUserMapper manuallyUserMapper) {
        this.userServiceApi = userServiceApi;
        this.user2GetUsersResponseUserMapper = user2GetUsersResponseUserMapper;
        this.roleServiceApi = roleServiceApi;
        this.manuallyUserMapper = manuallyUserMapper;
    }

    @GetMapping
    public GetUsersResponse getAllUsers() {
        return new GetUsersResponse(
                userServiceApi.findAll().stream()
                        .map(user2GetUsersResponseUserMapper::toGetUsersResponseUser)
                        .toList()
        );
    }

    @GetMapping
    public List<RoleType> getAllRoles() {
        return roleServiceApi.findAllRoles()
                .stream()
                .map(Role::getName)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.name")
    public ResponseEntity<GetUsersResponse.User> getUserById(@PathVariable UUID id,
                                                             Authentication auth) {
        User user = userServiceApi.findById(id);
        if (!isAdmin(auth) && !user.getId().toString().equals(auth.getName())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(manuallyUserMapper.toGetUsersResponseUser(user));
    }

    @PostMapping(value = "/clients")
    public ResponseEntity<GetUsersResponse.User> createUser(@RequestBody User user) {
        if (userServiceApi.userExists(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        userServiceApi.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetUsersResponse.User> updateUser(@PathVariable UUID id,
                                                            @RequestBody User updatedUser) {
        User user = userServiceApi.findById(id);
        if (userServiceApi.userExists(user.getUsername())) {

            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setRoles(updatedUser.getRoles());
            user.setUpdatedAt(updatedUser.getUpdatedAt());
            userServiceApi.saveUser(user);

            return ResponseEntity.ok(manuallyUserMapper.toGetUsersResponseUser(user));
        }
        return ResponseEntity.status(451).build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (!userServiceApi.userExists(System.getProperty("user.name"))) {
            return ResponseEntity.notFound().build();
        }

        userServiceApi.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public boolean isAdmin(Authentication authentication){
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}