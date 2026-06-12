package com.example.mediBook.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // ADMIN, MEDECIN, PATIENT
}
