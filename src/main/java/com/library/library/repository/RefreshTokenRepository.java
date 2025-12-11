package com.library.library.repository;

import com.library.library.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);


    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from RefreshToken r where r.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    java.util.List<com.library.library.model.RefreshToken> findByUserId(Long userId);
}
