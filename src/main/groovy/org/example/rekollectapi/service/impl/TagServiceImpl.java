package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.exceptions.ValidationException;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.entity.TagEntity;
import org.example.rekollectapi.repository.TagRepository;
import org.example.rekollectapi.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagResponseDTO> processTags(List<String> tagNames, RecordEntity record) {
        if (tagNames == null || tagNames.isEmpty()) {
            throw new ValidationException("At least one tag is required.");
        }

        // Trim tag names to prevent accidental duplicates
        tagNames = tagNames.stream()
                .map(String::trim)
                .filter(tagName -> !tagName.isEmpty())
                .collect(Collectors.toList());

        // Fetch existing tags in one batch query
        List<TagEntity> existingTags = tagRepository.findAllByTagNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(TagEntity::getTagName)
                .collect(Collectors.toSet());

        //  Create only non-existing tags
        List<TagEntity> newTags = tagNames.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .map(tagName -> new TagEntity(null, tagName))
                .toList();

        if (!newTags.isEmpty()) {
            tagRepository.saveAll(newTags);
            existingTags.addAll(newTags);
        }

        record.getTags().clear();
        record.getTags().addAll(existingTags);

        return existingTags.stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                .toList();
    }

}
