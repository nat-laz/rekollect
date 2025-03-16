package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.model.entity.CreatorEntity;
import org.example.rekollectapi.model.entity.RecordEntity;

import java.util.List;


public interface CreatorService {

    List<CreatorWithRoleResponseDTO> processCreatorsAndRoles(List<CreatorRequestDTO> creatorRequests, RecordEntity record);

    CreatorEntity getOrCreateCreator(CreatorRequestDTO creatorReq);

}
