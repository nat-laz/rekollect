package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;
import org.example.rekollectapi.exceptions.ValidationException;
import org.example.rekollectapi.model.entity.CreatorEntity;
import org.example.rekollectapi.model.entity.CreatorRoleEntity;
import org.example.rekollectapi.model.entity.RecordCreatorRoleEntity;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.ids.RecordCreatorRoleId;
import org.example.rekollectapi.repository.CreatorRepository;
import org.example.rekollectapi.repository.RecordCreatorRoleRepository;
import org.example.rekollectapi.service.CreatorRoleService;
import org.example.rekollectapi.service.CreatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;
    private final RecordCreatorRoleRepository recordCreatorRoleRepository;
    private final CreatorRoleService creatorRoleService;

    @Override
    public List<CreatorResponseDTO> processCreatorsAndRoles(List<CreatorRequestDTO> creatorRequests, RecordEntity record) {
        if (creatorRequests == null || creatorRequests.isEmpty()) {
            throw new ValidationException("At least one creator is required.");
        }

        List<CreatorEntity> creators = new ArrayList<>();
        List<CreatorRoleEntity> roles = new ArrayList<>();
        List<CreatorResponseDTO> creatorResponses = new ArrayList<>();
        List<RecordCreatorRoleEntity> recordCreators = new ArrayList<>();

        for (CreatorRequestDTO creatorReq : creatorRequests) {
            if (creatorReq.getCreatorFirstName() == null || creatorReq.getCreatorFirstName().trim().isEmpty() ||
                    creatorReq.getCreatorLastName() == null || creatorReq.getCreatorLastName().trim().isEmpty() ||
                    creatorReq.getCreatorRole() == null || creatorReq.getCreatorRole().trim().isEmpty()) {
                throw new ValidationException("Each creator must have a first name, last name, and role.");
            }

            CreatorEntity creator = getOrCreateCreator(creatorReq);
            CreatorRoleEntity role = creatorRoleService.getOrCreateRole(creatorReq.getCreatorRole());

            // Check if this creator-role combination already exists for this record: COMPOSITE-KEY
            RecordCreatorRoleId recordCreatorRoleId = new RecordCreatorRoleId(record.getId(), creator.getId(), role.getId());
            if (recordCreatorRoleRepository.existsById(recordCreatorRoleId)) {
                throw new ValidationException("Duplicate creator-role entry detected.");
            }

            recordCreators.add(new RecordCreatorRoleEntity(recordCreatorRoleId, record, creator, role));

            creatorResponses.add(new CreatorResponseDTO(
                    creator.getId(),
                    creator.getCreatorFirstName(),
                    creator.getCreatorLastName(),
                    creator.getCreatorBio(),
                    role.getRoleName()
            ));
        }

        // Batch insert
        recordCreatorRoleRepository.saveAll(recordCreators);

        return creatorResponses;
    }

    @Override
    public CreatorEntity getOrCreateCreator(CreatorRequestDTO creatorReq) {
        return creatorRepository.findByCreatorFirstNameAndCreatorLastName(
                        creatorReq.getCreatorFirstName(), creatorReq.getCreatorLastName())
                .orElseGet(() -> creatorRepository.save(
                        new CreatorEntity(null, creatorReq.getCreatorFirstName(), creatorReq.getCreatorLastName(), creatorReq.getCreatorBio())
                ));
    }

}
