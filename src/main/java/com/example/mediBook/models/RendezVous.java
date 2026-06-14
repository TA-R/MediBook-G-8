package com.example.mediBook.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "rendez_vous")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RendezVous {
    public enum Statut {
        EN_ATTENTE, CONFIRME, ANNULE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // تاريخ الموعد (YYYY-MM-DD)

    @Column(nullable = false)
    private LocalTime heure; // وقت الموعد (HH:MM)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Statut statut = Statut.EN_ATTENTE;
     // حالة الموعد: CONFIRME, ANNULE, EN_ATTENTE
     private String motif;
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient; // المريض الذي حجز الموعد

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin; // الطبيب المراد زيارته
}
