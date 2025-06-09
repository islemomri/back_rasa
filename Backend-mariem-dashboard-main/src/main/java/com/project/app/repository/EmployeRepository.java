package com.project.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.app.dto.DiplomeDTO;
import com.project.app.dto.EmployeBaseInfoDTO;
import com.project.app.dto.EmployeReportingProjectionDTO;
import com.project.app.dto.ExperienceDTO;
import com.project.app.dto.FormationDTO2;
import com.project.app.model.Diplome;
import com.project.app.model.Employe;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.Formation;
import com.project.app.model.Poste;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
	 Optional<Employe> findByMatricule(Long matricule);
	 @Query("SELECT e FROM Employe e WHERE e.ajout = false")
	 List<Employe> findEmployesWhereAjoutIsFalse();
	 
	 
	 @Query("SELECT e FROM Employe e WHERE e.ajout = true")
	 List<Employe> findEmployesWhereAjoutIsTrue();
	 
	 @Query("SELECT DISTINCT e FROM Employe e " +
		       "LEFT JOIN FETCH e.employePostes ep " +
		       "LEFT JOIN FETCH ep.poste p " +
		       "LEFT JOIN FETCH e.diplomes d " +
		       "LEFT JOIN FETCH d.typeDiplome td " +
		       "LEFT JOIN FETCH e.experiences ea " +
		       "WHERE e.actif = true")
		List<Employe> findEmployesWithAllData();
	 
	 @Query(value = """
			    SELECT 
			        e.nom AS nom,
			        e.prenom AS prenom,
			        e.matricule AS matricule,
			        e.email AS email,
			        p.titre AS posteActuel,
			        d.libelle AS diplomeLibelle,
			        td.libelleTypeDiplome AS typeDiplome,
			        d.dateObtention AS dateObtention,
			        ep.nomDirection AS direction,
			        ea.societe AS societe,
			        ea.poste AS posteExperience,
			        ea.dateDebut AS dateDebut,
			        ea.dateFin AS dateFin
			    FROM employe e
			    LEFT JOIN employePoste ep ON ep.employe_id = e.id AND ep.dateFin IS NULL
			    LEFT JOIN poste p ON p.id = ep.poste_id
			    LEFT JOIN diplome d ON d.employe_id = e.id
			    LEFT JOIN typeDiplome td ON td.id = d.type_diplome_id
			    LEFT JOIN experience ea ON ea.employe_id = e.id AND dtype = 'ExperienceAnterieure'
			    WHERE e.actif = true
			    """, nativeQuery = true)
			List<EmployeReportingProjectionDTO> generateEmployeReport();
	 
	 @Query("SELECT NEW com.project.app.dto.EmployeBaseInfoDTO(" +
	           "e.id, e.nom, e.prenom, e.matricule, e.email, p.titre, ep.nomDirection) " +
	           "FROM Employe e " +
	           "LEFT JOIN e.employePostes ep ON ep.dateFin IS NULL " + 
	           "LEFT JOIN ep.poste p " +
	           "WHERE e.actif = true " +
	           "ORDER BY e.nom, e.prenom")
	    List<EmployeBaseInfoDTO> findEmployeBaseInfos();

	    @Query("SELECT NEW com.project.app.dto.DiplomeDTO(" +
	           "d.employe.id, d.libelle, td.libelleTypeDiplome, d.dateObtention) " +
	           "FROM Diplome d JOIN d.typeDiplome td " +
	           "WHERE d.employe.actif = true")
	    List<DiplomeDTO> findAllDiplomesForActiveEmployes();

	    @Query("SELECT NEW com.project.app.dto.ExperienceDTO(" +
	           "ea.employe.id, ea.societe, ea.poste, ea.dateDebut, ea.dateFin) " +
	           "FROM Experience ea " +
	           "WHERE TYPE(ea) = ExperienceAnterieure AND ea.employe.actif = true")
	    List<ExperienceDTO> findAllExperiencesForActiveEmployes();

	    @Query("SELECT new com.project.app.dto.FormationDTO2(f.id, f.titre, f.typeFormation, f.sousTypeFormation, f.dateDebutPrevue, f.dateFinPrevue, e.id) " +
	    	       "FROM Formation f JOIN f.employes e WHERE e.actif = true")
	    	List<FormationDTO2> findFormationsForActiveEmployes();




}
