package org.example.rekollectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagRequestDTO {

    @NotBlank(message = "Tag name cannot be blank")
    @Size(min = 1, max = 255)
    private String tagName;
}
