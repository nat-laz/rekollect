package org.example.rekollectapi.repository;

import org.assertj.core.api.Assertions;
import org.example.rekollectapi.model.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;


    @Test
    public void TagRepository_SaveAll_ReturnSavedTag() {

        // Arrange
        TagEntity tagEntity = TagEntity.builder()
                .tagName("test tag")
                .build();

        // Act
        TagEntity savedTag = tagRepository.save(tagEntity);

        // Assert
        Assertions.assertThat(savedTag).isNotNull();
        Assertions.assertThat(savedTag.getTagName()).isEqualTo(tagEntity.getTagName());
        Assertions.assertThat(savedTag.getId()).isGreaterThan(0);

    }

    @Test
    public void TagRepository_GetAll_ReturnMoreThanOneTag() {

        // Arrange
        TagEntity tag1 = TagEntity.builder().tagName("test tag1").build();
        TagEntity tag2 = TagEntity.builder().tagName("test tag2").build();

        tagRepository.save(tag1);
        tagRepository.save(tag2);

        // Act
        List<TagEntity> savedTags = tagRepository.findAll();

        // Assert
        Assertions.assertThat(savedTags).isNotNull();
        Assertions.assertThat(savedTags.size()).isEqualTo(2);
    }

    @Test
    public void TagRepository_FindAllByTagNameIn_ReturnTag() {

        // Arrange
        TagEntity tag1 = TagEntity.builder().tagName("test tag1").build();
        TagEntity tag2 = TagEntity.builder().tagName("test tag2").build();

        TagEntity savedTag1 = tagRepository.save(tag1);
        TagEntity savedTag2 = tagRepository.save(tag2);

        // Act
        List<TagEntity> savedTags = tagRepository.findAllByTagNameIn(List.of(savedTag1.getTagName(), savedTag2.getTagName()));

        // Assert
        Assertions.assertThat(savedTags).isNotNull();
        Assertions.assertThat(savedTags.size()).isEqualTo(2);
        Assertions.assertThat(savedTags.get(0).getTagName()).isEqualTo(tag1.getTagName());
        Assertions.assertThat(savedTags.get(1).getTagName()).isEqualTo(tag2.getTagName());
    }

    @Test
    public void TagRepository_SaveTag_ThrowsException_WhenTagNameIsDuplicate() {
        // Arrange
        TagEntity tag1 = TagEntity.builder().tagName("duplicate tag").build();
        TagEntity tag2 = TagEntity.builder().tagName("duplicate tag").build();

        tagRepository.save(tag1);

        // Act & Assert
        Assertions.assertThatThrownBy(() -> tagRepository.save(tag2))
                .isInstanceOf(Exception.class);
    }

    @Test
    public void TagRepository_FindAll_ReturnsEmptyList_WhenNoTagsExist() {
        // Act
        List<TagEntity> savedTags = tagRepository.findAll();

        // Assert
        Assertions.assertThat(savedTags).isNotNull();
        Assertions.assertThat(savedTags).isEmpty();
    }

    @Test
    public void TagRepository_FindAllByTagNameIn_ReturnEmptyList_WhenTagDoesNotExist() {

        // Act
        List<TagEntity> savedTags = tagRepository.findAllByTagNameIn(List.of("non-existent tag"));

        // Assert
        Assertions.assertThat(savedTags).isNotNull();
        Assertions.assertThat(savedTags).isEmpty();
    }

    @Test
    public void TagRepository_SaveTag_ThrowsException_WhenTagNameIsNull() {
        // Arrange
        TagEntity tagEntity = TagEntity.builder().tagName(null).build();

        // Act & Assert
        Assertions.assertThatThrownBy(() -> tagRepository.save(tagEntity))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void TagRepository_UpdateTagName_SuccessfullyUpdatesTag() {
        // Arrange
        TagEntity tag = TagEntity.builder().tagName("old name").build();
        TagEntity savedTag = tagRepository.save(tag);

        // Act
        savedTag.setTagName("new name");
        TagEntity updatedTag = tagRepository.save(savedTag);

        // Assert
        Assertions.assertThat(updatedTag.getTagName()).isNotNull();
        Assertions.assertThat(updatedTag.getTagName()).isEqualTo("new name");
    }


    @Test
    public void TagRepository_DeleteTag_RemovesTagFromDatabase() {
        // Arrange
        TagEntity tag = TagEntity.builder().tagName("test tag").build();
        TagEntity savedTag = tagRepository.save(tag);

        // Act
        tagRepository.deleteById(savedTag.getId());
        boolean exists = tagRepository.existsById(savedTag.getId());

        // Assert
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void TagRepository_DeleteNonExistentTag_NoExceptionThrown() {
        // Act & Assert
        Assertions.assertThatCode(() -> tagRepository.deleteById(999L))
                .doesNotThrowAnyException();
    }

}