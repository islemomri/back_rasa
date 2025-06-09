package com.project.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.app.model.FormationPoste;
import com.project.app.model.Poste;
import com.project.app.service.FormationPosteService;

import java.util.List;

@RestController
@RequestMapping("/api/formation-poste")
public class FormationPosteController {

    @Autowired
    private FormationPosteService formationPosteService;

    // Ajouter une paire
    @PostMapping
    public ResponseEntity<FormationPoste> addPair(@RequestParam ("formationId") Long formationId, @RequestParam ("posteId") Long posteId) {
        FormationPoste pair = formationPosteService.addPair(formationId, posteId);
        return ResponseEntity.ok(pair);
    }

    // Récupérer toutes les paires
    @GetMapping
    public ResponseEntity<List<FormationPoste>> getAllPairs() {
        List<FormationPoste> pairs = formationPosteService.getAllPairs();
        return ResponseEntity.ok(pairs);
    }

    // Supprimer une paire par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePair(@PathVariable ("id")Long id) {
        formationPosteService.deletePair(id);
        return ResponseEntity.noContent().build();
    }

    // Récupérer une paire par ID
    @GetMapping("/{id}")
    public ResponseEntity<FormationPoste> getPairById(@PathVariable ("id") Long id) {
        FormationPoste pair = formationPosteService.getPairById(id);
        return pair != null ? ResponseEntity.ok(pair) : ResponseEntity.notFound().build();
    }
    
    @PutMapping("/formation/{formationId}")
    public ResponseEntity<FormationPoste> updatePosteForFormation(
        @PathVariable ("formationId") Long formationId,
        @RequestBody Long newPosteId) { // Récupérer newPosteId depuis le corps de la requête
        FormationPoste updatedPair = formationPosteService.updatePosteForFormation(formationId, newPosteId);
        return ResponseEntity.ok(updatedPair);
    }
    
    @GetMapping("/poste-by-formation/{formationId}")
    public ResponseEntity<Long> getPosteIdByFormationId(@PathVariable("formationId") Long formationId) {
        Long posteId = formationPosteService.getPosteIdByFormationId(formationId);
        return ResponseEntity.ok(posteId);
    }
    
    @GetMapping("/poste-by-formationid/{formationId}")
    public ResponseEntity<Poste> getPosteByFormationId(@PathVariable("formationId") Long formationId) {
        Poste poste = formationPosteService.getPosteByFormationId(formationId);
        return ResponseEntity.ok(poste);
    }

    
    
    
    
}