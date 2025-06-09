package com.project.app.dto;

import java.time.LocalDate;

import lombok.Data;
@Data
public class DiplomeRequest {
    private Long employeId;
    private Long typeDiplomeId;
    private Long idType;
    private String libelleTypeDiplome;
    private String libelle;
    private LocalDate dateObtention;
}
