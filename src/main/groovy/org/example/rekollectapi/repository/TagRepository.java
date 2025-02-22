package org.example.rekollectapi.repository;

import org.example.rekollectapi.dto.request.TagRequestDTO;
import org.example.rekollectapi.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    Optional<TagEntity> findByTagNameIgnoreCase(String name);

    List<TagEntity> findAll();

    List<TagEntity> findAllByTagNameIn(List<String> tagNames);
}
