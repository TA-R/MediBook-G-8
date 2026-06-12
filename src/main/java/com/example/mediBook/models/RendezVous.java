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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date; // تاريخ الموعد (YYYY-MM-DD)

    @Column(nullable = false)
    private LocalTime heure; // وقت الموعد (HH:MM)

    private String statut; // حالة الموعد: CONFIRME, ANNULE, EN_ATTENTE

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient; // المريض الذي حجز الموعد

    @ManyToOne
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin; // الطبيب المراد زيارته
}
