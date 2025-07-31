package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.domain.model.User;

public interface User2GetUsersResponseUserMapper {

    GetUsersResponse.User toGetUsersResponseUser(User user);
}
