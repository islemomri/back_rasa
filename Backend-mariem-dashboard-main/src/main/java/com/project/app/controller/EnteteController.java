package com.project.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.app.model.Entete;
import com.project.app.service.EnteteService;

@RestController
@RequestMapping("/api/entetes")
public class EnteteController {

    private final EnteteService enteteService;

    public EnteteController(EnteteService enteteService) {
        this.enteteService = enteteService;
    }

    @GetMapping
    public List<Entete> getAll() {
        return enteteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entete> getById(@PathVariable("id") Long id) {
        Entete entete = enteteService.getById(id);
        return entete != null ? ResponseEntity.ok(entete) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Entete create(@RequestBody Entete entete) {
        return enteteService.save(entete);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entete> update(@PathVariable("id") Long id, @RequestBody Entete enteteDetails) {
        Entete entete = enteteService.getById(id);
        if (entete == null) {
            return ResponseEntity.notFound().build();
        }

        entete.setLibelle(enteteDetails.getLibelle());
        entete.setReference(enteteDetails.getReference());
        entete.setNumerorevision(enteteDetails.getNumerorevision());
        entete.setDateApplication(enteteDetails.getDateApplication());

        return ResponseEntity.ok(enteteService.save(entete));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ("id")Long id) {
        enteteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
