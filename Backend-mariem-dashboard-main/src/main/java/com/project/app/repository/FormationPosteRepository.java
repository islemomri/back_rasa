package com.project.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.app.model.FormationPoste;

public interface FormationPosteRepository extends JpaRepository<FormationPoste, Long> {


	Optional<FormationPoste> findByFormationId(Long formationId);
}