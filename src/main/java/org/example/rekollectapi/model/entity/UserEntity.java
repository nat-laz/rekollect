package org.example.rekollectapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users") // "user" is a reserved SQL keyword
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID id; // (default JPA convention)

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // encryption

    @Column(nullable = false, unique = true)
    private String email;
}
