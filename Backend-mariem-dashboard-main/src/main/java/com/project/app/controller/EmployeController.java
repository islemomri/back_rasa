package com.project.app.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.EmployeDTO;
import com.project.app.dto.EmployeHabilitationDto;
import com.project.app.dto.EmployePosteDetailDto;
import com.project.app.dto.EmployePosteRequest;
import com.project.app.dto.EmployeUpdateDTO;
import com.project.app.dto.PosteAvecDatesDTO;
import com.project.app.model.Discipline;
import com.project.app.model.Employe;
import com.project.app.model.EmployePoste;
import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;
import com.project.app.model.Poste;
import com.project.app.repository.EmployePosteRepository;
import com.project.app.service.IEmployeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/api/employes")
public class EmployeController {
	@Autowired
    private IEmployeService employeService;
	
	@Autowired
	private EmployePosteRepository employePosteRepository;
    @GetMapping
    public List<Employe> getAllEmployes() {
        return employeService.getAllEmployes();
    }

    @PostMapping
    public Employe addEmploye(@RequestBody Employe employe) {
        return employeService.addEmploye(employe);
    }

    @GetMapping("/{id}")
    public Optional<Employe> getEmployeById(@PathVariable ("id") Long id) {
        return employeService.getEmployeById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEmploye(@PathVariable ("id") Long id) {
        employeService.deleteEmploye(id);
    }
    
    
    @PostMapping("/addWithExperience")
    public ResponseEntity<Employe> addEmployeWithExperience(@RequestBody Employe employe) {
        Employe savedEmploye = employeService.addEmployeWithExperience(employe);
        return new ResponseEntity<>(savedEmploye, HttpStatus.CREATED);
    }
  

    @PostMapping("/test")
    public ResponseEntity<Employe> ajouterEmploye(@RequestBody EmployeDTO employeDTO) {
        Employe nouvelEmploye = employeService.ajouterEmploye(employeDTO);
        return ResponseEntity.ok(nouvelEmploye);
    }
    

 
    
    @GetMapping("/{id}/disciplines")
    public List<Discipline> getAllDisciplines(@PathVariable ("id") Long id) {
        return employeService.getAllDisciplines(id);
    }
    
    @GetMapping("/employes/{id}/experiences/assad")
    public List<ExperienceAssad> getAllExperiencesAssadByEmployeId(@PathVariable ("id") Long id) {
        return employeService.getAllExperiencesAssadByEmployeId(id);
    }

    // API pour obtenir la liste des expériences antérieures d'un employé par son ID
    @GetMapping("/employes/{id}/experiences/anterieures")
    public List<ExperienceAnterieure> getAllExperiencesAnterieuresByEmployeId(@PathVariable ("id") Long id) {
        return employeService.getAllExperiencesAnterieuresByEmployeId(id);
    }
    
    
    
    @PostMapping("/ajouterAvecPoste")
    public ResponseEntity<PosteAvecDatesDTO> ajouterPosteAEmploye(
            @RequestParam ("employeId")Long employeId,
            @RequestParam ("posteId")Long posteId,
            @RequestParam  ("directionId") Long directionId,
            @RequestParam  ("siteId")Long siteId,
            @RequestParam ("dateDebut")LocalDate dateDebut,
            @RequestParam  (value="dateFin",required = false) LocalDate dateFin) {  // dateFin est maintenant optionnelle

        try {
            // Appel du service pour ajouter le poste à l'employé et obtenir le DTO
            PosteAvecDatesDTO posteAvecDatesDTO = employeService.ajouterPosteAEmploye(employeId, posteId, directionId, siteId, dateDebut, dateFin);

            // Retourner une réponse avec le DTO du poste
            return ResponseEntity.ok(posteAvecDatesDTO);
        } catch (RuntimeException e) {
            // Gérer les exceptions si l'employé, le poste, la direction ou le site n'existent pas
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<EmployePoste> getPosteDetails(
            @RequestParam("employeId") Long employeId,
            @RequestParam("posteId") Long posteId) {

        Optional<EmployePoste> employePoste = employeService.getPosteDetailsByEmployeIdAndPosteId(employeId, posteId);
        
        return employePoste
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/details/{employePosteId}")
    public ResponseEntity<EmployePoste> getPosteDetails(
            @PathVariable ("employePosteId")Long employePosteId) {
        
        EmployePoste employePoste = employeService.getPosteDetailsById(employePosteId);
        return ResponseEntity.ok(employePoste);
    }
    @GetMapping("/postes/{employeId}")
    @Transactional
    public List<PosteAvecDatesDTO> getPostesByEmploye(@PathVariable("employeId") Long employeId) {
        return employePosteRepository.findByEmployeId(employeId)
            .stream()
            .map(ep -> new PosteAvecDatesDTO(
                ep.getId(), // ← Ajouter l'ID de l'association
                ep.getPoste().getId(),
                ep.getPoste().getTitre(),
                ep.getDateDebut(),
                ep.getDateFin(),
                ep.getNomDirection(),
                ep.getNomSite()
            ))
            .collect(Collectors.toList());
    }
    


    @DeleteMapping("/delete")
    public ResponseEntity<String> supprimerPoste(@RequestParam ("employeId") Long employeId, @RequestParam ("posteId") Long posteId) {
    	employeService.supprimerPostePourEmploye(employeId, posteId);
        return ResponseEntity.ok("Poste supprimé avec succès !");
    }
    
    @PostMapping("/ajouter")
    public Employe ajouterEmployeAvecPoste(@RequestParam ("posteId")Long posteId,
                                           @RequestParam ("directionId") Long directionId,
                                           @RequestParam ("siteId")Long siteId,
                                           @RequestBody Employe employe,
                                           @RequestParam("dateDebut") String dateDebut,
                                           @RequestParam  ("dateFin")String dateFin) {
        // Convertir les dates
        LocalDate dateDebutLocal = LocalDate.parse(dateDebut);
        LocalDate dateFinLocal = LocalDate.parse(dateFin);

        // Ajouter l'employé avec son poste
        return employeService.ajouterEmployeAvecPoste(posteId, directionId, siteId, employe, dateDebutLocal, dateFinLocal);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Employe> modifierEmploye(
            @PathVariable ("id") Long id,
            @RequestBody Employe employe,
            @RequestParam( value="posteId",required = false) Long posteId,
            @RequestParam(value="directionId",required = false) Long directionId,
            @RequestParam(value="siteId",required = false) Long siteId,
            @RequestParam(value="dateDebut",required = false) LocalDate dateDebut,
            @RequestParam(value="dateFin",required = false) LocalDate dateFin) {
        
        Employe updatedEmploye = employeService.modifierEmploye(id, employe, posteId, directionId, siteId, dateDebut, dateFin);
        return ResponseEntity.ok(updatedEmploye);
    }
    @GetMapping("/employes-without-poste")
    public List<Employe> getEmployesWithoutPoste() {
        return employeService.getEmployesWithoutPoste();
    }
    
    
    @GetMapping("/document")
    public ResponseEntity<byte[]> getDocumentByEmployeIdAndFormationId(
            @RequestParam  ("employeId")Long employeId,
            @RequestParam ("formationId") Long formationId) {

        // Récupérer le document depuis le service
        byte[] document = employeService.getDocumentByEmployeIdAndFormationId(employeId, formationId);

        if (document != null) {
            // Retourner le document avec un statut OK
            return ResponseEntity.ok(document);
        } else {
            // Retourner un statut NOT_FOUND si aucun document n'est trouvé
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/changer-poste")
    public ResponseEntity<PosteAvecDatesDTO> changerPosteEmploye(
            @RequestParam ("employeId") Long employeId,
            @RequestParam ("nouveauPosteId") Long nouveauPosteId,
            @RequestParam ("directionId")Long directionId,
            @RequestParam ("siteId") Long siteId) {
        
        try {
            PosteAvecDatesDTO result = employeService.changerPosteEmploye(employeId, nouveauPosteId, directionId, siteId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @DeleteMapping("/poste/{employePosteId}")
    public ResponseEntity<String> supprimerPoste(@PathVariable ("employePosteId")Long employePosteId) {
        employeService.supprimerPostePourEmployeParId(employePosteId);
        return ResponseEntity.ok("Poste supprimé avec succès !");
    }

    @PutMapping("/poste/{employePosteId}")
    public ResponseEntity<PosteAvecDatesDTO> modifierPosteAEmploye(
            @PathVariable("employePosteId") Long employePosteId,
            @RequestParam( value="posteId",required = false) Long posteId,
            @RequestParam("directionId") Long directionId,
            @RequestParam("siteId") Long siteId,
            @RequestParam("dateDebut") LocalDate dateDebut,
            @RequestParam( value="dateFin",required = false) LocalDate dateFin) {

    	try {
            PosteAvecDatesDTO dto = employeService.modifierPosteComplet(
                employePosteId, 
                posteId, // Maintenant utilisé
                directionId, 
                siteId, 
                dateDebut, 
                dateFin);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }


    @GetMapping("/habilitations-proches")
    public List<EmployeHabilitationDto> getEmployesAvecPostesHabilitesProches() {
        return employeService.getEmployesAvecPostesHabilitesProches();
    }
    
    
    @GetMapping("/{id}/poste-actuel/direction")
    public ResponseEntity<String> getNomDirectionPosteActuel(@PathVariable("id") Long id) {
        String nomDirection = employeService.getNomDirectionPosteActuel(id);
        if (nomDirection != null) {
            return ResponseEntity.ok(nomDirection);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @GetMapping("/employe-poste-detail")
    public ResponseEntity<EmployePosteDetailDto> getEmployePosteDetail(
            @RequestParam String titrePoste,
            @RequestParam Integer matricule) {

        EmployePosteDetailDto detail = employeService.getEmployeAvecPosteParMatriculeEtTitre(titrePoste, matricule);
        if (detail != null) {
            return ResponseEntity.ok(detail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    
    
    
    
    
    
    
    
    
    
}