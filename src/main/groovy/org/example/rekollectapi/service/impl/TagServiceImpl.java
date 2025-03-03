package org.example.rekollectapi.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.exceptions.ValidationException;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.entity.TagEntity;
import org.example.rekollectapi.repository.TagRepository;
import org.example.rekollectapi.service.TagService;
import org.springframework.stereotype.Service;
import org.example.rekollectapi.util.TagUtil;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    @Transactional
    public List<TagResponseDTO> processTags(List<String> tagNames, RecordEntity record) {

        if (tagNames == null || tagNames.isEmpty()) {
            throw new ValidationException("At least one tag is required.");
        }

        Set<String> sanitizedTagNames = TagUtil.sanitizedTagNames(tagNames);

        List<TagEntity> existingTags = fetchExistingTags(sanitizedTagNames);

        List<TagEntity> newTags = saveNewTags(sanitizedTagNames, existingTags);

        linkTagsToRecord(record, existingTags, newTags);

        return convertToDTO(existingTags, newTags);
    }


    public List<TagEntity> fetchExistingTags(Set<String> tagNames) {
        return tagRepository.findAllByTagNameIn(tagNames);
    }

    public List<TagEntity> saveNewTags(Set<String> newTagNames, List<TagEntity> existingTags) {
        Set<String> getExistingTagNames = existingTags.stream()
                .map(TagEntity::getTagName)
                .collect(Collectors.toSet());

        List<TagEntity> newTags = newTagNames.stream()
                .filter(tagName -> !getExistingTagNames.contains(tagName))
                .map(tagName -> new TagEntity(null, tagName))
                .toList();

        return newTags.isEmpty() ? Collections.emptyList() : tagRepository.saveAll(newTags);
    }

    public void linkTagsToRecord(RecordEntity record, List<TagEntity> existingTags, List<TagEntity> newTags) {
        if (record.getTags() == null) {
            record.setTags(new HashSet<>());
        }
        record.getTags().clear();

        record.getTags().addAll(existingTags);
        record.getTags().addAll(newTags);
    }

    public List<TagResponseDTO> convertToDTO(List<TagEntity> existingTags, List<TagEntity> newTags) {
        List<TagEntity> allTags = new ArrayList<>(existingTags);
        allTags.addAll(newTags);

        return allTags.stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                .toList();
    }
}
