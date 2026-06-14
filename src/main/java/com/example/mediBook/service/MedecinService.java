package com.example.mediBook.service;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Specialite;
import com.example.mediBook.repository.MedecinRepository;
import com.example.mediBook.repository.SpecialiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedecinService {

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private SpecialiteRepository specialiteRepository;

    // Liste tous les médecins
    public List<Medecin> tousLesMedecins() {
        return medecinRepository.findAll();
    }

    // Chercher par spécialité
    public List<Medecin> chercherParSpecialite(Long specialiteId) {
        Optional<Specialite> specialite = specialiteRepository.findById(specialiteId);
        return specialite.map(medecinRepository::findBySpecialite)
                .orElse(List.of());
    }

    // Chercher par nom
    public List<Medecin> chercherParNom(String nom) {
        return medecinRepository.findByNomContaining(nom);
    }

    // Toutes les spécialités
    public List<Specialite> toutesLesSpecialites() {
        return specialiteRepository.findAll();
    }

    // Trouver médecin par id
    public Optional<Medecin> trouverParId(Long id) {
        return medecinRepository.findById(id);
    }

    // Sauvegarder médecin
    public Medecin sauvegarder(Medecin medecin) {
        return medecinRepository.save(medecin);
    }

    // Supprimer médecin
    public void supprimer(Long id) {
        medecinRepository.deleteById(id);
    }
}