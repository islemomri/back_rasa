package com.project.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Responsable;

@Repository
public interface ResponsableRepository extends JpaRepository<Responsable, Long> {

}

