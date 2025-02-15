package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {

    Optional<TagEntity> findByName(String name);

    List<TagEntity> findAll();
}
