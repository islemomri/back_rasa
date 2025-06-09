package com.project.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.CompetencePoste;
import com.project.app.repository.CompetencePosteRepository;

@Service
public class CompetencePosteService implements ICompetencePosteService{
	
	@Autowired
    private CompetencePosteRepository repository;
	
	@Override
	public CompetencePoste create(CompetencePoste competence) {
		// Vérifier si la compétence avec le même nom existe déjà
		Optional<CompetencePoste> existingCompetence = repository.findByNom(competence.getNom());
		if (existingCompetence.isPresent()) {
			throw new RuntimeException("Une compétence avec ce nom existe déjà");
		}
		return repository.save(competence);
	}

	@Override
	public CompetencePoste update(Long id, CompetencePoste competence) {
        CompetencePoste existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compétence non trouvée"));
        existing.setNom(competence.getNom());
        existing.setDescription(competence.getDescription());
        return repository.save(existing);
    }
	
	@Override
	public void delete(Long id) {
		repository.deleteById(id);
		
	}

	@Override
	public CompetencePoste getById(Long id) {
		return repository.findById(id).orElseThrow(() -> new RuntimeException("Compétence non trouvée"));
	}

	@Override
	public List<CompetencePoste> getAll() {
		return repository.findAll();
	}

}
