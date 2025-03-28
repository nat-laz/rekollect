package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@NoArgsConstructor // Required by JPA
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false , unique = true)
    private String categoryName;
}
