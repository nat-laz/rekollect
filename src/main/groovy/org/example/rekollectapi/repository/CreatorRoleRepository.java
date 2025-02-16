package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.CreatorRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreatorRoleRepository extends JpaRepository<CreatorRoleEntity, Integer> {

    Optional<CreatorRoleEntity> findByName(String name);

}
