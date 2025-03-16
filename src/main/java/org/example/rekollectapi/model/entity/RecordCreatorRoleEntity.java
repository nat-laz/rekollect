package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.rekollectapi.model.ids.RecordCreatorRoleId;

@Entity
@Table(name = "record_creator_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecordCreatorRoleEntity {

    @EmbeddedId // Composite Key
    private RecordCreatorRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recordId")
    @JoinColumn(name = "record_id", nullable = false)
    private RecordEntity record;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("creatorId")
    @JoinColumn(name = "creator_id", nullable = false)
    private CreatorEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("creatorRoleId")
    @JoinColumn(name = "creator_role_id", nullable = false)
    private CreatorRoleEntity role;
}