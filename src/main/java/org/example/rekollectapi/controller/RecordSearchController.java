package org.example.rekollectapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.response.RecordResponseDTO;
import org.example.rekollectapi.model.index.RecordIndex;
import org.example.rekollectapi.service.ElasticsearchRecordService;
import org.example.rekollectapi.service.RecordService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordSearchController {

    private final ElasticsearchRecordService elasticsearchRecordService;
    private final RecordService recordService;


    @GetMapping("/search")
    public List<RecordIndex> searchRecords(@RequestParam String query) {
        return elasticsearchRecordService.searchRecords(query);
    }

    @GetMapping("/filter")
    public List<RecordResponseDTO> filterAndSortRecords(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String creator,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        return recordService.filterAndSortRecords(category, tags, creator, page, size, sortField, sortOrder);
    }


    @GetMapping("/sync")
    public String syncDatabaseWithElasticsearch() {
        try {
            elasticsearchRecordService.syncDatabaseWithElasticsearch();
            return "✅ Elasticsearch sync completed successfully!";
        } catch (Exception e) {
            return "❌ Elasticsearch sync failed: " + e.getMessage();
        }
    }
}