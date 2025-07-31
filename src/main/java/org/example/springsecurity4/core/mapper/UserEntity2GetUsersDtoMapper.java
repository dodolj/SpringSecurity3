package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.domain.model.User;

public interface UserEntity2GetUsersDtoMapper {

    GetUsersResponse.User toDto(User user);
}
