package com.example.ecommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseRepo <E, ID> extends JpaRepository<E,ID>, JpaSpecificationExecutor<E> {
    long countByIdIn(List<ID> ids);
    Optional<E> findById(ID id);
}
