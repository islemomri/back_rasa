package com.project.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.app.dto.PosteAvecDatesDTO;
import com.project.app.model.Employe;
import com.project.app.model.EmployePoste;
import com.project.app.model.Poste;

import jakarta.transaction.Transactional;
@Repository
public interface EmployePosteRepository extends JpaRepository<EmployePoste, Long>{

	
	@Transactional
	void deleteById(Long id);
	
	@Query("SELECT ep FROM EmployePoste ep JOIN FETCH ep.poste WHERE ep.id = :id")
	Optional<EmployePoste> findByIdWithPoste(@Param("id") Long id);
	
	@Query("SELECT ep FROM EmployePoste ep WHERE ep.id = :id")
	Optional<EmployePoste> findById(@Param("id") Long id);
	
	@Query("SELECT ep FROM EmployePoste ep " +
	           "WHERE ep.employe.id = :employeId " +
	           "AND ep.poste.id = :posteId " +
	           "AND ep.dateDebut = :dateDebut " +
	           "AND (:dateFin IS NULL OR ep.dateFin = :dateFin)")
	    Optional<EmployePoste> findUniqueEmployePoste(
	        @Param("employeId") Long employeId,
	        @Param("posteId") Long posteId,
	        @Param("dateDebut") LocalDate dateDebut,
	        @Param("dateFin") LocalDate dateFin);
	
    @Query("SELECT ep FROM EmployePoste ep " +
            "JOIN FETCH ep.employe e " +
            "JOIN FETCH ep.poste p " +
            "WHERE e.id = :employeId")
     List<EmployePoste> findByEmployeId(@Param("employeId") Long employeId);

    @Query("SELECT ep FROM EmployePoste ep WHERE ep.employe.id = :employeId AND ep.poste.id = :posteId")
    Optional<EmployePoste> findPosteDetailsByEmployeIdAndPosteId(@Param("employeId") Long employeId, @Param("posteId") Long posteId);

    
    @Transactional
    void deleteByEmployeIdAndPosteId(Long employeId, Long posteId);
    
    
    @Query("SELECT ep FROM EmployePoste ep JOIN FETCH ep.poste WHERE ep.employe.id = :employeId")
    List<EmployePoste> findByEmployeIdWithPoste(@Param("employeId") Long employeId);
    
    @Query("SELECT ep FROM EmployePoste ep JOIN FETCH ep.employe JOIN FETCH ep.poste WHERE ep.id = :id")
    Optional<EmployePoste> findByIdWithDetails(@Param("id") Long id);
    
	}