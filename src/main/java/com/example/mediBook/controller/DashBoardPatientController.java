package com.example.mediBook.controller;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Patient;
import com.example.mediBook.models.RendezVous;
import com.example.mediBook.service.MedecinService;
import com.example.mediBook.service.PatientService;
import com.example.mediBook.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/dashboardPatient")
public class DashBoardPatientController {

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RendezVousService rendezVousService;

    // Dashboard
    @GetMapping("/")
    public String dashboard(Model model) {
        return "patient/dashboardPatient";
    }

    // Liste des médecins
    @GetMapping("/medecins")
    public String listeMedecins(Model model) {
        model.addAttribute("medecins", medecinService.tousLesMedecins());
        model.addAttribute("specialites", medecinService.toutesLesSpecialites());
        return "patient/medecins";
    }

    // Page prendre RDV
    @GetMapping("/rdv/{medecinId}")
    public String prendreRdv(@PathVariable Long medecinId, Model model) {
        Optional<Medecin> medecin = medecinService.trouverParId(medecinId);
        medecin.ifPresent(m -> model.addAttribute("medecin", m));
        return "patient/prendre-rdv";
    }

    // Traitement RDV
    @PostMapping("/rdv/{medecinId}")
    public String confirmerRdv(@PathVariable Long medecinId,
                               @RequestParam String date,
                               @RequestParam String heure,
                               @RequestParam Long patientId,
                               Model model) {
        Optional<Medecin> medecin = medecinService.trouverParId(medecinId);
        Optional<Patient> patient = patientService.trouverParId(patientId);

        if (medecin.isPresent() && patient.isPresent()) {
            try {
                rendezVousService.prendreRdv(
                        patient.get(),
                        medecin.get(),
                        LocalDate.parse(date),
                        LocalTime.parse(heure)
                );
                return "redirect:/dashboardPatient/mes-rdv";
            } catch (Exception e) {
                model.addAttribute("erreur", e.getMessage());
                model.addAttribute("medecin", medecin.get());
                return "patient/prendre-rdv";
            }
        }
        return "redirect:/dashboardPatient/medecins";
    }

    // Mes RDV
    @GetMapping("/rdv")
    public String mesRdv(Model model) {
        model.addAttribute("rdvs", rendezVousService.tousLesRdv());
        return "patient/mes-rdv";
    }

    // Profil
    @GetMapping("/profil")
    public String profil(Model model) {
        return "patient/profil";
    }
}