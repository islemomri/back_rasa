package com.project.app.dto;

import java.util.List;
import lombok.Data;

@Data
public class DirectionDTO {
	 private Long id; 
    private String nom_direction;
    private List<Long> siteIds;
}
