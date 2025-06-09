package com.project.app.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // Tables séparées pour chaque type
@DiscriminatorColumn(name = "type_experience") // Permet d’identifier le type
@Data
@AllArgsConstructor




public abstract class Experience {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Temporal(TemporalType.DATE)// stocke uniquement la date (sans l'heure).
	    private Date dateDebut;

	    @Temporal(TemporalType.DATE)
	    private Date dateFin;

	    private String poste; 
	    
	    @ManyToOne
	    @JoinColumn(name = "employe_id", nullable = false) // Clé étrangère vers Employe
	    @JsonIgnore // Évite la récursion infinie
	    private Employe employe;
	    
	    public Experience() {
	    }

	    
	    
	    
	    
}
