package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.rekollectapi.model.ids.RecordTagId;

@Entity
@Table(name = "record_tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecordTagEntity {

    @EmbeddedId // composite keys
    private RecordTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recordId")
    @JoinColumn(name = "record_id", nullable = false)
    private RecordEntity record;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;
}
