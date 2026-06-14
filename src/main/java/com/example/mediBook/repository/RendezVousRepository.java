package com.example.mediBook.repository;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Patient;
import com.example.mediBook.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByPatient(Patient patient);
    List<RendezVous> findByMedecin(Medecin medecin);
    boolean existsByMedecinAndDateAndHeure(Medecin medecin, LocalDate date, LocalTime heure);
}
