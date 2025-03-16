package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {

    Optional<RecordEntity> findById(UUID id);
}
