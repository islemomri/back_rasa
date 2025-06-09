package com.project.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Candidat;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long>{

}
