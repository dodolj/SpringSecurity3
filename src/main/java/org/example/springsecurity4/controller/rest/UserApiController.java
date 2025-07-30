package org.example.springsecurity4.controller.rest;

import org.example.springsecurity4.dto.GetUsersResponse;
import org.example.springsecurity4.mapper.UserToGetUsersResponseUserMapper;
import org.example.springsecurity4.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final UserToGetUsersResponseUserMapper userToGetUsersResponseUserMapper;

    public UserApiController(UserService userService,
                             UserToGetUsersResponseUserMapper userToGetUsersResponseUserMapper) {
        this.userService = userService;
        this.userToGetUsersResponseUserMapper = userToGetUsersResponseUserMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public GetUsersResponse getUsers() {
        return new GetUsersResponse(
                userService.findAll().stream()
                        .map(userToGetUsersResponseUserMapper::toGetUsersResponseUser)
                        .toList()
        );
    }
}
