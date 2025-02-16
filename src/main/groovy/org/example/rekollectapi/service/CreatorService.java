package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;

import java.util.UUID;

public interface CreatorService {

    CreatorResponseDTO addCreator(CreatorRequestDTO creatorRequestDTO);

    CreatorResponseDTO updateCreator(UUID creatorId, CreatorRequestDTO creatorRequestDTO);
}
