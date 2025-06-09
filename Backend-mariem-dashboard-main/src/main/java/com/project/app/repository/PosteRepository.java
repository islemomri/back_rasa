package com.project.app.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Poste;


@Repository
public interface PosteRepository extends JpaRepository<Poste, Long>{
	List<Poste> findByArchiveFalse(); 
	List<Poste> findByArchiveTrue();
}