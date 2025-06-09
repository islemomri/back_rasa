package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;
@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
	 // Recherche des expériences Assad
    List<Experience> findByEmployeId(Long employeId);  


}
