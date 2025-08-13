package org.example.springsecurity4.core.dto;

import org.example.springsecurity4.domain.model.User;

import java.util.List;
import java.util.UUID;

public record GetUserResponse(
        UUID id,
        String username,
        List<RoleDto> roles
) {

    public static GetUserResponse from(User user) {
        var roleDto = user.getRoles().stream()
                .map(r -> new RoleDto(r.getId(), r.getName().name()))
                .toList();
        return new GetUserResponse(user.getId(), user.getUsername(), roleDto);
    }

    public record RoleDto(
            UUID id,
            String name
    ) {

    }
}
