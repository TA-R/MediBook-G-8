package com.example.mediBook.controller;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.RendezVous;
import com.example.mediBook.service.MedecinService;
import com.example.mediBook.service.RendezVousService;
import com.example.mediBook.service.UserService;
import com.example.mediBook.repository.MedecinRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dashboardMedecin")
public class MedecinController {

    // CORRECTION : @Autowired sur les champs → injection par constructeur
    private final MedecinService medecinService;
    private final RendezVousService rendezVousService;
    private final UserService userService;
    private final MedecinRepository medecinRepository;

    public MedecinController(MedecinService medecinService,
                             RendezVousService rendezVousService,
                             UserService userService,
                             MedecinRepository medecinRepository) {
        this.medecinService = medecinService;
        this.rendezVousService = rendezVousService;
        this.userService = userService;
        this.medecinRepository = medecinRepository;
    }

    // CORRECTION : le dashboard récupère le médecin connecté via Spring Security
    // Avant : dashboard() ne transmettait aucune donnée au modèle
    @GetMapping("/")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        getMedecinConnecte(userDetails).ifPresent(m -> {
            List<RendezVous> rdvs = rendezVousService.rdvMedecin(m);
            model.addAttribute("medecin", m);
            model.addAttribute("rdvs", rdvs);
        });
        return "medecin/dashboard";
    }

    // Liste des RDV du médecin connecté
    @GetMapping("/rdv")
    public String mesRdv(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        getMedecinConnecte(userDetails).ifPresent(m -> {
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
        return "redirect:/dashboardMedecin/rdv";
    }

    // Annuler un RDV
    @PostMapping("/rdv/annuler/{rdvId}")
    public String annulerRdv(@PathVariable Long rdvId) {
        rendezVousService.annulerRdv(rdvId);
        return "redirect:/dashboardMedecin/rdv";
    }

    // Utilitaire : trouve le médecin lié au compte connecté
    private Optional<Medecin> getMedecinConnecte(UserDetails userDetails) {
        return userService.findByTelephone(userDetails.getUsername())
                .flatMap(u -> medecinRepository.findByUser_Id(u.getId()));
    }
}
