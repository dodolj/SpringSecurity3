package org.example.springsecurity4.core.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetUsersResponse(
        List<User> users
) {

    public record User(
            UUID id,
            String username,
            List<UUID> roleIds,
            Instant createdAt,
            Instant updatedAt
    ) {

    }
}
