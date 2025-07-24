package org.example.springsecurity4.repository;

import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleType name);

    Optional<Role> findById(UUID id);

    List<Role> findByNameIn(List<String> selectedRoles);
}
