package org.example.rekollectapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RecordRequestDTO {

    @NotBlank(message = "Title is required.")
    private String title;

    @NotBlank(message = "Description is required.")
    private String description;

    private String coverImageUrl;
    private String onlineLink;

    @NotBlank(message = "Category is required.")
    private String categoryName;

    @Size(min = 1, message = "At least one creator is required.")
    private List<@Valid CreatorRequestDTO> creators;

    @Size(min = 1, message = "At least one tag is required.")
    private List<String> tags;
}

