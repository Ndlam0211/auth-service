package com.example.ecommerce.modules.user.repositories;

import com.example.ecommerce.modules.user.entities.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BlacklistTokenRepo extends JpaRepository<BlacklistToken, Long> {
    boolean existsByToken(String token);
    int deleteByExpiryDateBefore(LocalDateTime currentDateTime);
}
