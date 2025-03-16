package org.example.rekollectapi.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreatorRoleResponseDTO {
    private Long id;
    private String name;
}
