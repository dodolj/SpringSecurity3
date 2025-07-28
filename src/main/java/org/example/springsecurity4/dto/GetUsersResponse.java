package org.example.springsecurity4.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GetUsersResponse(
        List<User> users
) {

    public record User(
            UUID id,
            String username,
            List<UUID> roles,
            Instant createdAt,
            Instant updatedAt
    ) {

    }
}
