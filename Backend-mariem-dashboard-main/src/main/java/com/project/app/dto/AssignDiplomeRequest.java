package com.project.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AssignDiplomeRequest {
    private Long employeId;
    private Long diplomeId;
    private LocalDate dateObtention;
}