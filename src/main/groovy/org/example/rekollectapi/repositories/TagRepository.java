package org.example.rekollectapi.repositories;

import org.example.rekollectapi.models.entities.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    Optional<TagEntity> findByName(String name);
}