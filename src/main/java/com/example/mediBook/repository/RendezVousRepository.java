package com.example.mediBook.repository;

import com.example.mediBook.models.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    // CORRECTION : findByPatientId ne fonctionne pas car "patient" est un objet
    // @ManyToOne dans RendezVous, pas un simple Long.
    // Spring Data JPA exige la notation avec underscore : findByPatient_Id
    // pour naviguer dans la relation : rendezVous → patient → id
    List<RendezVous> findByPatient_Id(Long patientId);

    // Même correction pour le médecin
    List<RendezVous> findByMedecin_Id(Long medecinId);

    List<RendezVous> findByMedecin_IdAndDateBetween(Long medecinId, LocalDate debut, LocalDate fin);

    @Query("SELECT COUNT(r) > 0 FROM RendezVous r WHERE r.medecin.id = :medecinId " +
            "AND r.date = :date AND r.heure = :heure " +
            "AND r.statut <> com.example.mediBook.models.RendezVous.Statut.ANNULE")
    boolean existsConflict(@Param("medecinId") Long medecinId,
                           @Param("date") LocalDate date,
                           @Param("heure") LocalTime heure);
}

