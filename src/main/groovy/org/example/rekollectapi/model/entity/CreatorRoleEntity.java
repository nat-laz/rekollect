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
    @Column(name = "creator_role_id")
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

}
