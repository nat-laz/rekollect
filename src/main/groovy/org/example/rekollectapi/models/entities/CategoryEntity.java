package org.example.rekollectapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Table(name = "category")
@Entity
@Setter
@Getter
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(nullable = false, unique = true)
    private String name;

    // One category has many records
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RecordEntity> records;
}
