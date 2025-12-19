package com.library.library.security;

import com.library.library.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] WHITE_LIST_URLS = {
            "/authenticate",
            "/register",
            "/refreshToken",
            "/forgot-password",
            "/reset-password",
            "/index.html",
            "/login.html",
            "/staff-login.html",
            "/register.html",
            "/reset-password.html",
            "/", 
            "/*.html", 
            "/*.css", 
            "/*.js", 
            "/images/**"
    };

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URLS).permitAll()
                                // KULLANICIYA ÖZEL İZİNLER (Admin kurallarından önce gelmeli)
                                .requestMatchers("/rest/api/reviews/**").authenticated()
                                .requestMatchers("/rest/api/loans/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/rest/api/users/{userId}/favorites/**").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/rest/api/reservations/**").hasAnyRole("USER", "ADMIN")
                                
                                // Bakiye ve Borç Ödeme için spesifik izinler
                                .requestMatchers(HttpMethod.POST, "/rest/api/users/*/load-balance").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/rest/api/users/*/pay-penalty").hasAnyRole("USER", "ADMIN")
                                
                                // Borç ve Bakiye Görüntüleme (GET) için spesifik izinler
                                .requestMatchers(HttpMethod.GET, "/rest/api/users/*/penalty").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/rest/api/users/*/balance").hasAnyRole("USER", "ADMIN")

                                .requestMatchers("/rest/api/users/**").hasAnyRole("USER", "ADMIN")

                                // ADMİN'E ÖZEL YAZMA İZİNLERİ
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/rest/api/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/rest/api/**").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/rest/api/**").hasRole("ADMIN")
                                
                                // Geri kalan tüm GET istekleri ve diğerleri için kimlik doğrulaması yeterli
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
