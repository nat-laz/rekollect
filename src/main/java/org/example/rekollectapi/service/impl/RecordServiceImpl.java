package org.example.rekollectapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.request.RecordUpdateDTO;
import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.*;
import org.example.rekollectapi.model.index.RecordIndex;
import org.example.rekollectapi.repository.*;
import org.example.rekollectapi.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.example.rekollectapi.exceptions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CreatorService creatorService;
    private final TagService tagService;
    private final RecordCreatorRoleRepository recordCreatorRoleRepository;
    private final ElasticsearchRecordService elasticsearchRecordService;

    /**
     * Creates a new record, saves it in PostgreSQL, and asynchronously indexes it in Elasticsearch.
     */
    @Override
    public RecordResponseDTO createRecord(RecordRequestDTO request, UUID authenticatedUserId) {

        UserEntity user = Optional.ofNullable(userService.getUser(authenticatedUserId))
                .orElseThrow(() -> new ValidationException("Invalid user ID: " + authenticatedUserId));

        CategoryEntity category = Optional.ofNullable(categoryService.getOrCreateCategory(request.getCategoryName()))
                .orElseThrow(() -> new ValidationException("Category could not be created: " + request.getCategoryName()));

        RecordEntity record = new RecordEntity();
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setCoverImageUrl(request.getCoverImageUrl());
        record.setOnlineLink(request.getOnlineLink());
        record.setCategory(category);
        record.setUser(user);

        if (request.getReleaseDate() != null && !request.getReleaseDate().isEmpty()) {
            record.setReleaseDate(LocalDate.parse(request.getReleaseDate()));
        }

        record = recordRepository.saveAndFlush(record);

        List<CreatorWithRoleResponseDTO> creatorResponses = creatorService.processCreatorsAndRoles(request.getCreators(), record);
        List<TagResponseDTO> tagResponses = tagService.processTags(request.getTags(), record);

        // sync with Elasticsearch
        indexRecordAsync(record, creatorResponses, tagResponses);

        return convertEntityToDTO(record, creatorResponses, tagResponses);
    }

    /**
     * Asynchronously indexes the record in Elasticsearch to prevent blocking.
     */
    @Async
    public void indexRecordAsync(RecordEntity record, List<CreatorWithRoleResponseDTO> creators, List<TagResponseDTO> tags) {
        try {
            elasticsearchRecordService.saveRecordToElasticsearch(record, creators, tags);
        } catch (Exception e) {
            System.err.println("⚠ Elasticsearch indexing failed: " + e.getMessage());
        }
    }


    /**
     * Fetch paginated and sorted records (Default View).
     */
    @Override
    public Page<RecordResponseDTO> getDefaultRecords(int page, int size, String sortField, String sortDirection) {
        String validatedSortField = validateSortField(sortField);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), validatedSortField));

        return recordRepository.findAll(pageable)
                .map(record -> convertEntityToDTO(
                        record, getCreatorsByRecordId(record.getId()), getTagsFromRecord(record)
                ));
    }


    /**
     * Get a record by ID, including its related creators and tags.
     */
    @Override
    public RecordResponseDTO getRecordById(UUID recordId) {
        return recordRepository.findById(recordId)
                .map(record -> convertEntityToDTO(record, getCreatorsByRecordId(record.getId()), getTagsFromRecord(record)))
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with ID: " + recordId));
    }

    /**
     * Updates an existing record and syncs changes to Elasticsearch.
     */
    @Override
    @Transactional
    public RecordResponseDTO updateRecord(UUID recordId, RecordUpdateDTO updateDTO) {
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with ID: " + recordId));

        Optional.ofNullable(updateDTO.getTitle()).ifPresent(record::setTitle);
        Optional.ofNullable(updateDTO.getDescription()).ifPresent(record::setDescription);
        Optional.ofNullable(updateDTO.getCoverImageUrl()).ifPresent(record::setCoverImageUrl);
        Optional.ofNullable(updateDTO.getOnlineLink()).ifPresent(record::setOnlineLink);

        if (updateDTO.getReleaseDate() != null && !updateDTO.getReleaseDate().isEmpty()) {
            record.setReleaseDate(LocalDate.parse(updateDTO.getReleaseDate()));
        }

        Optional.ofNullable(updateDTO.getCategory())
                .map(categoryService::getOrCreateCategory)
                .ifPresent(record::setCategory);

        List<CreatorWithRoleResponseDTO> creatorResponses = updateDTO.getCreators() != null && !updateDTO.getCreators().isEmpty()
                ? creatorService.processCreatorsAndRoles(updateDTO.getCreators(), record)
                : getCreatorsByRecordId(recordId);

        List<TagResponseDTO> tagResponses = updateDTO.getTags() != null && !updateDTO.getTags().isEmpty()
                ? tagService.processTags(updateDTO.getTags(), record)
                : getTagsFromRecord(record);

        record = recordRepository.saveAndFlush(record);

        indexRecordAsync(record, creatorResponses, tagResponses);

        return convertEntityToDTO(record, creatorResponses, tagResponses);
    }


    /**
     * Filters & sorts records:
     * PRIMARY: Elasticsearch
     * FALLBACK: PostgreSQL).
     */
    @Override
    public List<RecordResponseDTO> filterAndSortRecords(String category, List<String> tags, String creator, int page, int size, String sortField, String sortOrder) {
        try {
            List<RecordIndex> elasticRecords = elasticsearchRecordService.filterRecords(category, tags, creator, page, size, sortField, sortOrder);
            return elasticRecords.stream()
                    .map(this::convertIndexToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("⚠ Elasticsearch failed, falling back to PostgreSQL: " + e.getMessage());
            return getFilteredRecordsFromPostgreSQL(category, tags, creator, page, size, sortField, sortOrder);
        }
    }

    /**
     * Fetch filtered and sorted records from PostgreSQL (Fallback).
     */
    private List<RecordResponseDTO> getFilteredRecordsFromPostgreSQL(
            String category, List<String> tags, String creator, int page, int size, String sortField, String sortOrder) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField != null ? sortField : "createdAt"));
        Page<RecordEntity> recordsPage = recordRepository.findAllRecordsByFilters(category, tags, creator, pageable);

        return recordsPage.getContent().stream()
                .map(record -> convertEntityToDTO(record, getCreatorsByRecordId(record.getId()), getTagsFromRecord(record)))
                .collect(Collectors.toList());
    }


    /**
     * Validates the sorting field to prevent invalid queries.
     */
    private String validateSortField(String sortField) {
        List<String> allowedSortFields = List.of("title", "createdAt", "updatedAt");
        return allowedSortFields.contains(sortField) ? sortField : "createdAt";
    }

    /**
     * Fetch creators associated with a record.
     */
    private List<CreatorWithRoleResponseDTO> getCreatorsByRecordId(UUID recordId) {
        return recordCreatorRoleRepository.findByRecordId(recordId)
                .stream()
                .map(rc -> new CreatorWithRoleResponseDTO(
                        rc.getCreator().getId(),
                        rc.getCreator().getCreatorFirstName(),
                        rc.getCreator().getCreatorLastName(),
                        rc.getCreator().getCreatorBio(),
                        rc.getRole().getRoleName()))
                .collect(Collectors.toList());
    }

    /**
     * Fetch tags associated with a record.
     */
    private List<TagResponseDTO> getTagsFromRecord(RecordEntity record) {
        return record.getTags().stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                .collect(Collectors.toList());
    }

    /**
     * Converts RecordEntity to DTO.
     */
    private RecordResponseDTO convertEntityToDTO(RecordEntity record, List<CreatorWithRoleResponseDTO> creators, List<TagResponseDTO> tags) {
        return new RecordResponseDTO(
                record.getId(),
                record.getTitle(),
                record.getDescription(),
                record.getCoverImageUrl(),
                record.getOnlineLink(),
                record.getCategory().getCategoryName(),
                record.getReleaseDate() != null ? record.getReleaseDate().toString() : null,
                record.getCreatedAt(),
                record.getUpdatedAt(),
                creators,
                tags
        );
    }

    /**
     * Converts Elasticsearch RecordIndex to DTO.
     */
    private RecordResponseDTO convertIndexToDTO(RecordIndex recordIndex) {
        List<CreatorWithRoleResponseDTO> creators = recordIndex.getCreators().stream()
                .map(creator -> new CreatorWithRoleResponseDTO(
                        null,
                        creator.getFirstName(),
                        creator.getLastName(),
                        null,
                        creator.getRole()
                ))
                .collect(Collectors.toList());

        List<TagResponseDTO> tags = recordIndex.getTags().stream().map(
                tag -> new TagResponseDTO(null, tag)
        ).collect(Collectors.toList()
        );

        return new RecordResponseDTO(
                UUID.fromString(recordIndex.getId()),
                recordIndex.getTitle(),
                recordIndex.getDescription(),
                null,
                null,
                recordIndex.getCategory(),
                recordIndex.getReleaseDate(),
                recordIndex.getCreatedAt(),
                recordIndex.getUpdatedAt(),
                creators,
                tags
        );
    }

}
