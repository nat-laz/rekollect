package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.RecordTagEntity;
import org.example.rekollectapi.model.ids.RecordTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordTagRepository extends JpaRepository<RecordTagEntity, RecordTagId> {
}
