package com.project.app.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.app.model.Utilisateur;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>{
	Optional<Utilisateur> findByUsername(String username);
	Boolean existsByUsername(String username);
	Optional<Utilisateur> findByEmail(String email);
    Boolean existsByEmail(String email);
    
    
    
}
