package org.example.springsecurity4.service;

import org.example.springsecurity4.model.Role;
import org.example.springsecurity4.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RoleService implements RoleServiceApi {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAllById(List<UUID> roleIds) {
        return roleRepository.findAllById(roleIds);
    }
}
