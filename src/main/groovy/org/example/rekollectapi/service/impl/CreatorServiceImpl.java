package org.example.rekollectapi.service.impl;

import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;
import org.example.rekollectapi.exceptions.ResourceNotFoundException;
import org.example.rekollectapi.model.entity.CreatorEntity;
import org.example.rekollectapi.repository.CreatorRepository;
import org.example.rekollectapi.service.CreatorService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CreatorServiceImpl implements CreatorService {

    private final CreatorRepository creatorRepository;

    @Override
    public CreatorResponseDTO addCreator(CreatorRequestDTO request) {

        // Check if the creator already exists
        Optional<CreatorEntity> existingCreator = creatorRepository
                .findByCreatorFirstNameAndCreatorLastName(request.getCreatorFirstName(), request.getCreatorLastName());

        if (existingCreator.isPresent()) {
            throw new RuntimeException("Creator already exists!");
        }

        CreatorEntity creator = new CreatorEntity();
        creator.setCreatorFirstName(request.getCreatorFirstName());
        creator.setCreatorLastName(request.getCreatorLastName());
        creator.setCreatorBio(request.getCreatorBio() == null || request.getCreatorBio().isBlank() ? null : request.getCreatorBio());


        CreatorEntity savedCreator = creatorRepository.save(creator);

        return new CreatorResponseDTO(
                savedCreator.getId(),
                savedCreator.getCreatorFirstName(),
                savedCreator.getCreatorLastName(),
                savedCreator.getCreatorBio()
        );
    }

    @Override
    public CreatorResponseDTO updateCreator(UUID creatorId, CreatorRequestDTO request) {

        // Find the existing creator
        CreatorEntity creator = creatorRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator with ID " + creatorId + " not found."));

        // Update only provided fields
        if (request.getCreatorFirstName() != null && !request.getCreatorFirstName().isBlank()) {
            creator.setCreatorFirstName(request.getCreatorFirstName());
        }

        if (request.getCreatorLastName() != null && !request.getCreatorLastName().isBlank()) {
            creator.setCreatorLastName(request.getCreatorLastName());
        }

        if (request.getCreatorBio() != null && !request.getCreatorBio().isBlank()) {
            creator.setCreatorBio(request.getCreatorBio());
        }

        CreatorEntity updatedCreator = creatorRepository.save(creator);

        return new CreatorResponseDTO(
                updatedCreator.getId(),
                updatedCreator.getCreatorFirstName(),
                updatedCreator.getCreatorLastName(),
                updatedCreator.getCreatorBio()
        );
    }
}
