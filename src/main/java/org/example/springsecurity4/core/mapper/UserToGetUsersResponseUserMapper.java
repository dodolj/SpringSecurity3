package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.domain.model.User;

public interface UserToGetUsersResponseUserMapper {

    GetUsersResponse.User toGetUsersResponseUser(User user);
}
