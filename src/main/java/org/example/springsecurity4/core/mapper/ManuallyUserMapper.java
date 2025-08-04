package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.CreateUserRequest;
import org.example.springsecurity4.core.dto.GetUsersResponse;
import org.example.springsecurity4.core.dto.UpdateUserRequest;
import org.example.springsecurity4.domain.model.Role;
import org.example.springsecurity4.domain.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ManuallyUserMapper implements User2GetUsersResponseUserMapper {

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

    public User toDomainUser(CreateUserRequest request, List<Role> roles) {
        return new User(
                request.userId(),
                request.username(),
                request.password(),
                roles,
                Instant.now(),
                Instant.now()
        );
    }


    public void updateUserFromRequest(User user, UpdateUserRequest request, List<Role> roles) {
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setRoles(roles);
        user.setUpdatedAt(Instant.now());
    }
}
