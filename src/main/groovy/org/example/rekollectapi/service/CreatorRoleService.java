package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.CreatorRoleRequestDTO;
import org.example.rekollectapi.dto.response.CreatorRoleResponseDTO;

public interface CreatorRoleService {

    CreatorRoleResponseDTO createRole(CreatorRoleRequestDTO roleRequestDTO);

    CreatorRoleResponseDTO getRoleById(Integer id);
}
