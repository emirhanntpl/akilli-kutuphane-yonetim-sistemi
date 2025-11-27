package com.library.library.controller;

import com.library.library.dto.AuthRequest;
import com.library.library.dto.AuthResponse;
import com.library.library.dto.DtoUser;
import com.library.library.dto.RefreshTokenRequest;

public interface AuthenticationController {

    public RootEntity<DtoUser> register(AuthRequest request);

    public RootEntity<AuthResponse> authenticate(AuthRequest request);

    public RootEntity<AuthResponse> refreshToken(RefreshTokenRequest request);


}
