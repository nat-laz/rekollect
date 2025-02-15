package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "creator")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "creator_id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
