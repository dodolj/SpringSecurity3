package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.CreateUserRequest;
import org.example.springsecurity4.core.dto.UpdateUserRequest;
import org.example.springsecurity4.domain.model.User;

import java.util.UUID;

public interface UserManagementService {

    User createUser(CreateUserRequest request);
    User updateUser(UUID id, UpdateUserRequest request);
}