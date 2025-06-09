package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Stage;


@Repository
public interface StageRepository extends JpaRepository<Stage, Long>{
	List<Stage> findByEmployeId(Long employeId);
}