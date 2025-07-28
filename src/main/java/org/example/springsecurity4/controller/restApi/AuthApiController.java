package org.example.springsecurity4.controller.restApi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.springsecurity4.dto.LoginRequest;
import org.example.springsecurity4.dto.LoginResponse;
import org.example.springsecurity4.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApiController {

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

    public AuthApiController(AuthenticationManager authenticationManager,
                             SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/api/login")
    public LoginResponse login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {

        var token = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
        var authentication = (UsernamePasswordAuthenticationToken) authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), httpServletRequest, httpServletResponse);

        return new LoginResponse(((User) authentication.getDetails()).getId());
    }
}
