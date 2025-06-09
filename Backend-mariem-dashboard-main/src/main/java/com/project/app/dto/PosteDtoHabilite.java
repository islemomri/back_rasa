package com.project.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PosteDtoHabilite {
	 private Long id;
	    private String niveauExperience;
	    private String diplomeRequis;
	    private String titre; // Assure-toi que ce champ existe dans l'entité Poste
	    private List<String> competences; 
	    private List<String> competencesManquantes; 
	    private List<String> competencesSupplementaires;
}
