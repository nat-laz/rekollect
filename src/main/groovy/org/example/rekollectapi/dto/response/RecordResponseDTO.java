package org.example.rekollectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RecordResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String coverImageUrl;
    private String onlineLink;
    private String category;
    private List<CreatorWithRoleResponseDTO> creators;
    private List<TagResponseDTO> tags;
}
