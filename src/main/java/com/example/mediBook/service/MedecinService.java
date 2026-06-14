package com.example.mediBook.service;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Specialite;
import com.example.mediBook.repository.MedecinRepository;
import com.example.mediBook.repository.SpecialiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedecinService {

    private final MedecinRepository medecinRepository;
    private final SpecialiteRepository specialiteRepository;

    public MedecinService(MedecinRepository medecinRepository,
                          SpecialiteRepository specialiteRepository) {
        this.medecinRepository = medecinRepository;
        this.specialiteRepository = specialiteRepository;
    }

    // AJOUT : manquait → AdminController appelait medecinService.tousLesMedecins()
    public List<Medecin> tousLesMedecins() {
        return medecinRepository.findAll();
    }

    // AJOUT : manquait → AdminController appelait medecinService.toutesLesSpecialites()
    public List<Specialite> toutesLesSpecialites() {
        return specialiteRepository.findAll();
    }

    // AJOUT : manquait → AdminController appelait medecinService.supprimer(id)
    public void supprimer(Long id) {
        medecinRepository.deleteById(id);
    }

    // Recherche par nom (appelé dans MedecinService depuis AdminController)
    public List<Medecin> chercherParNom(String nom) {
        return medecinRepository.findByNomContaining(nom);
    }

    public Optional<Medecin> trouverParId(Long id) {
        return medecinRepository.findById(id);
    }
}