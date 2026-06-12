package com.example.mediBook.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medecins")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;
    private String specialite; // التخصص (للإستخدام في خاصية البحث)

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
