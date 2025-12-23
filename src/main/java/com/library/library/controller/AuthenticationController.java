package com.library.library.controller;

import com.library.library.dto.AuthRequest;
import com.library.library.dto.AuthResponse;
import com.library.library.dto.DtoUser;
import com.library.library.dto.RefreshTokenRequest;
import com.library.library.model.CreateUserRequest;
import org.springframework.web.bind.annotation.RequestParam;

public interface AuthenticationController {

   public RootEntity<DtoUser> register(CreateUserRequest request);

    public RootEntity<AuthResponse> authenticate(AuthRequest request);

    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest request);


    RootEntity<String> forgotPassword(String email);
    RootEntity<String> resetPassword(String token, String newPassword);
}
