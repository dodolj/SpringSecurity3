package org.example.springsecurity4.core.mapper;

import org.example.springsecurity4.core.dto.CreateUserRequest;
import org.example.springsecurity4.core.dto.UpdateUserRequest;
import org.example.springsecurity4.domain.model.User;
import org.example.springsecurity4.domain.service.RoleServiceApi;
import org.example.springsecurity4.domain.service.UserServiceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService {

    private final UserServiceApi userServiceApi;
    private final RoleServiceApi roleServiceApi;
    private final ManuallyUserMapper manuallyUserMapper;

    public UserManagementServiceImpl(UserServiceApi userServiceApi,
                                     RoleServiceApi roleServiceApi,
                                     ManuallyUserMapper manuallyUserMapper) {
        this.userServiceApi = userServiceApi;
        this.roleServiceApi = roleServiceApi;
        this.manuallyUserMapper = manuallyUserMapper;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        var roles = roleServiceApi.findAllById(request.roleIds());
        var user = manuallyUserMapper.toDomainUser(request, roles);
        userServiceApi.saveUser(user);
        return user;
    }

    @Override
    public User updateUser(UUID id, UpdateUserRequest request) {
        var user = userServiceApi.findById(id);
        var roles = roleServiceApi.findAllById(request.roleIds());
        manuallyUserMapper.updateUserFromRequest(user, request, roles);
        userServiceApi.saveUser(user);
        return user;
    }
}
