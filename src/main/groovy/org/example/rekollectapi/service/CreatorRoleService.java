package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.CreatorRoleRequestDTO;
import org.example.rekollectapi.dto.response.CreatorRoleResponseDTO;
import org.example.rekollectapi.model.entity.CreatorRoleEntity;

public interface CreatorRoleService {

    CreatorRoleEntity getOrCreateRole(String roleName);

}
