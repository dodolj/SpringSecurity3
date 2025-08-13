package org.example.springsecurity4.core.controller.rest;

import org.example.springsecurity4.core.dto.CreateUserRequest;
import org.example.springsecurity4.core.dto.GetUserResponse;
import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.core.dto.UpdateUserRequest;
import org.example.springsecurity4.core.exception.UserNameAlreadyExistException;
import org.example.springsecurity4.core.mapper.ManuallyUserMapper;
import org.example.springsecurity4.core.mapper.UserManagementService;
import org.example.springsecurity4.domain.model.User;
import org.example.springsecurity4.domain.service.UserServiceApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserServiceApi userServiceApi;
    private final ManuallyUserMapper manuallyUserMapper;
    private final UserManagementService userManagementService;

    public UserApiController(UserServiceApi userServiceApi,
                             ManuallyUserMapper manuallyUserMapper,
                             UserManagementService userManagementService) {
        this.userServiceApi = userServiceApi;
        this.manuallyUserMapper = manuallyUserMapper;
        this.userManagementService = userManagementService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public GetUsersResponse getAllUsers() {
        var users = userServiceApi.findAll().stream()
                .map(GetUserResponse::from)
                .toList();
        return new GetUsersResponse(users);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.equals(authentication.principal.id)")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable UUID id) {
        User user = userServiceApi.findById(id);
        return ResponseEntity.ok(manuallyUserMapper.toGetUsersResponseUser(user));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public GetUserResponse createUser(@RequestBody CreateUserRequest request) {
        return GetUserResponse.from(userManagementService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public GetUserResponse updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {
        return GetUserResponse.from(userManagementService.updateUser(id, request));
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