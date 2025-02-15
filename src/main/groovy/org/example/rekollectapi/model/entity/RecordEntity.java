package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "record")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Generates  as a Universally Unique Identifier (UUID).
    @Column(name = "record_id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "cover_image")
    private String coverImageUrl; // Use Google Drive URL

    @Column(nullable = false)
    private String description;

    @Column
    private String online_link;

    // =========== relation between record and category ===========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false) // Foreign key to category table
    private CategoryEntity category;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // =========== relation between record and tag ===========
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "record_tags",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags;

    // =========== relation between record and comments ===========
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    // =========== relation between record and creator ===========
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecordCreatorEntity> creators;

    // =========== relation between record and user ===========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


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
