package org.example.rekollectapi.repository;


import org.example.rekollectapi.model.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // Optional<T> = represents a container object which may or may not contain a non-null value.
    Optional<CategoryEntity> findByCategoryNameIgnoreCase(String name);

    List<CategoryEntity> findAll();

}

