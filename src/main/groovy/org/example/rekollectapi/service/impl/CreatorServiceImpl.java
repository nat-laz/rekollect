package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;
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
    private final RecordCreatorRoleRepository recordCreatorRepository;
    private final CreatorRoleService creatorRoleService;

    @Override
    public List<CreatorResponseDTO> processCreatorsAndRoles(List<CreatorRequestDTO> creatorRequests, RecordEntity record) {
        List<CreatorEntity> creators = new ArrayList<>();
        List<CreatorRoleEntity> roles = new ArrayList<>();
        List<CreatorResponseDTO> creatorResponses = new ArrayList<>();

        for (CreatorRequestDTO creatorReq : creatorRequests) {
            CreatorEntity creator = getOrCreateCreator(creatorReq);
            CreatorRoleEntity role = creatorRoleService.getOrCreateRole(creatorReq.getCreatorRole());

            creators.add(creator);
            roles.add(role);
        }

        // Batch save relationships
        List<RecordCreatorRoleEntity> recordCreators = new ArrayList<>();
        for (int i = 0; i < creators.size(); i++) {
            recordCreators.add(new RecordCreatorRoleEntity(
                    new RecordCreatorRoleId(record.getId(), creators.get(i).getId(), roles.get(i).getId()),
                    record, creators.get(i), roles.get(i)
            ));

            creatorResponses.add(new CreatorResponseDTO(
                    creators.get(i).getId(),
                    creators.get(i).getCreatorFirstName(),
                    creators.get(i).getCreatorLastName(),
                    creators.get(i).getCreatorBio(),
                    roles.get(i).getRoleName()
            ));
        }
        // Batch insert
        recordCreatorRepository.saveAll(recordCreators);

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
