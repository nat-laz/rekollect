package org.example.rekollectapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.*;
import org.example.rekollectapi.repository.*;
import org.example.rekollectapi.service.*;
import org.springframework.stereotype.Service;
import org.example.rekollectapi.exceptions.*;

import java.util.List;
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
        record.setUser(user);
        recordRepository.save(record);

        List<CreatorWithRoleResponseDTO> creatorResponses = creatorService.processCreatorsAndRoles(request.getCreators(), record);

        List<TagResponseDTO> tagResponses = tagService.processTags(request.getTags(), record);

        return new RecordResponseDTO(
                record.getId(),
                record.getTitle(),
                record.getDescription(),
                record.getCoverImageUrl(),
                record.getOnlineLink(),
                category.getCategoryName(),
                creatorResponses,
                tagResponses
        );
    }

    @Override
    public RecordResponseDTO getRecordById(UUID recordId) {
        RecordEntity record = recordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found with ID: " + recordId));

        // Fetch creators with their roles
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

        // Fetch tags
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
                creatorWithRoles,
                tags
        );
    }
}
