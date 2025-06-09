package com.project.app.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class EmployeUpdateDTO {
    private String nom;
    private String prenom;
    private Integer matricule;
    private Boolean actif; 
    private LocalDate dateNaissance;
    private LocalDate dateRecrutement;
    private String sexe;
    private String email;
    private String site;
    private String direction;
    private String photo;

}
