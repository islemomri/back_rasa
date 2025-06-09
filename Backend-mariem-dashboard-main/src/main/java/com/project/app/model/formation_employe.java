	package com.project.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
@Entity
@Data
public class formation_employe {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "employe_id")
	    private Employe employe;

	    @ManyToOne
	    @JoinColumn(name = "formation_id")
	    private Formation formation;

	    @Lob // Permet de stocker de grandes quantités de données
	    private byte[] document; // Contenu binaire du fichier PDF
	    private boolean evalue = false;

	    @Enumerated(EnumType.STRING) // Stocke la valeur de l'enum sous forme de chaîne
	    private ResultatFormation resultat; // Utilisation de l'enum

	  
	    private boolean Res = false;
	    
	    
	    
	    @Column(columnDefinition = "boolean default false")
	    private boolean capabilite = false;
}