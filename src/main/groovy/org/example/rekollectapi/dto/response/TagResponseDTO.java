package org.example.rekollectapi.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagResponseDTO {
    private Long id;
    private String tagName;

}
