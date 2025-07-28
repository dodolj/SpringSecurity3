package org.example.springsecurity4.mapper;

import org.example.springsecurity4.dto.GetUsersResponse;
import org.example.springsecurity4.model.User;

public interface UserEntity2GetUsersDtoMapper {

    GetUsersResponse.User toDto(User user);
}
