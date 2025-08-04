package org.example.springsecurity4.core.controller.rest;

import org.example.springsecurity4.core.dto.CreateUserRequest;
import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.core.dto.UpdateUserRequest;
import org.example.springsecurity4.core.exception.UserNameAlreadyExistException;
import org.example.springsecurity4.core.mapper.ManuallyUserMapper;
import org.example.springsecurity4.core.mapper.User2GetUsersResponseUserMapper;
import org.example.springsecurity4.domain.model.Role;
import org.example.springsecurity4.domain.model.RoleType;
import org.example.springsecurity4.domain.model.User;
import org.example.springsecurity4.domain.service.RoleServiceApi;
import org.example.springsecurity4.domain.service.UserServiceApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
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
    @PreAuthorize("hasRole('ADMIN')")
    public GetUsersResponse getAllUsers() {
        return new GetUsersResponse(
                userServiceApi.findAll().stream()
                        .map(user2GetUsersResponseUserMapper::toGetUsersResponseUser)
                        .toList()
        );
    }

    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleType> getAllRoles() {
        return roleServiceApi.findAllRoles()
                .stream()
                .map(Role::getName)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.equals(authentication.principal.id)")
    public ResponseEntity<GetUsersResponse.User> getUserById(@PathVariable UUID id) {
        User user = userServiceApi.findById(id);
        return ResponseEntity.ok(manuallyUserMapper.toGetUsersResponseUser(user));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetUsersResponse.User> createUser(@RequestBody CreateUserRequest request) {
        /*лучше throw exception
        * return не response entity а сразу user без generics
        * а exception перехватывать либо внизу контроллеров, с помощью @exceptionHandler (problemDetail)
        * но чаще делают глобальный Exceptions перехватчик*/
        if (userServiceApi.userExists(request.username())) {
            throw new UserNameAlreadyExistException(request.username());
        }

        List<Role> roles = roleServiceApi.findAllById(request.roleIds());
        User user = manuallyUserMapper.toDomainUser(request, roles);
        userServiceApi.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(manuallyUserMapper.toGetUsersResponseUser(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetUsersResponse.User> updateUser(@PathVariable UUID id,
                                                            @RequestBody UpdateUserRequest request) {
        User user = userServiceApi.findById(id);
        List<Role> roles = roleServiceApi.findAllById(request.roleIds());
        manuallyUserMapper.updateUserFromRequest(user, request, roles);
        userServiceApi.saveUser(user);

        return ResponseEntity.ok(manuallyUserMapper.toGetUsersResponseUser(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        if (!userServiceApi.userExists(id)) {
            return ResponseEntity.notFound().build();
        }

        userServiceApi.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNameAlreadyExistException.class)
    public ProblemDetail handlerException(UserNameAlreadyExistException ex) {
        URI type = URI.create("urn://" + ex.getClass().getSimpleName());
        String title = "Registration error";
        String detail = "Username already exist";

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setType(type);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);

        return problemDetail;
    }
}