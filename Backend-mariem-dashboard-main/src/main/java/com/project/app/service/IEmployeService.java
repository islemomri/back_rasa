package com.project.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;

import com.project.app.dto.EmployeDTO;
import com.project.app.dto.EmployeHabilitationDto;
import com.project.app.dto.EmployePosteDetailDto;
import com.project.app.dto.EmployePosteRequest;
import com.project.app.dto.EmployeUpdateDTO;
import com.project.app.dto.PosteAvecDatesDTO;
import com.project.app.model.Discipline;
import com.project.app.model.Employe;
import com.project.app.model.EmployePoste;
import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;
import com.project.app.model.Poste;

import jakarta.transaction.Transactional;

public interface IEmployeService {
	public List<Employe> getAllEmployes();
	 public Employe addEmploye(Employe employe);
	 public Optional<Employe> getEmployeById(Long id);
	 public void deleteEmploye(Long id) ;
	/* public Discipline addDisciplineToEmploye(Long employeId, Discipline discipline);
	 void removeDisciplineFromEmploye(Long employeId, Long disciplineId);
	 Discipline updateDiscipline(Long disciplineId, Discipline updatedDiscipline);
	 List<Discipline> getDisciplinesByEmployeId(Long employeId);*/
	 public Employe addEmployeWithExperience(Employe employe);
	 public Employe ajouterEmploye(EmployeDTO employeDTO);
	 public Employe ajouterEmployeAvecPoste(Employe employe, Long posteId, LocalDate dateDebut, LocalDate dateFin) ;
	
	  public Employe updateEmploye(Long id, EmployeUpdateDTO employeUpdateDTO);
	  public List<Discipline> getAllDisciplines(Long employeId);
	  public List<ExperienceAssad> getAllExperiencesAssadByEmployeId(Long id);
	  public List<ExperienceAnterieure> getAllExperiencesAnterieuresByEmployeId(Long id);
	  public PosteAvecDatesDTO ajouterPosteAEmploye(Long employeId, Long posteId, Long directionId, Long siteId, LocalDate dateDebut, LocalDate dateFin);
	  public List<PosteAvecDatesDTO> getPostesByEmploye(Long employeId);
	  public PosteAvecDatesDTO modifierPosteAEmploye(Long employeId, Long posteId, Long directionId, Long siteId, LocalDate dateDebut, LocalDate dateFin);
	  public Optional<EmployePoste> getPosteDetailsByEmployeIdAndPosteId(Long employeId, Long posteId);
	  public void supprimerPostePourEmploye(Long employeId, Long posteId);
	  public Employe ajouterEmployeAvecPoste(Long posteId, Long directionId, Long siteId, Employe employe, LocalDate dateDebut, LocalDate dateFin);
	  public Employe modifierEmploye(Long id, Employe employeModifie, Long posteId, Long directionId, Long siteId, LocalDate dateDebut, LocalDate dateFin) ;
	  public List<Employe> getEmployesWithoutPoste();
		public byte[] getDocumentByEmployeIdAndFormationId(Long employeId, Long formationId) ;
		public PosteAvecDatesDTO changerPosteEmploye(Long employeId, Long nouveauPosteId, Long directionId,
				Long siteId);
		
		public void supprimerPostePourEmployeParId(Long employePosteId);
		public PosteAvecDatesDTO modifierPosteAEmployeParId(
		        Long employePosteId, Long directionId, Long siteId, LocalDate dateDebut, LocalDate dateFin);
		
		public EmployePoste getPosteDetailsById(Long employePosteId);
		public PosteAvecDatesDTO modifierPosteComplet(
		        Long employePosteId, 
		        Long nouveauPosteId,
		        Long directionId, 
		        Long siteId,
		        LocalDate dateDebut, 
		        LocalDate dateFin) ;
		
		
	
		public List<EmployeHabilitationDto> getEmployesAvecPostesHabilitesProches();
		public String getNomDirectionPosteActuel(Long employeId);
		public EmployePosteDetailDto getEmployeAvecPosteParMatriculeEtTitre(String titrePoste, Integer matricule);
		
		
		
}
