package org.example.springsecurity4.mapper;

import org.example.springsecurity4.dto.GetUsersResponse;
import org.example.springsecurity4.model.User;

public interface UserToGetUsersResponseUserMapper {

    GetUsersResponse.User toGetUsersResponseUser(User user);
}
