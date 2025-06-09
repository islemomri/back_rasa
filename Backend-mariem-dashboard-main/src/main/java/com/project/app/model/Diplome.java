package com.project.app.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Diplome {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;
    private LocalDate dateObtention;
    private boolean archive=false;
    
    public Diplome() {
    	this.archive=false;
    }
    
    public void archiver() {
    	this.archive=true;
    }
    
    @ManyToOne
    @JoinColumn(name = "employe_id")
    private Employe employe;

    @JsonIgnoreProperties("diplomes")
    @ManyToOne
    @JoinColumn(name = "type_diplome_id", nullable = false)
    private TypeDiplome typeDiplome;
}
