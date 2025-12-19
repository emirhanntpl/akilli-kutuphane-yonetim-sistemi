package com.library.library.service;

import com.library.library.dto.AuthRequest;
import com.library.library.dto.AuthResponse;
import com.library.library.dto.DtoUser;
import com.library.library.dto.RefreshTokenRequest;
import com.library.library.model.CreateUserRequest;

public interface AuthenticationService  {

    public  DtoUser register(CreateUserRequest request);

    public AuthResponse authenticate(AuthRequest request);

    public AuthResponse refreshToken(RefreshTokenRequest request);


    void forgotPassword(String email);
    void resetPassword(String token, String newPassword);
}
