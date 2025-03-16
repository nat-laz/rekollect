package org.example.rekollectapi.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordUpdateDTO {

    private String title;
    private String description;
    private String coverImageUrl;
    private String onlineLink;
    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Release date cannot be in the future.")
    private String releaseDate;

    private List<@Valid CreatorRequestDTO> creators;
    private List<String> tags;
}
