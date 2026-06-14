package com.example.mediBook.controller;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.RendezVous;
import com.example.mediBook.service.MedecinService;
import com.example.mediBook.service.RendezVousService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/medecin")
public class MedecinController {

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private RendezVousService rendezVousService;

    // Dashboard médecin
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "medecin/dashboard";
    }

    // Liste des RDV du médecin
    @GetMapping("/rdv/{id}")
    public String mesRdv(@PathVariable Long id, Model model) {
        Optional<Medecin> medecin = medecinService.trouverParId(id);
        medecin.ifPresent(m -> {
            List<RendezVous> rdvs = rendezVousService.rdvMedecin(m);
            model.addAttribute("rdvs", rdvs);
            model.addAttribute("medecin", m);
        });
        return "medecin/mes-rdv";
    }

    // Confirmer un RDV
    @PostMapping("/rdv/confirmer/{rdvId}")
    public String confirmerRdv(@PathVariable Long rdvId) {
        rendezVousService.confirmerRdv(rdvId);
        return "redirect:/medecin/dashboard";
    }

    // Annuler un RDV
    @PostMapping("/rdv/annuler/{rdvId}")
    public String annulerRdv(@PathVariable Long rdvId) {
        rendezVousService.annulerRdv(rdvId);
        return "redirect:/medecin/dashboard";
    }
}
