package com.example.mediBook.service;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Patient;
import com.example.mediBook.models.RendezVous;
import com.example.mediBook.models.RendezVous.Statut;
import com.example.mediBook.repository.MedecinRepository;
import com.example.mediBook.repository.PatientRepository;
import com.example.mediBook.repository.RendezVousRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;

    public RendezVousService(RendezVousRepository rendezVousRepository,
                             PatientRepository patientRepository,
                             MedecinRepository medecinRepository) {
        this.rendezVousRepository = rendezVousRepository;
        this.patientRepository = patientRepository;
        this.medecinRepository = medecinRepository;
    }

    public RendezVous prendreRdv(Long patientId, Long medecinId,
                                 LocalDate date, LocalTime heure, String motif) {
        if (rendezVousRepository.existsConflict(medecinId, date, heure)) {
            throw new IllegalStateException(
                    "Ce créneau est déjà pris. Veuillez choisir une autre heure.");
        }
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient introuvable"));
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new IllegalArgumentException("Médecin introuvable"));

        RendezVous rdv = new RendezVous();
        rdv.setPatient(patient);
        rdv.setMedecin(medecin);
        rdv.setDate(date);
        rdv.setHeure(heure);
        rdv.setMotif(motif);
        rdv.setStatut(Statut.EN_ATTENTE);
        return rendezVousRepository.save(rdv);
    }

    public RendezVous confirmerRdv(Long rdvId) {
        RendezVous rdv = rendezVousRepository.findById(rdvId)
                .orElseThrow(() -> new IllegalArgumentException("RDV introuvable"));
        rdv.setStatut(Statut.CONFIRME);
        return rendezVousRepository.save(rdv);
    }

    public RendezVous annulerRdv(Long rdvId) {
        RendezVous rdv = rendezVousRepository.findById(rdvId)
                .orElseThrow(() -> new IllegalArgumentException("RDV introuvable"));
        rdv.setStatut(Statut.ANNULE);
        return rendezVousRepository.save(rdv);
    }

    public List<RendezVous> rdvDuPatient(Long patientId) {
        return rendezVousRepository.findByPatient_Id(patientId);
    }

    // CORRECTION : MedecinController appelle rdvMedecin(medecin) avec un objet Medecin.
    // Cette méthode manquait → "Cannot resolve method 'rdvMedecin'"
    public List<RendezVous> rdvMedecin(Medecin medecin) {
        return rendezVousRepository.findByMedecin_Id(medecin.getId());
    }

    public List<RendezVous> agendaSemaine(Long medecinId, LocalDate debut) {
        LocalDate fin = debut.plusDays(6);
        return rendezVousRepository.findByMedecin_IdAndDateBetween(medecinId, debut, fin);
    }

    public List<RendezVous> tousLesRdv() {
        return rendezVousRepository.findAll();
    }
}
