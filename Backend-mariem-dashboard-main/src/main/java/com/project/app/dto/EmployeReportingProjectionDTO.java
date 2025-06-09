package com.project.app.dto;

import java.time.LocalDate;

public record EmployeReportingProjectionDTO(
	    String nom,
	    String prenom,
	    Integer matricule,
	    String email,
	    String posteActuel,
	    String diplomeLibelle,
	    String typeDiplome,
	    LocalDate dateObtention,
	    String direction,
	    String societe,
	    String posteExperience,
	    LocalDate dateDebut,
	    LocalDate dateFin
	) {}

