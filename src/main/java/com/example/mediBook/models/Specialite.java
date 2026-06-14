package com.example.mediBook.models;

import jakarta.persistence.*;

import lombok.*;


import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "specialites")
@NoArgsConstructor
@AllArgsConstructor
public class Specialite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "specialite")
    private List<Medecin> medecins;
}