package org.example.rekollectapi.service;


import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.index.RecordIndex;

import java.util.List;

public interface ElasticsearchRecordService {

    void saveRecordToElasticsearch(RecordEntity record, List<CreatorWithRoleResponseDTO> creators, List<TagResponseDTO> tags);

    List<RecordIndex> searchRecords(String query);

    void syncDatabaseWithElasticsearch();

    void deleteRecordFromElasticsearch(String recordId);
}
