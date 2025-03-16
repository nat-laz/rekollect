package org.example.rekollectapi.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.example.rekollectapi.dto.response.CreatorWithRoleResponseDTO;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.index.CreatorIndex;
import org.example.rekollectapi.model.index.RecordIndex;
import org.example.rekollectapi.repository.RecordCreatorRoleRepository;
import org.example.rekollectapi.repository.RecordElasticsearchRepository;
import org.example.rekollectapi.repository.RecordRepository;
import org.example.rekollectapi.service.ElasticsearchRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchRecordServiceImpl implements ElasticsearchRecordService {

    private final RecordElasticsearchRepository elasticsearchRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final RecordRepository recordRepository;
    private final RecordCreatorRoleRepository recordCreatorRoleRepository;


    @Override
    public void saveRecordToElasticsearch(RecordEntity record, List<CreatorWithRoleResponseDTO> creators, List<TagResponseDTO> tags) {
        RecordIndex recordIndex = new RecordIndex();
        recordIndex.setId(record.getId().toString());
        recordIndex.setTitle(record.getTitle());
        recordIndex.setDescription(record.getDescription());
        recordIndex.setCategory(record.getCategory().getCategoryName());

        recordIndex.setReleaseDate(record.getReleaseDate() != null ? record.getReleaseDate().toString() : null);
        recordIndex.setCreatedAt(record.getCreatedAt());
        recordIndex.setUpdatedAt(record.getUpdatedAt());

        //  Convert tags
        recordIndex.setTags(tags.stream()
                .map(TagResponseDTO::getTagName)
                .collect(Collectors.toList()));

        // Convert creators
        List<CreatorIndex> creatorIndexes = creators.stream().map(creator -> {
            CreatorIndex ci = new CreatorIndex();
            ci.setFirstName(creator.getCreatorFirstName());
            ci.setLastName(creator.getCreatorLastName());
            ci.setRole(creator.getCreatorRole());
            return ci;
        }).collect(Collectors.toList());

        recordIndex.setCreators(creatorIndexes);

        elasticsearchRepository.save(recordIndex);
    }


    public List<RecordIndex> searchRecords(String query) {
        try {
            SearchResponse<RecordIndex> searchResponse = elasticsearchClient.search(s -> s
                    .index("records")
                    .query(q -> q
                            .bool(b -> b
                                    .should(m -> m
                                            .multiMatch(mm -> mm
                                                    .query(query)
                                                    .fields(List.of("title^3", "description^2", "category", "tags"))
                                                    .fuzziness("AUTO") // fuzziness: "AUTO" => allow typo tolerance
                                            )
                                    )
                                    .should(nq -> nq
                                            .nested(n -> n
                                                    .path("creators")
                                                    .query(nq2 -> nq2
                                                            .multiMatch(mm -> mm
                                                                    .query(query)
                                                                    .fields(List.of("creators.firstName", "creators.lastName", "creators.role"))
                                                                    .fuzziness("AUTO")
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ), RecordIndex.class);

            //  debug
            System.out.println("ES Response: " + searchResponse.toString());

            return searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())  // extract actual records
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error searching Elasticsearch", e);
        }
    }


    @Override
    public void syncDatabaseWithElasticsearch() {
        List<RecordEntity> allRecords = recordRepository.findAll();

        for (RecordEntity record : allRecords) {

            // 1. Fetch related data
            List<CreatorWithRoleResponseDTO> creators = recordCreatorRoleRepository.findByRecordId(record.getId())
                    .stream()
                    .map(recordCreatorRole -> new CreatorWithRoleResponseDTO(
                            recordCreatorRole.getCreator().getId(),
                            recordCreatorRole.getCreator().getCreatorFirstName(),
                            recordCreatorRole.getCreator().getCreatorLastName(),
                            recordCreatorRole.getCreator().getCreatorBio(),
                            recordCreatorRole.getRole().getRoleName()
                    ))
                    .collect(Collectors.toList());

            List<TagResponseDTO> tags = record.getTags()
                    .stream()
                    .map(tag -> new TagResponseDTO(tag.getId(), tag.getTagName()))
                    .collect(Collectors.toList());

            // 2. Index record in Elasticsearch
            saveRecordToElasticsearch(record, creators, tags);
        }

        System.out.println("Database records successfully indexed in Elasticsearch.");
    }

    @Override
    public void deleteRecordFromElasticsearch(String recordId) {
        elasticsearchRepository.deleteById(recordId);
    }

}
