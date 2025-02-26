package org.example.rekollectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorRequestDTO {

    @NotBlank(message = "First name cannot be blank")
    @Size(min = 1, max = 255)
    private String creatorFirstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 1, max = 255)
    private String creatorLastName;

    private String creatorBio;

    @NotBlank(message = "Role is required")
    private String creatorRole;
}
