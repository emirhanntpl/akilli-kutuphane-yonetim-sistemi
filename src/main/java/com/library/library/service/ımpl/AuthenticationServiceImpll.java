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
import com.library.library.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final EmailService emailService;

    public AuthenticationServiceImpll(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
        this.emailService = emailService;
    }

    private User createUser(CreateUserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setAddress(request.address());
        user.setRole(request.authorities()); 
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

        String subject = "Kütüphanemize Hoş Geldiniz!";
        String text = "Merhaba " + savedUser.getName() + ",\n\nKütüphane sistemimize kaydınız başarıyla tamamlanmıştır.";
        emailService.sendEmail(savedUser.getEmail(), subject, text);

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

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(MessageType.USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordTokenExpiry(LocalDateTime.now().plusMinutes(15)); 
        userRepository.save(user);

        // DÜZELTİLDİ: Link formatı düzeltildi
        String resetLink = "http://localhost:8080/reset-password.html?token=" + token;
        String subject = "Şifre Sıfırlama İsteği";
        String text = "Merhaba " + user.getName() + ",\n\n"
                    + "Şifrenizi sıfırlamak için aşağıdaki linke tıklayın:\n"
                    + resetLink + "\n\n"
                    + "Bu link 15 dakika boyunca geçerlidir.";
        
        emailService.sendEmail(user.getEmail(), subject, text);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new BaseException(MessageType.NO_RECORD_EXIST, HttpStatus.BAD_REQUEST));

        if (user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BaseException(MessageType.REFRESH_TOKEN_IS_VALID, HttpStatus.BAD_REQUEST); 
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpiry(null);
        userRepository.save(user);
    }
}
