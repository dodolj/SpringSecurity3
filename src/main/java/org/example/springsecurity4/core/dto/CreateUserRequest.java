package org.example.springsecurity4.core.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreateUserRequest(
        UUID userId,
        String username,
        String password,
        List<UUID> roleIds,
        Instant createdAt
) {
    public CreateUserRequest(UUID userId, String username, String password, List<UUID> roleIds, Instant createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleIds = roleIds;
        this.createdAt = createdAt;
    }
}
