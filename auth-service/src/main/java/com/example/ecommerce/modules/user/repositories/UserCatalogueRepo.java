package com.example.ecommerce.modules.user.repositories;

import com.example.ecommerce.repositories.BaseRepo;
import com.example.ecommerce.modules.user.entities.UserCatalogue;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCatalogueRepo extends BaseRepo<UserCatalogue, Long> {
}