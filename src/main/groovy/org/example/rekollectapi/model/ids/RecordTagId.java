package org.example.rekollectapi.model.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecordTagId implements Serializable {

    @Column(name = "record_id")
    private UUID recordId;

    @Column(name = "tag_id")
    private Long tagId;
}
