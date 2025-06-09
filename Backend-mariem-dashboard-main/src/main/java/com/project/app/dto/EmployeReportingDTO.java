package com.project.app.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeReportingDTO {
	private String nom;
    private String prenom;
    private Integer matricule;
    private String email;
    private String posteActuel;
    private String diplomes; // Chaîne concaténée
    private String typesDiplomes; // Chaîne concaténée
    private String datesObtention; // Chaîne concaténée
    private String direction;
    private String societesExperience; // Chaîne concaténée
    private String postesExperience; // Chaîne concaténée
    private String periodesExperience; // Chaîne concaténée
    private String titresFormations;
    private String typesFormations;
    private String sousTypesFormations;
    private String periodesFormations;

}
