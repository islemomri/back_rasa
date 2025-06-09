package com.project.app.dto;

import java.time.LocalDate;

import com.project.app.model.SousTypeFormation;
import com.project.app.model.TypeFormation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormationDTO2 {
	private Long formationId;
    private String titre;
    private TypeFormation typeFormation;
    private SousTypeFormation sousTypeFormation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long employeId;
}
