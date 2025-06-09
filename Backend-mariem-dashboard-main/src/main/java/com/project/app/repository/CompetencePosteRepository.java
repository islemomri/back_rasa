package com.project.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.CompetencePoste;

@Repository
public interface CompetencePosteRepository extends JpaRepository<CompetencePoste, Long>{
	Optional<CompetencePoste> findByNom(String nom);

}
