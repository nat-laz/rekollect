package org.example.rekollectapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreatorRoleRequestDTO {

    @NotBlank(message = "Role name cannot be blank")
    @Size(min = 1, max = 255)
    private String name;
}
