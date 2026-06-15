package com.example.mediBook.controller;

import com.example.mediBook.models.Patient;
import com.example.mediBook.models.User;
import com.example.mediBook.service.PatientService;
import com.example.mediBook.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/authPatient")
public class AuthController {

    private final PatientService patientService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(PatientService patientService,
                          UserService userService,
                          AuthenticationManager authenticationManager) {
        this.patientService = patientService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/register")
    public String showRegister() {
        return "patient/register";
    }
    @PostMapping("/register")
    public String register(@RequestParam String nom,
                           @RequestParam String prenom,
                           @RequestParam String telephone,
                           @RequestParam String dateNaissance,
                           @RequestParam String password,
                           Model model) {
        if (userService.telephoneExiste(telephone)) {
            model.addAttribute("erreur", "Ce téléphone est déjà utilisé !");
            return "patient/register";
        }

        User user = new User();
        user.setTelephone(telephone);
        user.setPassword(password);
        user.setRole(User.Role.PATIENT);

        Patient patient = new Patient();
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setDateNaissance(LocalDate.parse(dateNaissance));

        patientService.inscrirePatient(patient, user);
        return "redirect:/authPatient/login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "patient/login";
    }
    @PostMapping("/login")
    public String login(@RequestParam String telephone,
                        @RequestParam String password,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(telephone, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            new HttpSessionSecurityContextRepository()
                    .saveContext(SecurityContextHolder.getContext(), request, response);

            // CORRECTION : redirection selon le rôle de l'utilisateur connecté
            // Avant : tout le monde était redirigé vers /dashboardPatient/
            // même un ADMIN → 403 Forbidden
            String role = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse("");

            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/";
            } else if (role.equals("ROLE_MEDECIN")) {
                return "redirect:/dashboardMedecin/";
            } else {
                return "redirect:/dashboardPatient/";
            }

        } catch (Exception e) {
            model.addAttribute("erreur", "Téléphone ou mot de passe incorrect !");
            return "patient/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}
