package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "creator", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"creator_first_name", "creator_last_name"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "creator_id")
    private UUID id;

    @Column(name = "creator_first_name", nullable = false)
    private String creatorFirstName;

    @Column(name = "creator_last_name", nullable = false)
    private String creatorLastName;

    @Column(name = "creator_bio", columnDefinition = "TEXT")
    private String creatorBio;
}
