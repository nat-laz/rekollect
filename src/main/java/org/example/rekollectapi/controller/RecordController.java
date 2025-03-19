package org.example.rekollectapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.request.RecordUpdateDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.service.RecordService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/records")
    public ResponseEntity<RecordResponseDTO> createRecord(
            @Valid @RequestBody RecordRequestDTO request,
            @RequestHeader(value = "X-User-Id", required = false) UUID authenticatedUserId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recordService.createRecord(request, authenticatedUserId));
    }

    @GetMapping("/records/{recordId}")
    public ResponseEntity<RecordResponseDTO> getRecord(@PathVariable("recordId") UUID recordId) {
        return ResponseEntity.ok(recordService.getRecordById(recordId));
    }

    @GetMapping("/records")
    public ResponseEntity<Page<RecordResponseDTO>> getDefaultRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<RecordResponseDTO> records = recordService.getDefaultRecords(page, size, sortField, sortDirection);
        return ResponseEntity.ok(records);
    }


    // TODO: auth
    // @RequestHeader(value = "X-User-Id", required = false) UUID authenticatedUserId
    @PatchMapping("/records/{recordId}")
    public ResponseEntity<RecordResponseDTO> updateRecord(
            @PathVariable("recordId") UUID recordId,
            @RequestBody RecordUpdateDTO request) {

        return ResponseEntity.ok(recordService.updateRecord(recordId, request));
    }

}
