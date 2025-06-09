	package com.project.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Competence;

@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Long> {
	boolean existsByNomIgnoreCase(String nom);

    Optional<Competence> findByNomIgnoreCase(String nom);
    Optional<Competence> findByNom(String nom);

}

