package com.example.ecommerce.modules.user.repositories;

import com.example.ecommerce.modules.user.entities.Permission;
import com.example.ecommerce.repositories.BaseRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("permissionRepo")
public interface PermissionRepo extends BaseRepo<Permission, Long> {
    Optional<Permission> findById(Long id);
}
