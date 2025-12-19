package com.library.library.repository;

import com.library.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    
    // YENİ METOTLAR
    Optional<User> findByEmail(String email);
    Optional<User> findByResetPasswordToken(String token);
}
