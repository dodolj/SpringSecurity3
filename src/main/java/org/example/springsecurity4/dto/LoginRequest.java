package org.example.springsecurity4.dto;

public record LoginRequest(
        String username,
        String password
) {

}
