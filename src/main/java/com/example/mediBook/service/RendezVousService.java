package com.example.mediBook.service;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Patient;
import com.example.mediBook.models.RendezVous;
import com.example.mediBook.repository.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    // Prendre un RDV
    public RendezVous prendreRdv(Patient patient,
                                 Medecin medecin,
                                 LocalDate date,
                                 LocalTime heure) {

        // Vérifier si le créneau est libre
        if (creneauOccupe(medecin, date, heure)) {
            throw new RuntimeException("Ce créneau est déjà occupé !");
        }

        RendezVous rdv = new RendezVous();
        rdv.setPatient(patient);
        rdv.setMedecin(medecin);
        rdv.setDate(date);
        rdv.setHeure(heure);
        rdv.setStatut("EN_ATTENTE");

        return rendezVousRepository.save(rdv);
    }

    // Vérifier si créneau occupé
    public boolean creneauOccupe(Medecin medecin, LocalDate date, LocalTime heure) {
        return rendezVousRepository
                .existsByMedecinAndDateAndHeure(medecin, date, heure);
    }

    // RDV d'un patient
    public List<RendezVous> rdvPatient(Patient patient) {
        return rendezVousRepository.findByPatient(patient);
    }

    // RDV d'un médecin
    public List<RendezVous> rdvMedecin(Medecin medecin) {
        return rendezVousRepository.findByMedecin(medecin);
    }

    // Annuler un RDV
    public void annulerRdv(Long rdvId) {
        Optional<RendezVous> rdv = rendezVousRepository.findById(rdvId);
        rdv.ifPresent(r -> {
            r.setStatut("ANNULE");
            rendezVousRepository.save(r);
        });
    }

    // Confirmer un RDV
    public void confirmerRdv(Long rdvId) {
        Optional<RendezVous> rdv = rendezVousRepository.findById(rdvId);
        rdv.ifPresent(r -> {
            r.setStatut("CONFIRME");
            rendezVousRepository.save(r);
        });
    }

    // Tous les RDV
    public List<RendezVous> tousLesRdv() {
        return rendezVousRepository.findAll();
    }
}