package com.project.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.app.model.ResultatFormation;

import lombok.Data;
@Data

public class EmployeResultatDto {
	 @JsonProperty("employeId") 
	 private Long employeId;
	    private ResultatFormation resultat;
}