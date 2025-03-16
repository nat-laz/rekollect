package org.example.rekollectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorWithRoleResponseDTO {
    private UUID id;
    private String creatorFirstName;
    private String creatorLastName;
    private String creatorBio;
    private String creatorRole;
}
