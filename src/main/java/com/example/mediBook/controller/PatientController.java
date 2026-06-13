package com.example.mediBook.controller;

import com.example.mediBook.models.Patient;
import com.example.mediBook.models.User;
import com.example.mediBook.service.PatientService;
import com.example.mediBook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/p")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegister() {
        return "patient/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String nom,
                           @RequestParam String prenom,
                           @RequestParam String telephone,
                           @RequestParam String password,
                           Model model) {
        if (userService.telephoneExiste(telephone)) {
            model.addAttribute("erreur", "Téléphone déjà utilisé !");
            return "patient/register";
        }
        User user = new User();
        user.setTelephone(telephone);
        user.setPassword(password);
        user.setRole("PATIENT");

        Patient patient = new Patient();
        patient.setNom(nom);
        patient.setPrenom(prenom);

        patientService.inscrirePatient(patient, user);
        return "redirect:/p/login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "patient/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String telephone,
                        @RequestParam String password,
                        Model model) {
        var user = userService.connecterParTelephone(telephone, password);
        if (user.isPresent()) {
            return "redirect:/p/dashboard";
        } else {
            model.addAttribute("erreur", "Téléphone ou mot de passe incorrect !");
            return "patient/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "patient/dashboard";
    }
}