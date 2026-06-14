package com.example.mediBook.repository;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {

    List<Medecin> findBySpecialite(Specialite specialite);

    // CORRECTION : MedecinService appelle findByNomContaining(nom)
    // Cette méthode Spring Data est valide — elle cherche dans le champ "nom"
    List<Medecin> findByNomContaining(String nom);

    // Méthode @Query pour recherche nom OU prénom (utilisée dans DashBoardPatientController)
    @Query("SELECT m FROM Medecin m WHERE " +
            "LOWER(m.nom) LIKE LOWER(CONCAT('%', :recherche, '%')) OR " +
            "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :recherche, '%'))")
    List<Medecin> rechercherParNomOuPrenom(@Param("recherche") String recherche);

    Optional<Medecin> findByUser_Id(Long userId);
}
