package com.project.app.dto;

import java.util.List;

import lombok.Data;

@Data
public class PosteListDTO {
    private Long id;
    private String titre;
    private String niveauExperience;
    private String diplomeRequis;
    private String competencesRequises;
    private boolean archive;
    private List<String> lesProgrammesDeFormation;
    
    
}