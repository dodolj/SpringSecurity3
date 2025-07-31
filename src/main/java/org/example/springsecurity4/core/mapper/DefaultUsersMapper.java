package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.domain.model.Role;
import org.example.springsecurity4.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class DefaultUsersMapper implements UserEntity2GetUsersDtoMapper {

    @Override
    public GetUsersResponse.User toDto(User user) {
        if (user == null) {
            return null;
        }
        return new  GetUsersResponse.User(
                user.getId(),
                user.getUsername(),
                Optional.ofNullable(user.getRoles()).stream()
                        .flatMap(Collection::stream)
                        .map(Role::getId).toList(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
