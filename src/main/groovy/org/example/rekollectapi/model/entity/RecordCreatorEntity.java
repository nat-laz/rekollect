package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.rekollectapi.model.ids.RecordCreatorId;

@Entity
@Table(name = "record_creator")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecordCreatorEntity {

    @EmbeddedId // Composite Key
    private RecordCreatorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recordId")
    @JoinColumn(name = "record_id", nullable = false)
    private RecordEntity record;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("creatorId")
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;
}