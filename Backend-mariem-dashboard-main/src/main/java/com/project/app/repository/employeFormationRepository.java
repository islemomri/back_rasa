package com.project.app.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.app.model.formation_employe;

@Repository
public interface employeFormationRepository extends JpaRepository<formation_employe, Long> {
	Optional<formation_employe> findByEmployeIdAndFormationId(Long employeId, Long formationId);
	@Query("SELECT f.document FROM formation_employe f WHERE f.employe.id = :employeId AND f.formation.id = :formationId")
	byte[] findDocumentByEmployeIdAndFormationId(@Param("employeId") Long employeId, @Param("formationId") Long formationId);
	List<formation_employe> findByFormationId(Long formationId);
	void deleteByFormationId(Long id);
	
	 @Query("SELECT fe FROM formation_employe fe " +
	           "JOIN FETCH fe.formation " +
	           "WHERE fe.employe.id = :employeId")
	    List<formation_employe> findByEmployeIdWithDetails(@Param("employeId") Long employeId);
	
	
}