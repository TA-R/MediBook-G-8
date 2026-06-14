package com.example.mediBook.repository;

import com.example.mediBook.models.Medecin;
import com.example.mediBook.models.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long> {
    List<Medecin> findBySpecialite(Specialite specialite);
    List<Medecin> findByNomContaining(String nom);
}