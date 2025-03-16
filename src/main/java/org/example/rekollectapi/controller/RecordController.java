package org.example.rekollectapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.request.RecordUpdateDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ResponseEntity<RecordResponseDTO> createRecord(
            @Valid @RequestBody RecordRequestDTO request,
            @RequestHeader(value = "X-User-Id", required = false) UUID authenticatedUserId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recordService.createRecord(request, authenticatedUserId));
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordResponseDTO> getRecord(@PathVariable("recordId") UUID recordId) {
        return ResponseEntity.ok(recordService.getRecordById(recordId));
    }


    // TODO:
    // @RequestHeader(value = "X-User-Id", required = false) UUID authenticatedUserId
    @PatchMapping("/{recordId}")
    public ResponseEntity<RecordResponseDTO> updateRecord(
            @PathVariable("recordId") UUID recordId,
            @RequestBody RecordUpdateDTO request
           ) {

        return ResponseEntity.ok(recordService.updateRecord(recordId, request));
    }

}
