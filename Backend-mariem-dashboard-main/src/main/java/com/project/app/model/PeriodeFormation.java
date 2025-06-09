package com.project.app.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PeriodeFormation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String formateur; 
    private String programme; 
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;

   
    
    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false) // Ajoutez nullable=false pour forcer la relation
    @JsonIgnore
    private Formation formation;
}