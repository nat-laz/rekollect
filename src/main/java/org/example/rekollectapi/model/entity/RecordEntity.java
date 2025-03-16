package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "record")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class RecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Generates  as a Universally Unique Identifier (UUID).
    @Column(name = "record_id", updatable = false, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image")
    private String coverImageUrl; // Use Google Drive URL

    @Column(name = "online_link")
    private String onlineLink;

    // =========== relation between record and category ===========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false) // Foreign key to category table
    private CategoryEntity category;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @CreationTimestamp // No need for manual @PrePersist method
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp // No need for manual  @PreUpdate method
    @Column(name = "updated_at")
    private Instant updatedAt;

    // =========== relation between record and tag ===========
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "record_tags",
            joinColumns = @JoinColumn(name = "record_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<TagEntity> tags = new HashSet<>();

    // =========== relation between record and comments ===========
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentEntity> comments;

    // =========== relation between record and creator ===========
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecordCreatorRoleEntity> creators;

    // =========== relation between record and user ===========
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


}
