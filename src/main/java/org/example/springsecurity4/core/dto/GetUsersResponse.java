package org.example.springsecurity4.core.dto;

import java.util.List;

public record GetUsersResponse(
        List<GetUserResponse> users
) {

}
