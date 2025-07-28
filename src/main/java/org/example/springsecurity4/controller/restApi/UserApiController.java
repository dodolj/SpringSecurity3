package org.example.springsecurity4.controller.restApi;

import org.example.springsecurity4.dto.GetUsersResponse;
import org.example.springsecurity4.mapper.UserEntity2GetUsersDtoMapper;
import org.example.springsecurity4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final UserEntity2GetUsersDtoMapper userEntity2GetUsersDtoMapper;

    public UserApiController(
            UserService userService,
            UserEntity2GetUsersDtoMapper userEntity2GetUsersDtoMapper) {
        this.userService = userService;
        this.userEntity2GetUsersDtoMapper = userEntity2GetUsersDtoMapper;
    }

    @GetMapping
    public GetUsersResponse getUsers() {
        var users = userService.findAll();
        return new GetUsersResponse(
                users.stream().map(userEntity2GetUsersDtoMapper::toDto).toList()
        );
    }
}
