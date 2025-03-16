package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.RecordCreatorRoleEntity;
import org.example.rekollectapi.model.ids.RecordCreatorRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecordCreatorRoleRepository extends JpaRepository<RecordCreatorRoleEntity, RecordCreatorRoleId>  {

    List<RecordCreatorRoleEntity> findByRecordId(UUID recordId);
}
