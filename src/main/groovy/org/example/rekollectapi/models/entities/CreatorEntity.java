package org.example.rekollectapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "creator")
@Getter
@Setter
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "creator_id")
    private UUID creatorId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
