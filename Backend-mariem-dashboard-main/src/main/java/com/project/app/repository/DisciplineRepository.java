package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Discipline;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    List<Discipline> findByEmployeId(Long employeId);
}
