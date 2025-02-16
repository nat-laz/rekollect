package org.example.rekollectapi.service.impl;


import lombok.AllArgsConstructor;
import org.example.rekollectapi.dto.request.RoleRequestDTO;
import org.example.rekollectapi.dto.response.RoleResponseDTO;
import org.example.rekollectapi.exceptions.BadRequestException;
import org.example.rekollectapi.exceptions.ResourceNotFoundException;
import org.example.rekollectapi.model.entity.RoleEntity;
import org.example.rekollectapi.repository.RoleRepository;
import org.example.rekollectapi.service.RoleService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO request) {
        if (roleRepository.findByName(request.getName()).isPresent()) {
            throw new BadRequestException("Role with name " + request.getName() + " already exists");
        }

        RoleEntity role = new RoleEntity();
        role.setName(request.getName());
        roleRepository.save(role);

        return new RoleResponseDTO(role.getId(), role.getName());
    }

    @Override
    public RoleResponseDTO getRoleById(Integer id) {
        RoleEntity role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with ID: " + id));
        return new RoleResponseDTO(role.getId(), role.getName());
    }
}
