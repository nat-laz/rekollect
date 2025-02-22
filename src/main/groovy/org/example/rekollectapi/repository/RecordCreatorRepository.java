package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.RecordCreatorEntity;
import org.example.rekollectapi.model.ids.RecordCreatorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordCreatorRepository extends JpaRepository<RecordCreatorEntity, RecordCreatorId>  {
}
