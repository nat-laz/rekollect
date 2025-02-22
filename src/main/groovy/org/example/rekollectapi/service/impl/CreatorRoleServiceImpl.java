package org.example.rekollectapi.service.impl;


import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.CreatorRoleRequestDTO;
import org.example.rekollectapi.dto.response.CreatorRoleResponseDTO;
import org.example.rekollectapi.exceptions.BadRequestException;
import org.example.rekollectapi.exceptions.ResourceNotFoundException;
import org.example.rekollectapi.model.entity.CreatorRoleEntity;
import org.example.rekollectapi.repository.CreatorRoleRepository;
import org.example.rekollectapi.service.CreatorRoleService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatorRoleServiceImpl implements CreatorRoleService {

    private final CreatorRoleRepository creatorRoleRepository;

    @Override
    public CreatorRoleEntity getOrCreateRole(String roleName) {
        return creatorRoleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> creatorRoleRepository.save(new CreatorRoleEntity(null, roleName)));
    }
}
