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

    private final CreatorRoleRepository roleRepository;

    @Override
    public CreatorRoleResponseDTO createRole(CreatorRoleRequestDTO request) {
        if (roleRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Role with name " + request.getName() + " already exists");
        }

        CreatorRoleEntity role = new CreatorRoleEntity();
        role.setName(request.getName());
        roleRepository.save(role);

        return new CreatorRoleResponseDTO(role.getId(), role.getName());
    }

    @Override
    public CreatorRoleResponseDTO getRoleById(Integer id) {
        CreatorRoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return new CreatorRoleResponseDTO(role.getId(), role.getName());
    }
}
