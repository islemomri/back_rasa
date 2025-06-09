package com.project.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeDTO {
    private String nom;
    private String prenom;
    private Integer matricule;
    private LocalDate dateNaissance;
    private LocalDate dateRecrutement;
}
