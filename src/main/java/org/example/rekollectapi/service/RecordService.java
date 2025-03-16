package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.request.RecordUpdateDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;

import java.util.UUID;

public interface RecordService {

    RecordResponseDTO createRecord(RecordRequestDTO recordRequestDTO, UUID authenticatedUserId);

    RecordResponseDTO getRecordById(UUID recordId);

    RecordResponseDTO updateRecord(UUID recordId, RecordUpdateDTO updateDTO);
}
