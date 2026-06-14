package com.example.mediBook.service;

import com.example.mediBook.models.Patient;
import com.example.mediBook.models.User;
import com.example.mediBook.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    // Inscription d'un patient avec son compte utilisateur
    // CORRECTION : dateNaissance est maintenant un champ du modèle Patient.
    // Avant, même si le contrôleur le recevait, il n'y avait nulle part où
    // le stocker → la donnée était perdue.
    public Patient inscrirePatient(Patient patient, User user) {
        user.setRole(User.Role.PATIENT);
        User savedUser = userService.inscrire(user);
        patient.setUser(savedUser);
        return patientRepository.save(patient);
    }

    public List<Patient> tousLesPatients() {
        return patientRepository.findAll();
    }

    // AJOUT : retrouver un patient à partir de son compte utilisateur
    // (nécessaire pour afficher le profil et les RDV du patient connecté)
    public Optional<Patient> findByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }
}