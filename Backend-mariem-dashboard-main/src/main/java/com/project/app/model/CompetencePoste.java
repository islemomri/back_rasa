package com.project.app.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class CompetencePoste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String nom;
    
    private String description; 
    
   /* @ManyToMany(mappedBy = "competenceRequises") // Changed to match Poste's field name
    @JsonIgnore
    private Set<Poste> postes = new HashSet<>();*/
    }