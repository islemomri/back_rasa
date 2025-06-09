package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.app.model.EmployeCompetence;

public interface EmployeCompetenceRepository extends JpaRepository<EmployeCompetence, Long> {
    List<EmployeCompetence> findByEmployeId(Long employeId);
    boolean existsByEmployeIdAndCompetenceId(Long employeId, Long competenceId);
    List<EmployeCompetence> findByCompetenceId(Long competenceId);
}

