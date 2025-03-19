package org.example.rekollectapi.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    Logger log = LoggerFactory.getLogger(ElasticsearchRecordServiceImpl.class);


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


    @Override
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
    public List<RecordIndex> filterRecords(String category, List<String> tags, String creator, int page, int size, String sortField, String sortOrder) {
        try {
            // debug
            log.info("  Filtering records in Elasticsearch with parameters:");
            log.info("   - Category: {}", category);
            log.info("   - Tags: {}", tags);
            log.info("   - Creator: {}", creator);
            log.info("   - Page: {}, Size: {}", page, size);
            log.info("   - Sort Field: {}, Sort Order: {}", sortField, sortOrder);

            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

            if (category != null) {
                log.info(" Adding category filter: {}", category);
                boolQueryBuilder.must(m -> m.match(t -> t.field("category").query(category)));
            }

            if (tags != null && !tags.isEmpty()) {
                log.info(" Adding tag filters: {}", tags);
                boolQueryBuilder.must(m -> m.terms(t -> t
                        .field("tags")
                        .terms(ts -> ts.value(tags.stream().map(FieldValue::of).collect(Collectors.toList())))));
            }

            if (creator != null) {
                log.info(" Adding creator filter: {}", creator);
                boolQueryBuilder.must(n -> n
                        .nested(nq -> nq
                                .path("creators")
                                .query(nq2 -> nq2
                                        .bool(creatorBool -> creatorBool
                                                .should(s -> s.match(t -> t.field("creators.firstName").query(creator)))
                                                .should(s -> s.match(t -> t.field("creators.lastName").query(creator)))
                                        )
                                )
                        )
                );
            }

            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("records")
                    .size(size)
                    .from(page * size)
                    .query(q -> q.bool(boolQueryBuilder.build()))
                    .sort(sort -> sort.field(f -> f
                            .field(sortField != null ? sortField : "createdAt")
                            .order(sortOrder != null && sortOrder.equalsIgnoreCase("asc") ? SortOrder.Asc : SortOrder.Desc)
                    ))
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonQuery = objectMapper.writeValueAsString(searchRequest);
            log.info("Constructed Elasticsearch Query: {}", jsonQuery);

            SearchResponse<RecordIndex> searchResponse = elasticsearchClient.search(searchRequest, RecordIndex.class);

            log.info(" Elasticsearch query executed successfully. Total hits: {}", searchResponse.hits().total().value());

            List<RecordIndex> results = searchResponse.hits().hits().stream()
                    .map(hit -> hit.source())
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            log.info("Returning {} filtered records", results.size());
            return results;

        } catch (Exception e) {
            log.error("Error filtering Elasticsearch records: {}", e.getMessage(), e);
            throw new RuntimeException("Error filtering Elasticsearch records", e);
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
