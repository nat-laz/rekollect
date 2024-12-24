package org.example.rekollectapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "records")
@Setter
@Getter
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Generates  as a Universally Unique Identifier (UUID).
    @Column(name = "record_id")
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(name = "cover_image")
    private byte[] coverImage; // store img as binary data

    @Column(nullable = false)
    private String description;

    @Column
    private String link;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // Foreign key to categories table
    private CategoryEntity category;

    @Column
    private String creator;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Called automatically by JPA when a new entity is being persisted for the first time.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Called automatically by JPA when an existing entity is being updated.
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
