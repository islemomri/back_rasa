package com.project.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.app.model.Diplome;
import com.project.app.model.TypeDiplome;
import com.project.app.service.TypeDiplomeService;

@RestController
@RequestMapping("/typediplomes")
public class TypeDiplomeController {

    @Autowired
    private TypeDiplomeService typeDiplomeService;

   
    @PostMapping("/add")
    public ResponseEntity<TypeDiplome> addTypeDiplome(@RequestBody TypeDiplome typeDiplome) {
        TypeDiplome savedTypeDiplome = typeDiplomeService.saveTypeDiplome(typeDiplome.getLibelleTypeDiplome());
        return ResponseEntity.ok(savedTypeDiplome);
    }


  
    
    @GetMapping("/{id}")
    public ResponseEntity<TypeDiplome> getTypeDiplomeById(@PathVariable("id") Long id) {
        Optional<TypeDiplome> typeDiplome = typeDiplomeService.getTypeDiplomeById(id);
        return typeDiplome.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TypeDiplome> updateTypeDiplome(@PathVariable("id") Long id, @RequestBody TypeDiplome typeDiplome) {
        TypeDiplome updatedTypeDiplome = typeDiplomeService.updateTypeDiplome(id, typeDiplome.getLibelleTypeDiplome());
        return ResponseEntity.ok(updatedTypeDiplome);
    }


   
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTypeDiplome(@PathVariable("id") Long id) {
        typeDiplomeService.deleteTypeDiplome(id);
        return ResponseEntity.ok("TypeDiplome supprimé avec succès !");
    }
    
    
    @PutMapping("/archiver/{id}")
	public TypeDiplome archiverTypeDiplome(@PathVariable("id") long id) {
		return typeDiplomeService.archiverTypeDiplome(id);
	}
    @GetMapping
	public List<TypeDiplome> getallTypeDiplomenonArchives() {
		return typeDiplomeService.getAllTypeDiplomenonArchives();
	}
    @GetMapping("/getallTypeDiplomeArchives")
   	public List<TypeDiplome> getallTypeDiplomeArchives() {
   		return typeDiplomeService.getAllTypeDiplomeArchives();
   	}
    @PutMapping("/desarchiver/{id}")
   	public TypeDiplome desarchiverTypeDiplome(@PathVariable("id") long id) {
   		return typeDiplomeService.desarchiverTypeDiplome(id);
   	}
    
    
}
