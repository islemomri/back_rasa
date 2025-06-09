package com.project.app.service;

import java.util.List;

import com.project.app.dto.FormationDto;
import com.project.app.model.Formation;

public interface IFormationService {
    Formation creerFormation(FormationDto formationDto, Long rhId);
    List<Formation> getFormationsParRH(Long rhId);
    
}

