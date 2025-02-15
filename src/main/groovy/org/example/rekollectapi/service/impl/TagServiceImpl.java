package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.TagRequestDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.exceptions.BadRequestException;
import org.example.rekollectapi.model.entity.TagEntity;
import org.example.rekollectapi.repository.TagRepository;
import org.example.rekollectapi.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public TagResponseDTO createTag(TagRequestDTO request) {
        if(tagRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Tag already exists" + request.getName());
        }

        TagEntity tag = new TagEntity();
        tag.setName(request.getName());
        tagRepository.save(tag);

        return new TagResponseDTO(tag.getId(), tag.getName());
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        List<TagEntity> tags = tagRepository.findAll();

        return tags.stream()
                .map(tag -> new TagResponseDTO(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }
}
