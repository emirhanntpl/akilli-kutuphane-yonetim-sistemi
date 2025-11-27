package com.library.library.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    public static final String SECRET_KEY = "/pkO7PE1EQ9rdWPckfLqpo38fA384DpfQu3liGJlx7k=";

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4))
                .signWith(getKey())
                .compact();
    }

    public Key getKey() {
        byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
    }

    public  Claims getClaims(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return  claims;
    }


    public <T> T exportToken(String token, Function<Claims,T> claimsFunction){
        Claims claims = getClaims(token);
        return  claimsFunction.apply(claims);
    }

    public String  getUsernameByToken(String token){
        return  exportToken(token, Claims::getSubject);
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = getUsernameByToken(token);
        final Date expireDate = exportToken(token, Claims::getExpiration);
        return (username.equals(userDetails.getUsername()) && !new Date().before(expireDate));

    }
}
