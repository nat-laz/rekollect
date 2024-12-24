package org.example.rekollectapi.repositories;

import org.example.rekollectapi.models.entities.CategoryEntity;
import org.example.rekollectapi.models.entities.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByName(String name);
}

