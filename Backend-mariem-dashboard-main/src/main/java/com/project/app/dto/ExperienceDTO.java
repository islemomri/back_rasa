package com.project.app.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienceDTO {
	private Long employeId;
    private String societe;
    private String poste;
    private Date dateDebut;
    private Date dateFin;
}