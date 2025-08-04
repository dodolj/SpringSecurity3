package org.example.springsecurity4.domain.repository;

import org.example.springsecurity4.domain.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Override
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    @Override
    @EntityGraph(attributePaths = "roles")
    List<User> findAllById(Iterable<UUID> uuids);

    @Override
    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(UUID uuid);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);
}