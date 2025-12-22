package com.library.library.controller.Impl;

import com.library.library.controller.AuthenticationController;
import com.library.library.controller.RootEntity;
import com.library.library.dto.AuthRequest;
import com.library.library.dto.AuthResponse;
import com.library.library.dto.DtoUser;
import com.library.library.dto.RefreshTokenRequest;
import com.library.library.model.CreateUserRequest;
import com.library.library.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.library.library.controller.RootEntity.ok;

@RestController
public class AuthenticationControllerImpl implements AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/register")
    @Override
    public RootEntity<DtoUser> register(@Valid @RequestBody CreateUserRequest request) {
        return ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    @Override
    public RootEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        return ok(authenticationService.authenticate(request));
    }


    @PostMapping("/refreshToken")
    @Override
    public RootEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ok(authenticationService.refreshToken(request));
    }

    @PostMapping("/forgot-password")
    @Override
    public RootEntity<String> forgotPassword(@RequestParam String email) {
        authenticationService.forgotPassword(email);
        return ok("Şifre sıfırlama linki e-posta adresinize gönderildi.");
    }

    @PostMapping("/reset-password")
    @Override
    public RootEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        authenticationService.resetPassword(token, newPassword);
        return ok("Şifreniz başarıyla güncellendi.");
    }
}
