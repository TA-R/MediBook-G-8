package com.example.mediBook.controller;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Specialite;
import com.example.mediBook.models.User;
import com.example.mediBook.repository.MedecinRepository;
import com.example.mediBook.repository.SpecialiteRepository;
import com.example.mediBook.repository.UserRepository;
import com.example.mediBook.service.MedecinService;
import com.example.mediBook.service.PatientService;
import com.example.mediBook.service.RendezVousService;
import com.example.mediBook.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MedecinService medecinService;
    private final PatientService patientService;
    private final RendezVousService rendezVousService;
    private final SpecialiteRepository specialiteRepository;
    private final MedecinRepository medecinRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(MedecinService medecinService,
                           PatientService patientService,
                           RendezVousService rendezVousService,
                           SpecialiteRepository specialiteRepository,
                           MedecinRepository medecinRepository,
                           UserRepository userRepository,
                           UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.medecinService = medecinService;
        this.patientService = patientService;
        this.rendezVousService = rendezVousService;
        this.specialiteRepository = specialiteRepository;
        this.medecinRepository = medecinRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String adminRoot() { return "redirect:/admin/dashboard"; }

    @GetMapping("")
    public String adminHome() { return "redirect:/admin/dashboard"; }

    @GetMapping("/login")
    public String adminLogin() { return "admin/login"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalMedecins", medecinService.tousLesMedecins().size());
        model.addAttribute("totalPatients", patientService.tousLesPatients().size());
        model.addAttribute("totalRdv", rendezVousService.tousLesRdv().size());
        return "admin/dashboard";
    }

    @GetMapping("/medecins")
    public String medecins(Model model) {
        model.addAttribute("medecins", medecinService.tousLesMedecins());
        model.addAttribute("specialites", medecinService.toutesLesSpecialites());
        return "admin/medecins";
    }

    @PostMapping("/medecins/ajouter")
    public String ajouterMedecin(@RequestParam String nom,
                                 @RequestParam String prenom,
                                 @RequestParam String telephone,
                                 // CORRECTION : paramètre password ajouté
                                 // Dans l'ancienne version il manquait ici
                                 // donc le mot de passe saisi dans le formulaire
                                 // était ignoré et remplacé par le téléphone
                                 @RequestParam String password,
                                 @RequestParam Long specialiteId,
                                 Model model) {

        if (userService.telephoneExiste(telephone)) {
            model.addAttribute("erreur", "Ce téléphone est déjà utilisé !");
            model.addAttribute("medecins", medecinService.tousLesMedecins());
            model.addAttribute("specialites", medecinService.toutesLesSpecialites());
            return "admin/medecins";
        }

        User user = new User();
        user.setTelephone(telephone);
        // CORRECTION : utilise le mot de passe saisi dans le formulaire
        // et non plus le téléphone comme mot de passe par défaut
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.MEDECIN);
        User savedUser = userRepository.save(user);

        Medecin medecin = new Medecin();
        medecin.setNom(nom);
        medecin.setPrenom(prenom);
        medecin.setUser(savedUser);
        specialiteRepository.findById(specialiteId)
                .ifPresent(medecin::setSpecialite);

        medecinRepository.save(medecin);
        return "redirect:/admin/medecins";
    }

    @PostMapping("/medecins/supprimer/{id}")
    public String supprimerMedecin(@PathVariable Long id) {
        medecinService.supprimer(id);
        return "redirect:/admin/medecins";
    }

    @GetMapping("/patients")
    public String patients(Model model) {
        model.addAttribute("patients", patientService.tousLesPatients());
        return "admin/patients";
    }

    @GetMapping("/rdv")
    public String rdv(Model model) {
        model.addAttribute("rdvs", rendezVousService.tousLesRdv());
        return "admin/rdv";
    }

    @GetMapping("/specialites")
    public String specialites(Model model) {
        model.addAttribute("specialites", medecinService.toutesLesSpecialites());
        return "admin/specialites";
    }

    @PostMapping("/specialites/ajouter")
    public String ajouterSpecialite(@RequestParam String nom) {
        Specialite specialite = new Specialite();
        specialite.setNom(nom);
        specialiteRepository.save(specialite);
        return "redirect:/admin/specialites";
    }
}