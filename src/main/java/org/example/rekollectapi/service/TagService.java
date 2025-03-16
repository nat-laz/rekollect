package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.RecordEntity;

import java.util.List;

public interface TagService {

    List<TagResponseDTO> processTags(List<String> tagNames, RecordEntity record);
}
