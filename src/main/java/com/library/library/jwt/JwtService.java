package com.library.library.jwt;

import com.library.library.model.User; // User modelini import ediyoruz
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public static final String SECRET_KEY = "/pkO7PE1EQ9rdWPckfLqpo38fA384DpfQu3liGJlx7k=";

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        User user = (User) userDetails;

        List<String> authorities = user.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());


        extraClaims.put("authorities", authorities);
        extraClaims.put("userId", user.getId());

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 4))
                .signWith(getKey())
                .compact();
    }

    public Key getKey() {
        byte[] decode = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(decode);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
        Claims claims = getClaims(token);
        return claimsFunction.apply(claims);
    }

    public String getUsernameByToken(String token) {
        return exportToken(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameByToken(token);
        final Date expireDate = exportToken(token, Claims::getExpiration);
        if (username == null || userDetails == null) return false;
        Date now = new Date();
        return username.equals(userDetails.getUsername()) && expireDate != null && now.before(expireDate);
    }
}
