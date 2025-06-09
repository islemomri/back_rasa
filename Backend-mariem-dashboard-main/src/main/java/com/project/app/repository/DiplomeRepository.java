package com.project.app.repository;

import com.project.app.model.Diplome;
import com.project.app.model.TypeDiplome;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiplomeRepository extends JpaRepository<Diplome, Long> {
	
	List<Diplome> findByEmployeId(Long employeId);
	Optional<Diplome> findByLibelleAndTypeDiplome(String libelle, TypeDiplome typeDiplome);
}