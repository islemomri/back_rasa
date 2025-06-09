package com.project.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Data
public class Poste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
        private String titre;
    
    private String niveauExperience; 
    private String diplomeRequis;
    private String competencesRequises;
    private boolean archive = false;
    
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "poste_direction",  // Nom de la table de jointure
        joinColumns = @JoinColumn(name = "poste_id"),
        inverseJoinColumns = @JoinColumn(name = "direction_id")
    )
    private Set<Direction> directions;  // Set pou
    public Poste() {
        this.archive = false;
    }

    public void archiver() {
        this.archive = true;
    }

    public void desarchiver() {
        this.archive = false;
    }
    @OneToMany(mappedBy = "poste", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<EmployePoste> employePostes;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "poste_competence_poste",
        joinColumns = @JoinColumn(name = "poste_id"),
        inverseJoinColumns = @JoinColumn(name = "competence_poste_id")
    )
    private Set<CompetencePoste> competencePostes = new HashSet<>();
    
    @ElementCollection
    @CollectionTable(name = "poste_programmes_formation", joinColumns = @JoinColumn(name = "poste_id"))
    @Column(name = "programme")
    private List<String> lesProgrammesDeFormation;

}