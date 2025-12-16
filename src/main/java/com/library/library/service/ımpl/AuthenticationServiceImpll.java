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

    private User createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setAddress(request.address());
        // DÜZELTİLDİ: Rolleri artık request'ten alıyor.
        user.setRole(request.authorities()); 
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        // 4 saatlik geçerlilik süresi
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
    public AuthResponse authenticate(AuthRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationProvider.authenticate(authenticationToken);

            Optional<User> optUser = userRepository.findByUsername(request.getUsername());
            if (optUser.isEmpty()) {
                throw new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.BAD_REQUEST);
            }

            String accessToken = jwtService.generateToken(optUser.get());
            RefreshToken savedRefreshToken = createRefreshToken(optUser.get());
            return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());

        } catch (Exception e) {
            e.printStackTrace(); 
            throw new BaseException(MessageType.AUTHENTICATION_FAILED, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> optRefreshToken = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (optRefreshToken.isEmpty()) {
            throw new BaseException(MessageType.REFRESH_TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (!isValidRefreshToken(optRefreshToken.get().getExpiredDate())) {
            throw new BaseException(MessageType.REFRESH_TOKEN_IS_VALID, HttpStatus.BAD_REQUEST);
        }
        User user = optRefreshToken.get().getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken savedRefreshToken = createRefreshToken(user);
        return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
    }
}