package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.entity.RecordTagEntity;
import org.example.rekollectapi.model.entity.TagEntity;
import org.example.rekollectapi.model.ids.RecordTagId;
import org.example.rekollectapi.repository.RecordTagRepository;
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
    private final RecordTagRepository recordTagRepository;

    @Override
    public List<TagResponseDTO> getAllTags() {
        List<TagEntity> tags = tagRepository.findAll();

        return tags.stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagResponseDTO> processTags(List<String> tagNames, RecordEntity record) {

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
        }

        //  Merge existing & new tags
        existingTags.addAll(newTags);

        //  Create record-tags relationships in batch
        List<RecordTagEntity> recordTags = existingTags.stream()
                .map(tag -> new RecordTagEntity(new RecordTagId(record.getId(), tag.getId()), record, tag))
                .toList();

        // Save in batch
        recordTagRepository.saveAll(recordTags);

        return existingTags.stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                .toList();
    }

}
