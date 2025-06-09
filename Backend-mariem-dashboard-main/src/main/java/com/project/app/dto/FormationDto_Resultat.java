package com.project.app.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.app.model.SousTypeFormation;
import com.project.app.model.TypeFormation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class FormationDto_Resultat {
    private String titre;
    private String description;
    
    @JsonProperty("typeFormation") // Assure la correspondance avec le JSON
    private TypeFormation typeFormation;
    
    @JsonProperty("sousTypeFormation")
    private SousTypeFormation sousTypeFormation;
    
    @JsonProperty("dateDebutPrevue")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebutPrevue;

    @JsonProperty("dateFinPrevue")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFinPrevue;

    @JsonProperty("responsableEvaluationId")
    private Long responsableEvaluationId;
    @JsonProperty("responsableEvaluationExterne")
    private String responsableEvaluationExterne;
    
    @JsonProperty("employes") // Assure la correspondance
    private List<EmployeResultatDto> employes = new ArrayList<>();
}
    
