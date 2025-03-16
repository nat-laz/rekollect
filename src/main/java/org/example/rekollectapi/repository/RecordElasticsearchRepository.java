package org.example.rekollectapi.repository;

import org.example.rekollectapi.model.index.RecordIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordElasticsearchRepository extends ElasticsearchRepository<RecordIndex, String> {

    List<RecordIndex> findByTitleContainingIgnoreCase(String title);
}