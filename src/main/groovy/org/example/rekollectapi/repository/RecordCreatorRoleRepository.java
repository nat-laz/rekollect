package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.RecordCreatorRoleEntity;
import org.example.rekollectapi.model.ids.RecordCreatorRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordCreatorRoleRepository extends JpaRepository<RecordCreatorRoleEntity, RecordCreatorRoleId>  {
}
