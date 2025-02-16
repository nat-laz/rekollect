package org.example.rekollectapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class CreatorResponseDTO {
    private UUID id;
    private String creatorFirstName;
    private String creatorLastName;
    private String creatorBio;
}
