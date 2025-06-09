package com.project.app.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.app.dto.DiplomeDTO;
import com.project.app.dto.EmployeBaseInfoDTO;
import com.project.app.dto.EmployeReportingDTO;
import com.project.app.dto.EmployeReportingProjectionDTO;
import com.project.app.dto.ExperienceDTO;
import com.project.app.dto.FormationDTO2;
import com.project.app.model.Diplome;
import com.project.app.model.Employe;
import com.project.app.model.EmployePoste;
import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.repository.EmployeRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmployeReportingService {
 
 private final EmployeRepository employeRepository;
 
 public List<EmployeReportingDTO> generateReport() {
     // Récupérer les infos de base
     List<EmployeBaseInfoDTO> baseInfos = employeRepository.findEmployeBaseInfos();
     
  // Récupérer les diplômes groupés par employé
     Map<Long, List<DiplomeDTO>> diplomesParEmploye = employeRepository.findAllDiplomesForActiveEmployes()
         .stream()
         .collect(Collectors.groupingBy(DiplomeDTO::getEmployeId));
     
     // Récupérer les expériences groupées par employé
     Map<Long, List<ExperienceDTO>> experiencesParEmploye = employeRepository.findAllExperiencesForActiveEmployes()
         .stream()
         .collect(Collectors.groupingBy(ExperienceDTO::getEmployeId));
     
     Map<Long, List<FormationDTO2>> formationsParEmploye = employeRepository.findFormationsForActiveEmployes()
    		    .stream()
    		    .collect(Collectors.groupingBy(FormationDTO2::getEmployeId));

     
     // Construire le résultat final
     return baseInfos.stream().map(base -> {
         EmployeReportingDTO dto = new EmployeReportingDTO();
         
         // Mapper les champs de base
         dto.setNom(base.getNom());
         dto.setPrenom(base.getPrenom());
         dto.setMatricule(base.getMatricule());
         dto.setEmail(base.getEmail());
         dto.setPosteActuel(base.getPosteActuel());
         dto.setDirection(base.getDirection());
         
         // Gérer les diplômes
         List<DiplomeDTO> diplomes = diplomesParEmploye.getOrDefault(base.getId(), Collections.emptyList());
         dto.setDiplomes(diplomes.stream().map(DiplomeDTO::getLibelle).collect(Collectors.joining(", ")));
         dto.setTypesDiplomes(diplomes.stream().map(DiplomeDTO::getTypeDiplome).collect(Collectors.joining(", ")));
         dto.setDatesObtention(diplomes.stream()
             .map(d -> d.getDateObtention() != null ? d.getDateObtention().toString() : "")
             .collect(Collectors.joining(", ")));
         
         List<FormationDTO2> formations = formationsParEmploye.getOrDefault(base.getId(), Collections.emptyList());
         dto.setTitresFormations(formations.stream().map(FormationDTO2::getTitre).collect(Collectors.joining(", ")));
         dto.setTypesFormations(formations.stream().map(f -> f.getTypeFormation().name()).collect(Collectors.joining(", ")));
         dto.setSousTypesFormations(formations.stream().map(f -> f.getSousTypeFormation().name()).collect(Collectors.joining(", ")));
         dto.setPeriodesFormations(formations.stream()
             .map(f -> formatPeriode(f.getDateDebut(), f.getDateFin()))
             .collect(Collectors.joining(", ")));


         
         // Gérer les expériences
         List<ExperienceDTO> experiences = experiencesParEmploye.getOrDefault(base.getId(), Collections.emptyList());
         
         dto.setSocietesExperience(experiences.stream()
             .map(e -> e.getSociete() != null ? e.getSociete() : "")
             .collect(Collectors.joining(", ")));
         
         dto.setPostesExperience(experiences.stream()
             .map(e -> e.getPoste() != null ? e.getPoste() : "")
             .filter(post -> !post.isEmpty()) // Optionnel: exclure les chaînes vides
             .collect(Collectors.joining(", ")));
         
         dto.setPeriodesExperience(experiences.stream()
             .map(e -> formatPeriode(e.getDateDebut(), e.getDateFin()))
             .collect(Collectors.joining(", ")));
         
         return dto;
     }).collect(Collectors.toList());
     	
     

 }
 
 private String formatPeriode(Date debut, Date fin) {
     if (debut == null && fin == null) return "";
     SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
     return (debut != null ? sdf.format(debut) : "?") + " - " + 
            (fin != null ? sdf.format(fin) : "?");
 }
 
 private String formatPeriode(LocalDate debut, LocalDate fin) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    return (debut != null ? debut.format(formatter) : "?") + " - " +
	           (fin != null ? fin.format(formatter) : "?");
	}

}