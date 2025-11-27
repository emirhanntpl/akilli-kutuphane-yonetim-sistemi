package com.library.library.controller.Impl;

import com.library.library.controller.AuthenticationController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.AuthRequest;
import com.library.library.dto.AuthResponse;
import com.library.library.dto.DtoUser;
import com.library.library.dto.RefreshTokenRequest;
import com.library.library.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.library.library.controller.RootEntity.ok;

@RestController
public class AuthenticationControllerImpl implements AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    @Override
    public RootEntity<DtoUser> register(AuthRequest request) {
        return ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    @Override
    public RootEntity<AuthResponse> authenticate(AuthRequest request) {
        return ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest request) {
        return ok(authenticationService.refreshToken(request));
    }
}
