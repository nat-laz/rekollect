package org.example.rekollectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorRequestDTO {

    @NotBlank(message = "Tag name cannot be blank")
    @Size(min = 1, max = 255)
    private String creatorFirstName;

    @NotBlank(message = "Tag name cannot be blank")
    @Size(min = 1, max = 255)
    private String creatorLastName;


    private String creatorBio;
}
