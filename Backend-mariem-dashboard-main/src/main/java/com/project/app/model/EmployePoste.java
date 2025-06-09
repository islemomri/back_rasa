package com.project.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
public class EmployePoste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employe_id")
    @JsonIgnore
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "poste_id")
    @JsonIgnore
    private Poste poste;

    private LocalDate dateDebut;
    @Column(nullable = true)
   
    private LocalDate dateFin;
    private String nomDirection;
    private String nomSite;
    
  
}
