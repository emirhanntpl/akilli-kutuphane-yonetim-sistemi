package com.library.library.service;

import com.library.library.dto.AuthRequest;
import com.library.library.dto.AuthResponse;
import com.library.library.dto.DtoUser;
import com.library.library.dto.RefreshTokenRequest;

public interface AuthenticationService  {

    public  DtoUser register(AuthRequest request);

    public AuthResponse authenticate(AuthRequest request);

    public AuthResponse refreshToken(RefreshTokenRequest request);

}
