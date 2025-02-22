package org.example.rekollectapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.*;
import org.example.rekollectapi.repository.*;
import org.example.rekollectapi.service.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CreatorService creatorService;
    private final TagService tagService;

    @Override
    public RecordResponseDTO createRecord(RecordRequestDTO request, UUID authenticatedUserId) {

        UserEntity user = userService.getUser(authenticatedUserId);
        CategoryEntity category = categoryService.getOrCreateCategory(request.getCategoryName());

        RecordEntity record = new RecordEntity();
        record.setTitle(request.getTitle());
        record.setDescription(request.getDescription());
        record.setCoverImageUrl(request.getCoverImageUrl());
        record.setOnlineLink(request.getOnlineLink());
        record.setCategory(category);
        record.setUser(user);
        recordRepository.save(record);

        List<CreatorResponseDTO> creatorResponses = creatorService.processCreatorsAndRoles(request.getCreators(), record);

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
}
