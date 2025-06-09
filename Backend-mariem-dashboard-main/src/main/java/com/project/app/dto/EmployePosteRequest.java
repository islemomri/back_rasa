package com.project.app.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployePosteRequest {
    private Long employeId;
    private Long posteId;
    private Long directionId;
    private Long siteId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
