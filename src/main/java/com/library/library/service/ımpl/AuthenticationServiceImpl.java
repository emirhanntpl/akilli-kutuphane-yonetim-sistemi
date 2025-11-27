package com.library.library.service.ımpl;

import com.library.library.dto.*;
import com.library.library.exception.BaseException;
import com.library.library.exception.MessageType;
import com.library.library.jwt.JwtService;
import com.library.library.model.RefreshToken;
import com.library.library.model.User;
import com.library.library.repository.RefreshTokenRepository;
import com.library.library.repository.UserRepository;
import com.library.library.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;

    public AuthenticationServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationProvider authenticationProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationProvider = authenticationProvider;
    }

    private RefreshToken craeteRefreshToken(User user){
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setCreateTime(new Date());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis()+1000*60*60*4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        return refreshToken;
    }

    private User createUser(AuthRequest request){
        User user=new User();
        user.setCreateTime(new Date());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return  user;
    }

    @Override
    public DtoUser register(AuthRequest request) {
       User newUser=createUser(request);
        User savedUser = userRepository.save(newUser);
        DtoUser dtoUser=new DtoUser();
        dtoUser.setUsername(savedUser.getUsername());
        return dtoUser;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {

        try {
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword());
            authenticationProvider.authenticate(authenticationToken);
            Optional<User> OptUser = userRepository.findByUsername(request.getUsername());
            String accesToken = jwtService.generateToken(OptUser.get());
            RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(OptUser.get()));
            return new AuthResponse(accesToken, savedRefreshToken.getRefreshToken());

        }catch (Exception e){
            throw new BaseException(MessageType.USERNAME_OR_PASSWORD_INVALID, HttpStatus.BAD_REQUEST);
        }

    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis() + 1000L * 60 * 60 *4 ));
        return refreshTokenRepository.save(refreshToken);
    }


    private boolean isValidRefreshToken(Date expiredDate){
        return new Date().before(expiredDate);
    }


    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        Optional<RefreshToken> OptRefreshToken = refreshTokenRepository.findByRefreshToken(request.getRefreshToken());
        if (OptRefreshToken.isEmpty()){
            throw new BaseException(MessageType.REFRESH_TOKEN_NOT_FOUND,HttpStatus.BAD_REQUEST);
        }
        if (!isValidRefreshToken(OptRefreshToken.get().getExpiredDate())){
            throw new BaseException(MessageType.REFRESH_TOKEN_IS_VALID,HttpStatus.BAD_REQUEST);
        }
        User user = OptRefreshToken.get().getUser();
        String accessToken = jwtService.generateToken(user);
        RefreshToken savedRefreshToken = refreshTokenRepository.save(craeteRefreshToken(user));
        return  new AuthResponse(accessToken,savedRefreshToken.getRefreshToken());
    }
}
