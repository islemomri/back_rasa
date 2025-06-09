package com.project.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PosteAvecDatesDTO {
	private Long employePosteId;
    private Long posteId;
    private String titre;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String nomDirection; // Ajout du nom de la direction
    private String nomSite;      // Ajout du nom du site

    public PosteAvecDatesDTO(Long posteId, String titre, LocalDate dateDebut, LocalDate dateFin, String nomDirection, String nomSite) {
        this.posteId = posteId;
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nomDirection = nomDirection;
        this.nomSite = nomSite;
    }
    
    public PosteAvecDatesDTO(Long employePosteId,Long posteId, String titre, LocalDate dateDebut, LocalDate dateFin, String nomDirection, String nomSite) {
        this.employePosteId=employePosteId;
    	this.posteId = posteId;
        
        this.titre = titre;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nomDirection = nomDirection;
        this.nomSite = nomSite;
    }
}