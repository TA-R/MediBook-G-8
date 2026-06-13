package com.example.mediBook.service;

import com.example.mediBook.models.Patient;
import com.example.mediBook.models.User;
import com.example.mediBook.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    // Inscrire un patient
    public Patient inscrirePatient(Patient patient, User user) {
        user.setRole("PATIENT");
        User savedUser = userService.inscrire(user);
        patient.setUser(savedUser);
        return patientRepository.save(patient);
    }

    // Liste de tous les patients
    public List<Patient> tousLesPatients() {
        return patientRepository.findAll();
    }
}