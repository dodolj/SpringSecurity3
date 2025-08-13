package org.example.springsecurity4.core.dto;

import org.example.springsecurity4.domain.model.RoleType;

import java.util.List;
import java.util.UUID;

public record GetRolesResponse(
        List<RoleDto> roles
) {
    public record RoleDto(
            UUID id,
            RoleType name
    ) {

    }
}