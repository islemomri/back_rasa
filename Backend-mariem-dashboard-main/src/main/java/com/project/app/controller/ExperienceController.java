package com.project.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;
import com.project.app.service.ExperienceService;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {
	@Autowired
    private ExperienceService experienceService;	
	
	@PostMapping("/anterieure/{employeId}")
    public ResponseEntity<ExperienceAnterieure> addExperienceAnterieure(
            @PathVariable("employeId") Long employeId,
            @RequestBody ExperienceAnterieure experience) {
        ExperienceAnterieure savedExperience = experienceService.addExperienceAnterieure(employeId, experience);
        return ResponseEntity.ok(savedExperience);
    }

 
    @PostMapping("/assad/{employeId}")
    public ResponseEntity<ExperienceAssad> addExperienceAssad(
            @PathVariable ("employeId")Long employeId,
            @RequestBody ExperienceAssad experience) {
        ExperienceAssad savedExperience = experienceService.addExperienceAssad(employeId, experience);
        return ResponseEntity.ok(savedExperience);
    }
    @PutMapping("/assad/{id}")
    public ResponseEntity<ExperienceAssad> modifierExperienceAssad(
            @PathVariable ("id")Long id,
            @RequestBody ExperienceAssad nouvelleExperience) {
        
        ExperienceAssad experienceModifiee = experienceService.modifierExperienceAssad(id, nouvelleExperience);
        return ResponseEntity.ok(experienceModifiee);
    }

    // Modifier une expérience Anterieure
    @PutMapping("/anterieure/{id}")
    public ResponseEntity<ExperienceAnterieure> modifierExperienceAnterieure(
            @PathVariable ("id") Long id,
            @RequestBody ExperienceAnterieure nouvelleExperience) {
        
        ExperienceAnterieure experienceModifiee = experienceService.modifierExperienceAnterieure(id, nouvelleExperience);
        return ResponseEntity.ok(experienceModifiee);
    }
    
    
    
    
    @GetMapping("/assad/{employeId}")
    public ResponseEntity<List<ExperienceAssad>> getExperiencesAssad(@PathVariable ("employeId") Long employeId) {
        List<ExperienceAssad> experiences = experienceService.getAllExperienceAssadByEmployeId(employeId);
        return ResponseEntity.ok(experiences);
    }

    // Endpoint pour récupérer les expériences de type "Antérieure" d'un employé
    @GetMapping("/anterieure/{employeId}")
    public ResponseEntity<List<ExperienceAnterieure>> getExperiencesAnterieure(@PathVariable ("employeId")Long employeId) {
        List<ExperienceAnterieure> experiences = experienceService.getAllExperienceAnterieureByEmployeId(employeId);
        return ResponseEntity.ok(experiences);
    }
    
    
    @DeleteMapping("/experienceAssad/{employeId}/{experienceId}")
    public void deleteExperienceAssad(
        @PathVariable ("employeId") Long employeId, 
        @PathVariable ("experienceId") Long experienceId) {
        experienceService.deleteExperienceAssad(employeId, experienceId);
    }

    
    @DeleteMapping("/experienceAnterieure/{employeId}/{experienceId}")
    public void deleteExperienceAnterieure(
        @PathVariable ("employeId") Long employeId, 
        @PathVariable ("experienceId") Long experienceId) {
        experienceService.deleteExperienceAnterieure(employeId, experienceId);
    }
    
    
    
    
}
