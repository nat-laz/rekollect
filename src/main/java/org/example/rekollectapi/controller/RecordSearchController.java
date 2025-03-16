package org.example.rekollectapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.model.index.RecordIndex;
import org.example.rekollectapi.repository.RecordElasticsearchRepository;
import org.example.rekollectapi.service.ElasticsearchRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordSearchController {

    private final ElasticsearchRecordService elasticsearchRecordService;

    @GetMapping("/search")
    public List<RecordIndex> searchRecords(@RequestParam String query) {
        return elasticsearchRecordService.searchRecords(query);
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