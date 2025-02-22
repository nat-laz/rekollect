package org.example.rekollectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecordRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String coverImageUrl;
    private String onlineLink;

    @NotBlank(message = "Category is required")
    private String categoryName;

    private List<CreatorRequestDTO> creators;

    private List<String> tags;
}

