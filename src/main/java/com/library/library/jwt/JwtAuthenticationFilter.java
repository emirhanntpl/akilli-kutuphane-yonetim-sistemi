package com.library.library.jwt;

import com.library.library.exception.BaseException;
import com.library.library.exception.ErrorMessage;
import com.library.library.exception.MessageType;
import com.library.library.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SignatureException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String header = request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
        }
        String token = header.substring(7);
        String username ;
        try {
            username = jwtService.getUsernameByToken(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null && jwtService.isTokenValid(token)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        catch (ExpiredJwtException e){
           throw new BaseException(MessageType.REFRESH_TOKEN_IS_VALID, HttpStatus.BAD_REQUEST);
        }
        catch (MalformedJwtException  e){
            throw new BaseException(MessageType.REFRESH_TOKEN_ERROR,HttpStatus.BAD_REQUEST);
    }
        catch ( Exception e){
            throw new BaseException(MessageType.GENERAL_EXCEPTION,HttpStatus.BAD_REQUEST);
        }
        filterChain.doFilter(request, response);
    }
}

