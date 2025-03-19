package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.entity.RecordEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {

    Optional<RecordEntity> findById(UUID id);

    @Query("SELECT r FROM RecordEntity r " +
            "LEFT JOIN r.category c " +
            "LEFT JOIN r.tags t " +
            "LEFT JOIN r.creators rc " +
            "WHERE (:category IS NULL OR c.categoryName = :category) " +
            "AND (:tags IS NULL OR t.tagName IN :tags) " +
            "AND (:creator IS NULL OR rc.creator.creatorFirstName LIKE %:creator% OR rc.creator.creatorLastName LIKE %:creator%)")
    Page<RecordEntity> findAllRecordsByFilters(
            @Param("category") String category,
            @Param("tags") List<String> tags,
            @Param("creator") String creator,
            Pageable pageable);
}
