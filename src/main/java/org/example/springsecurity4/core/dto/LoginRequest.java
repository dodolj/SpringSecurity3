package org.example.springsecurity4.core.dto;

public record LoginRequest(
        String username,
        String password
) {}
