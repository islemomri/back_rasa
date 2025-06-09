package com.project.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

import com.project.app.dto.NomSiteRequest;
import com.project.app.dto.SiteRequest;
import com.project.app.model.Direction;
import com.project.app.model.Poste;
import com.project.app.model.Site;
import com.project.app.repository.SiteRepository;
import com.project.app.service.DirectionService;
import com.project.app.service.SiteService;

@RestController
@RequestMapping("/api/sites")
public class SiteController {
	 @Autowired
	    private SiteService siteService;
	 @Autowired
	    private DirectionService directionService;
	 @Autowired
	    private SiteRepository siteRepository;
	    
	    @PostMapping("/ajouter")
	    public Site ajouterSite(@RequestBody Site site) {
	        return siteService.ajouterSite(site);
	    }
	  
	    
	    @GetMapping("/non-archives")
	    public List<Site> getAllSitesnonArchivés() {
	        List<Site> sites = new ArrayList<>(siteService.getAllSitesnonArchivés());
	        return sites;
	    }

	    @PutMapping("/modifier-nom")
	    public Site updateSiteName(@RequestBody NomSiteRequest request) {
	        Long id = request.getId();
	        String newNomSite = request.getNom_site();

	        if (newNomSite == null || newNomSite.trim().isEmpty()) {
	            throw new IllegalArgumentException("Le nouveau nom du site ne peut pas être vide.");
	        }

	        Optional<Site> optionalSite = siteRepository.findById(id);
	        if (optionalSite.isPresent()) {
	            Site site = optionalSite.get();
	            site.setNom_site(newNomSite);
	            return siteRepository.save(site);
	        } else {
	            throw new RuntimeException("Site avec ID " + id + " introuvable.");
	        }
	    }


	  
	    @GetMapping("/liste-sites-archivés")
	    public List<Site> getAllsitesArchivés() {
	        return siteService.getAllSitesArchivés();
	    }
	    
	    
	    @PutMapping("/archiver")
	    public ResponseEntity<Site> archiverSite(@RequestBody SiteRequest request) {
	        try {
	            // Utilisation du service pour archiver le site avec l'ID dans le corps
	            Site site = siteService.archiverSite(request.getId());
	            return ResponseEntity.ok(site);  // Retourner le site archivé en réponse
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retour d'une erreur 404 si site non trouvé
	        }
	    }



	    @PutMapping("/desarchiver")
	    public ResponseEntity<Site> desarchiverSite(@RequestBody SiteRequest request) {
	        try {
	            // Utilisation du service pour désarchiver le site avec l'ID dans le corps
	            Site site = siteService.desarchiverSite(request.getId());
	            return ResponseEntity.ok(site);  // Retourner le site désarchivé en réponse
	        } catch (RuntimeException e) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retour d'une erreur 404 si site non trouvé
	        }
	    }



	    
	    
}
