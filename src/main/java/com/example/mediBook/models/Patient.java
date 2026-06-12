package com.example.mediBook.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patients")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // الحساب الذي يحتوي على رقم الهاتف والباسورد
}
