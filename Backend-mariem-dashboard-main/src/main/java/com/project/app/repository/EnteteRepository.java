package com.project.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Entete;

@Repository
public interface EnteteRepository extends JpaRepository<Entete, Long> {

}
