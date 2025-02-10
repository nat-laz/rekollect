package org.example.rekollectapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "creator_role")
@Getter
@Setter
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "role_id", updatable = false, nullable = false)
    private Integer roleId;

    @Column(nullable = false, unique = true)
    private String name;
}
