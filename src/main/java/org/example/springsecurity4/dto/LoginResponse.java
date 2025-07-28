package org.example.springsecurity4.dto;

import java.util.UUID;

public record LoginResponse(
    UUID userId
) {

}
