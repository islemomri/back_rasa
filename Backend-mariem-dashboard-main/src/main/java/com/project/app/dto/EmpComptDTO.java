package com.project.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmpComptDTO {
	private Long id;
    private String nom;
    private String prenom;
    private Integer matricule;
    private String niveau;
}
