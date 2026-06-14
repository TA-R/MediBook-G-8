package com.example.mediBook.controller;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Specialite;
import com.example.mediBook.service.MedecinService;
import com.example.mediBook.service.PatientService;
import com.example.mediBook.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.mediBook.repository.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private RendezVousService rendezVousService;
    @Autowired
    private SpecialiteRepository specialiteRepository;
    // Dashboard admin
    @Autowired
    private MedecinRepository medecinRepository;
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMedecins",
                medecinService.tousLesMedecins().size());
        model.addAttribute("totalPatients",
                patientService.tousLesPatients().size());
        model.addAttribute("totalRdv",
                rendezVousService.tousLesRdv().size());
        return "admin/dashboard";
    }

    // Liste des médecins
    @GetMapping("/medecins")
    public String medecins(Model model) {
        model.addAttribute("medecins", medecinService.tousLesMedecins());
        model.addAttribute("specialites", medecinService.toutesLesSpecialites());
        return "admin/medecins";
    }
    @GetMapping("/login")
    public String adminLogin() {
        return "admin/login";
    }

    @GetMapping("")
    public String adminHome() {
        return "admin/index";
    }

    // Ajouter médecin
    @PostMapping("/medecins/ajouter")
    public String ajouterMedecin(@RequestParam String nom,
                                 @RequestParam String prenom,
                                 @RequestParam String telephone,
                                 @RequestParam Long specialiteId) {
        Medecin medecin = new Medecin();
        medecin.setNom(nom);
        medecin.setPrenom(prenom);

        // Lier la spécialité
        specialiteRepository.findById(specialiteId)
                .ifPresent(medecin::setSpecialite);

        medecinRepository.save(medecin);
        return "redirect:/admin/medecins";
    }

    // Supprimer médecin
    @PostMapping("/medecins/supprimer/{id}")
    public String supprimerMedecin(@PathVariable Long id) {
        medecinService.supprimer(id);
        return "redirect:/admin/medecins";
    }

    // Liste des patients
    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", patientService.tousLesPatients());
        return "admin/patients";
    }

    // Liste des RDV
    @GetMapping("/rdv")
    public String rdv(Model model) {
        model.addAttribute("rdvs", rendezVousService.tousLesRdv());
        return "admin/rdv";
    }

    // Spécialités
    @GetMapping("/specialites")
    public String specialites(Model model) {
        model.addAttribute("specialites",
                medecinService.toutesLesSpecialites());
        return "admin/specialites";
    }

    // Ajouter spécialité
    // Ajouter spécialité
    @PostMapping("/specialites/ajouter")
    public String ajouterSpecialite(@RequestParam String nom) {
        Specialite specialite = new Specialite();
        specialite.setNom(nom);
        specialiteRepository.save(specialite); // ← CORRECT !
        return "redirect:/admin/specialites";
    }
}
