package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Long> {

    List<TagEntity> findAll();

    List<TagEntity> findAllByTagNameIn(List<String> tagNames);

}
