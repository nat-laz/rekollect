package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.TagRequestDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;

import java.util.List;

public interface TagService {

   TagResponseDTO createTag(TagRequestDTO tagRequestDTO);

   List<TagResponseDTO> getAllTags();
}
