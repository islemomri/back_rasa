package com.project.app.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.app.model.SousTypeFormation;
import com.project.app.model.TypeFormation;

import lombok.Data;

@Data
public class FormationDto {
	 private String titre;
	    private String description;
	    private TypeFormation typeFormation;
	    private SousTypeFormation sousTypeFormation;
	    private LocalDate dateDebutPrevue;
	    private LocalDate dateFinPrevue;
	    private Long responsableEvaluationId; 
	    private String responsableEvaluationExterne; 
	    private List<Long> employeIds;
	    private MultipartFile fichierPdf;
	    private Long organisateurId;
	    private String titrePoste;
	    private LocalDate dateRappel; 
	    private List<PeriodeDto> periodes;
	    private boolean annuler;
	    private Long enteteId; 
	 
	    @Data
	    public static class PeriodeDto {
	        private LocalDate dateDebut;
	        private LocalDate dateFin;
	        private String formateur;  
	        private String programme;
	    }

}
