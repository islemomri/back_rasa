package com.project.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.EmployeCompetenceDTO;
import com.project.app.model.EmployeCompetence;
import com.project.app.service.EmployeCompetenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/employecompetences")
@RequiredArgsConstructor
public class EmployeCompetenceController {

    private final EmployeCompetenceService employeCompetenceService;

    @GetMapping("/employes/{id}/competences")
    public List<EmployeCompetence> getCompetencesByEmploye(@PathVariable("id") Long employeId) {
        return employeCompetenceService.getCompetencesByEmploye(employeId);
    }

    @PostMapping("/employes/{id}/competences")
    public ResponseEntity<?> addCompetenceToEmploye(@PathVariable("id") Long employeId,
                                                    @RequestBody EmployeCompetenceDTO dto) {
        try {
            EmployeCompetence ec = employeCompetenceService.addCompetenceToEmploye(employeId, dto);
            return ResponseEntity.ok(ec);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/employe-competences/{id}")
    public ResponseEntity<?> updateNiveau(@PathVariable("id") Long associationId,
                                          @RequestBody EmployeCompetenceDTO dto) {
        try {
            EmployeCompetence ec = employeCompetenceService.updateNiveau(associationId, dto.getNiveau());
            return ResponseEntity.ok(ec);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/employe-competences/{id}")
    public ResponseEntity<?> deleteAssociation(@PathVariable("id") Long associationId) {
        try {
            employeCompetenceService.deleteAssociation(associationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
