package com.project.app.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Employe {
    
    // Identifiant
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informations personnelles
    private String nom;
    private String prenom;
    private Integer matricule;
    private String sexe;
    private LocalDate dateNaissance;
    private String email;
    private String photo;
    private boolean actif;
    private boolean ajout;
    
    // Dates importantes
    private LocalDate dateRecrutement;

    // Relations
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Discipline> disciplines = new ArrayList<>(); 
    
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Experience> experiences = new ArrayList<>();
    
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Stage> stages = new ArrayList<>(); 
    
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EmployePoste> employePostes = new ArrayList<>();
    
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Diplome> diplomes = new ArrayList<>();
    
    @ManyToMany(mappedBy = "employes")
    @JsonIgnore
    private List<Formation> formations = new ArrayList<>();
    
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<EmployeCompetence> employeCompetences = new ArrayList<>();

}