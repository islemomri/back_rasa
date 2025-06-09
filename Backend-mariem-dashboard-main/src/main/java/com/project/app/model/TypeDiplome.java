package com.project.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class TypeDiplome {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelleTypeDiplome;
    private boolean archive=false;
    
	public TypeDiplome() {
	
		this.archive = false;
	}
    
	 public void archiver() {
	    	this.archive=true;
	    }

	    public void desarchiver() {
	        this.archive = false;
	    }
    
    
    
}
