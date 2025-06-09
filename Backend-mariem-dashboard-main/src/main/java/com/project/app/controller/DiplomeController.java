package com.project.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.AssignDiplomeRequest;
import com.project.app.dto.DiplomeRequest;
import com.project.app.model.Diplome;

import com.project.app.service.IDiplomeService;


@RestController
@RequestMapping("/diplomes")
public class DiplomeController {
	
	@Autowired
	private IDiplomeService diplomeService;
	
	@PostMapping("/import")
    public String importDiplomes(@RequestBody List<Map<String, String>> diplomeList) {
        for (Map<String, String> row : diplomeList) {
            Long idType = Long.parseLong(row.get("NRIType_Diplome"));
            String libelleTypeDiplome = row.get("LibelleTypeDiplome");
            String libelle = row.get("Libelle");
            diplomeService.saveDiplome(idType, libelleTypeDiplome, libelle);
        }
        return "Importation terminée avec succès !";
    }
	
	// Ajouter ou associer un diplôme à un employé
	@PostMapping("/assign/{employeId}/{diplomeId}")
	public ResponseEntity<Diplome> assignDiplomeToEmploye(
	    @PathVariable("employeId") Long employeId,
	    @PathVariable("diplomeId") Long diplomeId,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateObtention
	) {
	    Diplome diplome = diplomeService.assignDiplomeToEmploye(employeId, diplomeId, dateObtention);
	    return ResponseEntity.ok(diplome);
	}

	@PutMapping("/update-assignment/{oldDiplomeId}/{employeId}/{newDiplomeId}")
	public ResponseEntity<Diplome> updateDiplomeAssignment(
	    @PathVariable("oldDiplomeId") Long oldDiplomeId,
	    @PathVariable("employeId") Long employeId,
	    @PathVariable("newDiplomeId") Long newDiplomeId,
	    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateObtention
	) {
	    Diplome diplome = diplomeService.updateDiplomeAssignment(oldDiplomeId, employeId, newDiplomeId, dateObtention);
	    return ResponseEntity.ok(diplome);
	}



    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<Diplome>> getDiplomesByEmploye(@PathVariable("employeId") Long employeId) {
        List<Diplome> diplomes = diplomeService.getDiplomesByEmploye(employeId);

        
        diplomes.forEach(d -> System.out.println("Diplôme: " + d.getLibelle() + ", Type: " + 
            (d.getTypeDiplome() != null ? d.getTypeDiplome().getLibelleTypeDiplome() : "null")));

        return ResponseEntity.ok(diplomes);
    }


    


    @DeleteMapping("/delete/{diplomeId}/{employeId}")
    public ResponseEntity<String> deleteDiplome(
        @PathVariable("diplomeId") Long diplomeId, 
        @PathVariable("employeId") Long employeId
    ) {
        diplomeService.deleteDiplome(diplomeId, employeId);
        return ResponseEntity.noContent().build();


    }
    
    @GetMapping("/all")
    public List<Diplome> getAllDiplomes() {
        return diplomeService.getAllDiplomes();
    }
    
    @PostMapping("/{id_type}")
    public ResponseEntity<Diplome> addDiplome(@PathVariable("id_type") Long id,@RequestBody DiplomeRequest request) {
        Diplome diplome = diplomeService.saveDiplome(id,request.getLibelleTypeDiplome(), request.getLibelle());
        return ResponseEntity.ok(diplome);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Diplome> getDiplomeById(@PathVariable("id") Long id) {
        Optional<Diplome> diplome = diplomeService.getDiplomeById(id);
        return diplome.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
	
    @PutMapping("/{id}")
	public ResponseEntity<Diplome> updateDiplome(@PathVariable("id") Long id, @RequestBody DiplomeRequest request) {
	    System.out.println("Mise à jour du diplôme avec ID: " + id);
	    System.out.println("Données reçues: " + request.toString());

	    Diplome updatedDiplome = diplomeService.updateDiplome(id, request.getIdType(), request.getLibelleTypeDiplome(), request.getLibelle());
	    return ResponseEntity.ok(updatedDiplome);
	}
    	
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiplomePermanently(@PathVariable("id") Long id) {
        System.out.println("Suppression du diplôme avec ID: " + id);
        diplomeService.deleteDiplomePermanently(id);
        return ResponseEntity.noContent().build();
    }

}
