package com.example.ecommerce.modules.user.repositories;

import com.example.ecommerce.modules.user.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {
    boolean existsByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByUserId(Long userId);
    int deleteByExpiryDateBefore(LocalDateTime currentDateTime);
}
