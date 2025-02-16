package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.RoleRequestDTO;
import org.example.rekollectapi.dto.response.RoleResponseDTO;

public interface RoleService {

    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);

    RoleResponseDTO getRoleById(Integer id);
}
