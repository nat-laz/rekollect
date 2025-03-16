package org.example.rekollectapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.request.RecordUpdateDTO;
import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.*;
import org.example.rekollectapi.repository.*;
import org.example.rekollectapi.service.*;
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

    @Override
    public RecordResponseDTO createRecord(RecordRequestDTO request, UUID authenticatedUserId) {

        UserEntity user = userService.getUser(authenticatedUserId);
        if (user == null) {
            throw new ValidationException("Invalid user ID: " + authenticatedUserId);
        }

        CategoryEntity category = categoryService.getOrCreateCategory(request.getCategoryName());
        if (category == null) {
            throw new ValidationException("Category could not be created: " + request.getCategoryName());
        }

        RecordEntity record = new RecordEntity();
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setCoverImageUrl(request.getCoverImageUrl());
        record.setOnlineLink(request.getOnlineLink());
        record.setCategory(category);

        if (request.getReleaseDate() != null && !request.getReleaseDate().isEmpty()) {
            record.setReleaseDate(LocalDate.parse(request.getReleaseDate()));
        }

        record.setUser(user);

        record = recordRepository.saveAndFlush(record);


        record = recordRepository.findById(record.getId()).orElseThrow(
                () -> new RuntimeException("Record not found after save")
        );

        List<CreatorWithRoleResponseDTO> creatorResponses = creatorService.processCreatorsAndRoles(request.getCreators(), record);
        List<TagResponseDTO> tagResponses = tagService.processTags(request.getTags(), record);

        // Save to Elasticsearch too
        elasticsearchRecordService.saveRecordToElasticsearch(record, creatorResponses, tagResponses);

        return new RecordResponseDTO(
                record.getId(),
                record.getTitle(),
                record.getDescription(),
                record.getCoverImageUrl(),
                record.getOnlineLink(),
                category.getCategoryName(),
                record.getReleaseDate() != null ? record.getReleaseDate().toString() : null,
                record.getCreatedAt(),
                record.getUpdatedAt(),
                creatorResponses,
                tagResponses
        );
    }


    @Override
    public RecordResponseDTO getRecordById(UUID recordId) {
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with ID: " + recordId));

        List<CreatorWithRoleResponseDTO> creatorWithRoles = recordCreatorRoleRepository.findByRecordId(recordId)
                .stream()
                .map(recordCreatorRole -> new CreatorWithRoleResponseDTO(
                        recordCreatorRole.getCreator().getId(),
                        recordCreatorRole.getCreator().getCreatorFirstName(),
                        recordCreatorRole.getCreator().getCreatorLastName(),
                        recordCreatorRole.getCreator().getCreatorBio(),
                        recordCreatorRole.getRole().getRoleName()
                ))
                .collect(Collectors.toList());

        List<TagResponseDTO> tags = record.getTags()
                .stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                .collect(Collectors.toList());

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
                creatorWithRoles,
                tags
        );
    }

    // TODO: auth
    @Override
    @Transactional
    public RecordResponseDTO updateRecord(UUID recordId, RecordUpdateDTO updateDTO) {
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with ID: " + recordId));

//        if (!record.getUser().getId().equals(authenticatedUserId)) {
//            throw new UnauthorizedException("You are not authorized to update this record.");
//        }

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

        List<CreatorWithRoleResponseDTO> creatorResponses = List.of();
        if (updateDTO.getCreators() != null && !updateDTO.getCreators().isEmpty()) {
            recordCreatorRoleRepository.deleteByRecordId(recordId);
            creatorResponses = creatorService.processCreatorsAndRoles(updateDTO.getCreators(), record);
        }

        List<TagResponseDTO> tagResponses = List.of();
        if (updateDTO.getTags() != null && !updateDTO.getTags().isEmpty()) {
            record.getTags().clear();
            tagResponses = tagService.processTags(updateDTO.getTags(), record);
        }

        record = recordRepository.saveAndFlush(record);

        // sync with Elasticsearch
        elasticsearchRecordService.saveRecordToElasticsearch(record, creatorResponses, tagResponses);

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
                creatorResponses,
                tagResponses
        );
    }

}
