package com.library.library.service.ımpl;

import com.library.library.dto.*;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.jwt.JwtService;
import com.library.library.model.CreateUserRequest;
import com.library.library.model.RefreshToken;
import com.library.library.model.Role;
import com.library.library.model.User;
import com.library.library.repository.RefreshTokenRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service

public class AuthenticationServiceImpll implements AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;

    public AuthenticationServiceImpll(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
    }


    private RefreshToken craeteRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setCreateTime(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }

    private User createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setAddress(request.address());
        user.setRole(Set.of(Role.ROLE_USER));
        User savedUser = userRepository.save(user);
        return savedUser;

    }


    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 4));
        return refreshTokenRepository.save(refreshToken);
    }


    private boolean isValidRefreshToken(Date expiredDate) {
        return new Date().before(expiredDate);
    }


    @Override
    public DtoUser register(CreateUserRequest request) {
        User savedUser = createUser(request);

        DtoUser dtoUser = new DtoUser();
        dtoUser.setName(savedUser.getName());
        dtoUser.setUsername(savedUser.getUsername());
        dtoUser.setPassword(savedUser.getPassword());
        dtoUser.setId(savedUser.getId());
        dtoUser.setAddress(savedUser.getAddress());
        dtoUser.setEmail(savedUser.getEmail());

        return dtoUser;
    }


    @Override
    public AuthResponse authenticate(AuthResponse response) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(response.getUsername(), response.getPassword());
            authenticationProvider.authenticate(authenticationToken);
            Optional<User> OptUser = userRepository.findByUsername(response.getUsername());
            String accesToken = jwtService.generateToken(OptUser.get());
            RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(OptUser.get()));
            return new AuthResponse(accesToken, savedRefreshToken.getRefreshToken());

        } catch (Exception e) {
            throw new BaseException(MessageType.USERNAME_OR_PASSWORD_INVALID, HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> OptRefreshToken = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (OptRefreshToken.isEmpty()) {
            throw new BaseException(MessageType.REFRESH_TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (!isValidRefreshToken(OptRefreshToken.get().getExpiredDate())) {
            throw new BaseException(MessageType.REFRESH_TOKEN_IS_VALID, HttpStatus.BAD_REQUEST);
        }
        User user = OptRefreshToken.get().getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(craeteRefreshToken(user));
        return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
    }
}


