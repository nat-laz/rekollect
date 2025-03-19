package org.example.rekollectapi.service;

import org.example.rekollectapi.dto.request.RecordRequestDTO;
import org.example.rekollectapi.dto.request.RecordUpdateDTO;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RecordService {

    RecordResponseDTO createRecord(RecordRequestDTO recordRequestDTO, UUID authenticatedUserId);

    Page<RecordResponseDTO> getDefaultRecords(int page, int size, String sortField, String sortDirection);

    RecordResponseDTO getRecordById(UUID recordId);

    RecordResponseDTO updateRecord(UUID recordId, RecordUpdateDTO updateDTO);

    List<RecordResponseDTO> filterAndSortRecords(
            String category, List<String> tags, String creator, int page, int size, String sortField, String sortOrder
    );
}
