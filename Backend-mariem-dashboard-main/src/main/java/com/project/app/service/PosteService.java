package com.project.app.service;

import com.project.app.dto.PosteDTO;
import com.project.app.model.CompetencePoste;
import com.project.app.model.Direction;
import com.project.app.model.Poste;
import com.project.app.repository.CompetencePosteRepository;
import com.project.app.repository.DirectionRepository;
import com.project.app.repository.PosteRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PosteService implements IPosteService {

    @Autowired
    private PosteRepository posteRepository;
    @Autowired
    private DirectionRepository directionRepository;
    
    @Autowired
    private CompetencePosteRepository competencePosteRepository;

    @Override
    public Poste ajouterPoste(Poste poste) {
        return posteRepository.save(poste);
    }

    @Override
    public List<Poste> getAllPostes() {
        return posteRepository.findAll();
    }

    @Override
    public Poste getPosteById(Long id) {
        return posteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poste non trouvé avec l'ID : " + id));
    }

    @Override
    @Transactional
    public Poste updatePoste(Long id, PosteDTO posteDto) throws IOException {
        Poste poste = posteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poste introuvable avec l'ID : " + id));

        // Mettre à jour les champs de base
        poste.setTitre(posteDto.getTitre());
        poste.setNiveauExperience(posteDto.getNiveauExperience());
        poste.setDiplomeRequis(posteDto.getDiplomeRequis());
        poste.setCompetencesRequises(posteDto.getCompetencesRequises());
        poste.setLesProgrammesDeFormation(posteDto.getLesProgrammesDeFormation());

     // Mettre à jour les compétences associées
        if (posteDto.getCompetencePosteIds() != null) {
            Set<CompetencePoste> competencePostes = posteDto.getCompetencePosteIds().stream()
                .map(competencePosteRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

            poste.setCompetencePostes(competencePostes);
        }

       
        // Mettre à jour les directions
        Set<Direction> directions = posteDto.getDirectionIds() != null
                ? posteDto.getDirectionIds().stream()
                  .map(directionRepository::findById)
                  .filter(java.util.Optional::isPresent)
                  .map(java.util.Optional::get)
                  .collect(Collectors.toSet())
                : new HashSet<>();

        poste.setDirections(directions);

        // Mettre à jour le fichier si présent
      
        
        return posteRepository.save(poste);  // Sauvegarder les modifications
    }
    
	@Override
	@Transactional

	 public Poste addPosteWithDirections(PosteDTO posteDTO) throws IOException {
        Poste poste = new Poste();
        poste.setTitre(posteDTO.getTitre());
        poste.setNiveauExperience(posteDTO.getNiveauExperience());
        poste.setDiplomeRequis(posteDTO.getDiplomeRequis());
        poste.setCompetencesRequises(posteDTO.getCompetencesRequises());
        poste.setLesProgrammesDeFormation(posteDTO.getLesProgrammesDeFormation());
        
     // Mettre à jour les compétences associées
     // Ajouter les compétences associées
        if (posteDTO.getCompetencePosteIds() != null) {
            Set<CompetencePoste> competencePostes = posteDTO.getCompetencePosteIds().stream()
                .map(competencePosteRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

            poste.setCompetencePostes(competencePostes);
        }


        
        if (posteDTO.getCompetencePosteIds() != null) {
            Set<CompetencePoste> competencePostes = posteDTO.getCompetencePosteIds().stream()
                .map(competencePosteRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

            poste.setCompetencePostes(competencePostes);
        }

        // Récupérer les directions par leurs IDs
        Set<Direction> directions = new HashSet<>();
        for (Long directionId : posteDTO.getDirectionIds()) {
            Direction direction = directionRepository.findById(directionId).orElse(null);
            if (direction != null) {
                directions.add(direction);
            }
        }

        poste.setDirections(directions);

        // Ajouter le fichier Word dans le modèle si présent
      
       


        return posteRepository.save(poste);  // Sauvegarder le poste dans la base de données
    }

    @Override
    public void deletePoste(Long id) {
        Poste poste = posteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poste non trouvé avec l'ID : " + id));
        posteRepository.delete(poste);
    }

	@Override
	@Transactional
	public List<Poste> getAllPostesnonArchivés() {
		return posteRepository.findByArchiveFalse();
	}

	@Override
	public Poste archiverPoste(Long id) {
		Poste Poste = posteRepository.findById(id).orElseThrow(() -> new RuntimeException("Direction not found"));
		Poste.archiver(); 
        return posteRepository.save(Poste);
	}

	@Override
	 @Transactional
	public List<Poste> getAllPostesArchivés() {
		return posteRepository.findByArchiveTrue();
	}

	@Override
	public Poste desarchiverPoste(Long id) {
		Poste Poste = posteRepository.findById(id).orElseThrow(() -> new RuntimeException("Direction non trouvée"));
		Poste.desarchiver();
        return posteRepository.save(Poste);
	}
	


	@Override
	public Optional<Set<Direction>> getDirectionsByPosteId(Long id) {
		return posteRepository.findById(id).map(Poste::getDirections);
	}
	
	
	
}