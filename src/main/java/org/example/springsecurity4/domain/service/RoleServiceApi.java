package org.example.springsecurity4.domain.service;

import org.example.springsecurity4.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleServiceApi {
    List<Role> findAllRoles();

    Optional<Role> findById(UUID id);

    List<Role> findAllById(List<UUID> roleIds);
}
