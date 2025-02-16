package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.CreatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CreatorRepository extends JpaRepository<CreatorEntity, UUID> {

    Optional<CreatorEntity> findByCreatorFirstNameAndCreatorLastName(String creatorFirstName, String creatorLastName);
}
