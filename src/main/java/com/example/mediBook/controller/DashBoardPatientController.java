package com.example.mediBook.controller;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Patient;
import com.example.mediBook.models.RendezVous;
import com.example.mediBook.repository.MedecinRepository;
import com.example.mediBook.repository.SpecialiteRepository;
import com.example.mediBook.service.PatientService;
import com.example.mediBook.service.RendezVousService;
import com.example.mediBook.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/dashboardPatient")
public class DashBoardPatientController {

    private final PatientService patientService;
    private final RendezVousService rendezVousService;
    private final MedecinRepository medecinRepository;
    private final SpecialiteRepository specialiteRepository;
    private final UserService userService;

    public DashBoardPatientController(PatientService patientService,
                                      RendezVousService rendezVousService,
                                      MedecinRepository medecinRepository,
                                      SpecialiteRepository specialiteRepository,
                                      UserService userService) {
        this.patientService = patientService;
        this.rendezVousService = rendezVousService;
        this.medecinRepository = medecinRepository;
        this.specialiteRepository = specialiteRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("patient", getPatientConnecte(userDetails));
        return "patient/dashboardPatient";
    }

    @GetMapping("/medecins")
    public String chercheMedecin(@RequestParam(required = false) Long specialiteId,
                                 @RequestParam(required = false) String recherche,
                                 Model model) {
        List<Medecin> medecins;
        if (specialiteId != null) {
            medecins = specialiteRepository.findById(specialiteId)
                    .map(medecinRepository::findBySpecialite)
                    .orElse(medecinRepository.findAll());
        } else if (recherche != null && !recherche.isBlank()) {
            medecins = medecinRepository.rechercherParNomOuPrenom(recherche);
        } else {
            medecins = medecinRepository.findAll();
        }
        model.addAttribute("medecins", medecins);
        model.addAttribute("specialites", specialiteRepository.findAll());
        return "patient/medecins";
    }

    @GetMapping("/rdv")
    public String mesRdv(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Patient patient = getPatientConnecte(userDetails);
        model.addAttribute("rdvs", rendezVousService.rdvDuPatient(patient.getId()));
        return "patient/mes-rdv";
    }

    @GetMapping("/rdv/nouveau")
    public String formRdv(@RequestParam Long medecinId, Model model) {
        model.addAttribute("medecin", medecinRepository.findById(medecinId).orElseThrow());
        // CORRECTION : "patient/formRdv" → "patient/prendre-rdv"
        // Le fichier dans le projet s'appelle prendre-rdv.html pas formRdv.html
        return "patient/prendre-rdv";
    }

    @PostMapping("/rdv/nouveau")
    public String prendreRdv(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam Long medecinId,
                             @RequestParam String date,
                             @RequestParam String heure,
                             @RequestParam String motif,
                             Model model) {
        try {
            Patient patient = getPatientConnecte(userDetails);
            rendezVousService.prendreRdv(patient.getId(), medecinId,
                    LocalDate.parse(date), LocalTime.parse(heure), motif);
            return "redirect:/dashboardPatient/rdv";
        } catch (IllegalStateException e) {
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("medecin", medecinRepository.findById(medecinId).orElseThrow());
            return "patient/prendre-rdv";
        }
    }

    @PostMapping("/rdv/annuler/{id}")
    public String annulerRdv(@PathVariable Long id) {
        rendezVousService.annulerRdv(id);
        return "redirect:/dashboardPatient/rdv";
    }

    @GetMapping("/profil")
    public String profil(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("patient", getPatientConnecte(userDetails));
        return "patient/profil";
    }

    private Patient getPatientConnecte(UserDetails userDetails) {
        return userService.findByTelephone(userDetails.getUsername())
                .flatMap(u -> patientService.findByUserId(u.getId()))
                .orElseThrow(() -> new IllegalStateException("Patient introuvable"));
    }
}