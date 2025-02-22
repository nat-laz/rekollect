package org.example.rekollectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RecordResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String coverImageUrl;
    private String onlineLink;
    private String category;
    private List<CreatorResponseDTO> creators;
    private List<TagResponseDTO> tags;
}
