package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;
import org.example.rekollectapi.model.entity.CreatorEntity;
import org.example.rekollectapi.model.entity.CreatorRoleEntity;
import org.example.rekollectapi.model.entity.RecordEntity;

import java.util.List;
import java.util.UUID;

public interface CreatorService {

    List<CreatorResponseDTO> processCreatorsAndRoles(List<CreatorRequestDTO> creatorRequests, RecordEntity record);

    CreatorEntity getOrCreateCreator(CreatorRequestDTO creatorReq);

}
