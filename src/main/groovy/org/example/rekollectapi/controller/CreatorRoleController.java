package org.example.rekollectapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.CreatorRoleRequestDTO;
import org.example.rekollectapi.dto.response.CreatorRoleResponseDTO;
import org.example.rekollectapi.service.CreatorRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roles")
@RequiredArgsConstructor
public class CreatorRoleController {

    private final CreatorRoleService roleService;

    @PostMapping
    public ResponseEntity<CreatorRoleResponseDTO> createRole(@Valid @RequestBody CreatorRoleRequestDTO request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    // TODO: remove after implementing FE
    @GetMapping("/{id}")
    public ResponseEntity<CreatorRoleResponseDTO> getRoleById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }
}
