package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.GetUserResponse;
import org.example.springsecurity4.domain.model.User;

public interface User2GetUsersResponseUserMapper {

    GetUserResponse toGetUsersResponseUser(User user);
}
