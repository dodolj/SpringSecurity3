package org.example.springsecurity4.core.controller.rest;

import org.example.springsecurity4.core.dto.GetRolesResponse;
import org.example.springsecurity4.domain.service.RoleServiceApi;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleApiController {

    private final RoleServiceApi roleServiceApi;

    public RoleApiController(RoleServiceApi roleServiceApi) {
        this.roleServiceApi = roleServiceApi;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public GetRolesResponse getAllRoles() {
        var items = roleServiceApi.findAllRoles().stream()
                .map(r -> new GetRolesResponse.RoleDto(r.getId(), r.getName()))
                .toList();
        return new GetRolesResponse(items);
    }
}
