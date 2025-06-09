package com.project.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeBaseInfoDTO {
    private Long id;
    private String nom;
    private String prenom;
    private Integer matricule;
    private String email;
    private String posteActuel;
    private String direction;
}
