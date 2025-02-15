package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "creator_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "role_id", updatable = false, nullable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
