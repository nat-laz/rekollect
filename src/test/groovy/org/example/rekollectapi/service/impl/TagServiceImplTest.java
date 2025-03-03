package org.example.rekollectapi.service.impl;

import org.assertj.core.api.Assertions;
import org.example.rekollectapi.dto.response.TagResponseDTO;
import org.example.rekollectapi.model.entity.RecordEntity;
import org.example.rekollectapi.model.entity.TagEntity;
import org.example.rekollectapi.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    @DisplayName("fetchExistingTags should return stored tags from repository")
    void TagServiceImpl_GetAllTags_ReturnsAllTags() {

        // Arrange
        TagEntity tag1 = TagEntity.builder().tagName("test tag1").build();
        TagEntity tag2 = TagEntity.builder().tagName("test tag2").build();

        Set<String> tagNames = Set.of("test tag1", "test tag2");

        when(tagRepository.findAllByTagNameIn(tagNames)).thenReturn(List.of(tag1, tag2));

        // Act
        List<TagEntity> savedTagList = tagService.fetchExistingTags(tagNames);

        // Assert
        Assertions.assertThat(savedTagList).isNotNull();
        Assertions.assertThat(savedTagList.size()).isEqualTo(2);

        verify(tagRepository, times(1)).findAllByTagNameIn(tagNames);
    }

    @Test
    @DisplayName("saveNewTags should save only new tags and return them")
    void TagServiceImpl_MapNewTagsWithExistingOne_ReturnsAllSavedNewTags() {

        // Arrange
        Set<String> tagNames = Set.of("tag1", "tag2", "new-tag");
        List<TagEntity> existingTags = List.of(new TagEntity(1L, "tag1"), new TagEntity(2L, "tag2"));

        List<TagEntity> newTags = List.of(new TagEntity(null, "new-tag"));

        when(tagRepository.saveAll(anyList())).thenReturn(newTags);

        // Act
        List<TagEntity> savedNewTags = tagService.saveNewTags(tagNames, existingTags);

        // Assert
        Assertions.assertThat(savedNewTags).hasSize(1);
        Assertions.assertThat(savedNewTags.get(0).getTagName()).isEqualTo("new-tag");
        verify(tagRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("saveNewTags should return empty list when all tags exist")
    void TagServiceImpl_MapNewTagsWithExistingOne_ReturnsEmptyWhenNoNewTags() {

        // Arrange
        Set<String> newTagNames = Set.of("tag1", "tag2");
        List<TagEntity> existingTags = List.of(new TagEntity(1L, "tag1"), new TagEntity(2L, "tag2"));

        // Act
        List<TagEntity> savedNewTags = tagService.saveNewTags(newTagNames, existingTags);

        // Assert
        Assertions.assertThat(savedNewTags).isEmpty();
        verify(tagRepository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("linkTagsToRecord should correctly update record tags")
    void TagServiceImpl_linkTagsToRecord_ShouldUpdateTags() {

        // Arrange
        RecordEntity record = new RecordEntity();
        record.setTags(null);

        List<TagEntity> existingTags = List.of(new TagEntity(1L, "tag1"));
        List<TagEntity> newTags = List.of(new TagEntity(2L, "new-tag"));

        // Act
        tagService.linkTagsToRecord(record, existingTags, newTags);

        // Assert
        Assertions.assertThat(record.getTags()).isNotNull();
        Assertions.assertThat(record.getTags()).hasSize(2);
        Assertions.assertThat(record.getTags()).extracting(TagEntity::getTagName)
                .containsExactlyInAnyOrder("tag1", "new-tag");
    }

    @Test
    @DisplayName("processTags should return correct DTOs for existing and new tags")
    void TagServiceImpl_processTags_ValidTags_ReturnsDTOs() {

        // Arrange
        RecordEntity record = new RecordEntity();
        record.setTags(new HashSet<>());

        List<String> inputTagNames = Set.of("tag1", "tag2", "new-tag").stream().toList();

        List<TagEntity> existingTags = List.of(new TagEntity(1L, "tag1"), new TagEntity(2L, "tag2"));
        List<TagEntity> newTags = List.of(new TagEntity(3L, "new-tag"));

        when(tagRepository.findAllByTagNameIn(anySet())).thenReturn(existingTags);
        when(tagRepository.saveAll(anyList())).thenReturn(newTags);

        // Act
        List<TagResponseDTO> result = tagService.processTags(inputTagNames, record);

        // Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(3);
        Assertions.assertThat(result).extracting(TagResponseDTO::getTagName)
                .containsExactlyInAnyOrder("tag1", "tag2", "new-tag");

        verify(tagRepository, times(1)).findAllByTagNameIn(anySet());
        verify(tagRepository, times(1)).saveAll(anyList());
    }
}