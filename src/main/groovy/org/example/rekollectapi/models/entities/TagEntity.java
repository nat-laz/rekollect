package org.example.rekollectapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tag")
@Getter
@Setter
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-incremented ID
    @Column(name = "tag_id")
    private Integer tagId;

    @Column(nullable = false, unique = true)
    private String name;
}