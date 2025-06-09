package com.project.app.service;


import java.util.List;

import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;

public interface IExperience {
	 public ExperienceAnterieure addExperienceAnterieure(Long employeId, ExperienceAnterieure experience);
	 public ExperienceAssad addExperienceAssad(Long employeId, ExperienceAssad experience);
	 public ExperienceAssad modifierExperienceAssad(Long id, ExperienceAssad nouvelleExperience) ;
	 public ExperienceAnterieure modifierExperienceAnterieure(Long id, ExperienceAnterieure nouvelleExperience);
	 public List<ExperienceAssad> getAllExperienceAssadByEmployeId(Long employeId);
	 public List<ExperienceAnterieure> getAllExperienceAnterieureByEmployeId(Long employeId);
	 public void deleteExperienceAssad(Long employeId, Long experienceId) ;
	 public void deleteExperienceAnterieure(Long employeId, Long experienceId);

}
