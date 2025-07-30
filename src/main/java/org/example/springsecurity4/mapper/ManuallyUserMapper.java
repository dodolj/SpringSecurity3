package org.example.springsecurity4.mapper;

import org.example.springsecurity4.dto.GetUsersResponse;
import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class ManuallyUserMapper implements UserToGetUsersResponseUserMapper {

    @Override
    public GetUsersResponse.User toGetUsersResponseUser(User user) {
        if (user == null) {
            return null;
        }

        return new GetUsersResponse.User(
                user.getId(),
                user.getUsername(),
                Optional.ofNullable(user.getRoles()).stream()
                        .flatMap(Collection::stream)
                        .map(Role::getId)
                        .toList(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
