package com.project.app.dto;

import java.util.List;

import com.project.app.model.Employe;
import com.project.app.model.Poste;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeHabilitationDto {
	 private Long id; 
	 private String nom;
	    private String prenom;
	    private Integer matricule;
	    private String email;
	    private List<String> competences;
    private List<PosteDtoHabilite> postesHabilites;
}
