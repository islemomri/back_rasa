package com.project.app.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.project.app.model.Competence;
import com.project.app.repository.CompetenceRepository;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompetenceService {

    private final CompetenceRepository competenceRepository;

    public List<Competence> getAll() {
        return competenceRepository.findAll();
    }

    public Competence create(Competence competence) {
        
        boolean exists = competenceRepository.existsByNomIgnoreCase(competence.getNom());
        if (exists) {
            throw new RuntimeException("Une compétence avec ce nom existe déjà.");
        }
        return competenceRepository.save(competence);
    }

    public Competence update(Long id, Competence updated) {
        Competence existing = competenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compétence non trouvée"));

       
        Optional<Competence> other = competenceRepository.findByNomIgnoreCase(updated.getNom());
        if (other.isPresent() && !other.get().getId().equals(id)) {
            throw new RuntimeException("Ce nom est déjà utilisé par une autre compétence.");
        }

        existing.setNom(updated.getNom());
        return competenceRepository.save(existing);
    }

    public void delete(Long id) {
        Competence competence = competenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compétence non trouvée"));

        if (!competence.getEmployeCompetences().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer cette compétence car elle est associée à un ou plusieurs employés.");
        }

        competenceRepository.delete(competence);
    }

}
