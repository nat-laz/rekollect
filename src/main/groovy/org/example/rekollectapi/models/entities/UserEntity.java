package org.example.rekollectapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users") // "user" is a reserved SQL keyword
@Getter
@Setter
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
