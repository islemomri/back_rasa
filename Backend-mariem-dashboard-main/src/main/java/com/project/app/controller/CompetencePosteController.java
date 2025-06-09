package com.project.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.CompetencePoste;
import com.project.app.service.ICompetencePosteService;

@RestController
@RequestMapping("/competences_poste")
public class CompetencePosteController {
	
	@Autowired
    private ICompetencePosteService competenceService;

	@PostMapping
    public ResponseEntity<CompetencePoste> create(@RequestBody CompetencePoste competence) {
        return new ResponseEntity<>(competenceService.create(competence), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompetencePoste> update(@PathVariable("id") Long id, @RequestBody CompetencePoste competence) {
        return ResponseEntity.ok(competenceService.update(id, competence));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        competenceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetencePoste> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(competenceService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CompetencePoste>> getAll() {
        return ResponseEntity.ok(competenceService.getAll());
    }
}
