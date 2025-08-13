package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.CreateUserRequest;
import org.example.springsecurity4.core.dto.GetUserResponse;
import org.example.springsecurity4.core.dto.UpdateUserRequest;
import org.example.springsecurity4.domain.model.Role;
import org.example.springsecurity4.domain.model.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class ManuallyUserMapper implements User2GetUsersResponseUserMapper {

    @Override
    public GetUserResponse toGetUsersResponseUser(User user) {
        if (user == null) {
            return null;
        }

        var roleDto = Optional.ofNullable(user.getRoles()).stream()
                .flatMap(Collection::stream)
                .map(r -> new GetUserResponse.RoleDto(r.getId(), r.getName().name()))
                .toList();

        return new GetUserResponse(
                user.getId(),
                user.getUsername(),
                roleDto
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

    public void updateUserFromRequest(
            User user,
            UpdateUserRequest request,
            List<Role> roles) {
        user.setUsername(request.username());
        user.setPassword(request.password());
        user.setRoles(roles);
        user.setUpdatedAt(Instant.now());
    }
}
