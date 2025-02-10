package org.example.rekollectapi.models.ids;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordCreatorId implements Serializable {
    private UUID recordId;
    private UUID creatorId;
    private Long roleId;
}
