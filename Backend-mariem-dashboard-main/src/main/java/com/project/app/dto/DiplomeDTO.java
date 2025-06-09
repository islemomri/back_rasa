package com.project.app.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public class DiplomeDTO {
		private Long employeId;
	    private String libelle;
	    private String typeDiplome;
	    private LocalDate dateObtention;
	}