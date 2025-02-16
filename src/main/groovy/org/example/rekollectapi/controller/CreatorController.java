package org.example.rekollectapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.CreatorRequestDTO;
import org.example.rekollectapi.dto.response.CreatorResponseDTO;
import org.example.rekollectapi.service.CreatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/creators")
@RequiredArgsConstructor
public class CreatorController {

    private final CreatorService creatorService;

    @PostMapping
    public ResponseEntity<CreatorResponseDTO> addCreator(@Valid @RequestBody CreatorRequestDTO request) {
        return ResponseEntity.ok(creatorService.addCreator(request));
    }

    @PutMapping("/{creatorId}")
    public ResponseEntity<CreatorResponseDTO> updateCreator(
            @PathVariable("creatorId") UUID creatorId,
            @RequestBody CreatorRequestDTO request) {
        return ResponseEntity.ok(creatorService.updateCreator(creatorId, request));
    }

    // TODO: Get all records by a creator: GET /creators/{creatorId}/records

}
