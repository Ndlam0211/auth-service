package com.example.ecommerce.modules.user.repositories;

import com.example.ecommerce.repositories.BaseRepo;
import com.example.ecommerce.modules.user.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends BaseRepo<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);
    Optional<User> findByPhone(String phone);
    Boolean existsByEmail(String email);
    Boolean existsByUserName(String userName);
    Boolean existsByPhone(String phone);
}
