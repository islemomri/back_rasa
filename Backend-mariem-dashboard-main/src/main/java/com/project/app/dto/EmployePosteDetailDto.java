package com.project.app.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployePosteDetailDto {
    private Long id;
    private String nom;
    private String prenom;
    private Integer matricule;
    private String email;
    private List<String> competences;
    private PosteDtoHabilite posteHabilite;  // un seul poste ici
}
