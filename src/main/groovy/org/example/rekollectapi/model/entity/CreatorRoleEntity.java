package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "creator_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatorRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremented ID
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String name;
}
