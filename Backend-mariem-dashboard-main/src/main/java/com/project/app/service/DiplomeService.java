package com.project.app.service;

import com.project.app.model.Diplome;
import com.project.app.model.Employe;
import com.project.app.model.TypeDiplome;
import com.project.app.repository.DiplomeRepository;
import com.project.app.repository.EmployeRepository;
import com.project.app.repository.TypeDiplomeRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiplomeService implements IDiplomeService{
    
	@Autowired
    private DiplomeRepository diplomeRepository;

    @Autowired
    private TypeDiplomeRepository typeDiplomeRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Transactional
    public Diplome saveOrAssignDiplome(Long employeId, Long diplomeId, LocalDate dateObtention) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

            Diplome diplome = diplomeRepository.findById(diplomeId)
                .orElseThrow(() -> new RuntimeException("Diplôme non trouvé"));

            // Vérifier si le diplôme n'est pas déjà assigné à un autre employé
            if (diplome.getEmploye() != null && !diplome.getEmploye().getId().equals(employeId)) {
                throw new RuntimeException("Ce diplôme est déjà assigné à un autre employé");
            }

            diplome.setEmploye(employe);
            diplome.setDateObtention(dateObtention);
            return diplomeRepository.save(diplome);
        }


    public List<Diplome> getDiplomesByEmploye(Long employeId) {
    	return diplomeRepository.findByEmployeId(employeId).stream()
    			.map(diplome -> {
    			    System.out.println("Diplôme récupéré : " + diplome.getLibelle() + ", Type: " + diplome.getTypeDiplome());
    			    return diplome;
    			}).collect(Collectors.toList());

    }
    @Transactional
    public Diplome updateDiplomeEmploye(Long diplomeId, Long employeId, Long newDiplomeId, LocalDate dateObtention) {
        // 1. Vérifier le diplôme existant
        Diplome existingDiplome = diplomeRepository.findById(diplomeId)
            .orElseThrow(() -> new RuntimeException("Diplôme non trouvé"));

        // 2. Vérifier l'appartenance
        if (!existingDiplome.getEmploye().getId().equals(employeId)) {
            throw new RuntimeException("Ce diplôme ne appartient pas à l'employé");
        }

        // 3. Si nouveau diplôme différent
        if (!diplomeId.equals(newDiplomeId)) {
            Diplome newDiplome = diplomeRepository.findById(newDiplomeId)
                .orElseThrow(() -> new RuntimeException("Nouveau diplôme non trouvé"));

            // 4. Vérifier disponibilité nouveau diplôme
            if (newDiplome.getEmploye() != null) {
                throw new RuntimeException("Nouveau diplôme déjà assigné");
            }

            // 5. Transférer l'association SANS SUPPRIMER
            existingDiplome.setEmploye(null);
            newDiplome.setEmploye(existingDiplome.getEmploye());
            newDiplome.setDateObtention(dateObtention);
            diplomeRepository.save(existingDiplome); // Conserve l'ancien diplôme
            return diplomeRepository.save(newDiplome);
        }
        
        // 6. Si même diplôme, mettre à jour la date
        existingDiplome.setDateObtention(dateObtention);
        return diplomeRepository.save(existingDiplome);
    }
    @Override
    @Transactional
    public void deleteDiplome(Long diplomeId, Long employeId) {
        Diplome diplome = diplomeRepository.findById(diplomeId)
            .orElseThrow(() -> new RuntimeException("Diplôme non trouvé"));

        if (!diplome.getEmploye().getId().equals(employeId)) {
            throw new RuntimeException("Ce diplôme ne correspond pas à cet employé");
        }

        diplomeRepository.delete(diplome);
    }

	@Override
	@Transactional
    public Diplome saveDiplome(Long idType, String libelleTypeDiplome, String libelle) {
        Optional<TypeDiplome> typeDiplomeOpt = typeDiplomeRepository.findById(idType);

        TypeDiplome typeDiplome = typeDiplomeOpt.orElseGet(() -> {
            Optional<TypeDiplome> existingType = typeDiplomeRepository.findByLibelleTypeDiplome(libelleTypeDiplome);
            return existingType.orElseGet(() -> {
                TypeDiplome newType = new TypeDiplome();
                newType.setLibelleTypeDiplome(libelleTypeDiplome);
                return typeDiplomeRepository.save(newType);
            });
        });

        Diplome diplome = new Diplome();
        diplome.setLibelle(libelle);
        diplome.setTypeDiplome(typeDiplome);
        return diplomeRepository.save(diplome);
    }

	@Override
	public List<Diplome> getAllDiplomes() {
		
		return diplomeRepository.findAll();
	}

	@Override
	public Optional<Diplome> getDiplomeById(Long id) {
        return diplomeRepository.findById(id);
    }

	@Override
	@Transactional
    public Diplome updateDiplome(Long id, Long idType, String libelleTypeDiplome, String libelle) {
        Diplome diplome = diplomeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Diplôme non trouvé avec l'ID : " + id));

        TypeDiplome typeDiplome = typeDiplomeRepository.findById(idType)
            .orElseGet(() -> {
                Optional<TypeDiplome> existingType = typeDiplomeRepository.findByLibelleTypeDiplome(libelleTypeDiplome);
                return existingType.orElseGet(() -> {
                    TypeDiplome newType = new TypeDiplome();
                    newType.setLibelleTypeDiplome(libelleTypeDiplome);
                    return typeDiplomeRepository.save(newType);
                });
            });

        diplome.setLibelle(libelle);
        diplome.setTypeDiplome(typeDiplome);
        return diplomeRepository.save(diplome);
    }
	
	@Override
	@Transactional
	public void deleteDiplomePermanently(Long id) {
	    diplomeRepository.deleteById(id);
	}
	
	@Transactional
	public Diplome assignDiplomeToEmploye(Long employeId, Long diplomeId, LocalDate dateObtention) {
	    Employe employe = employeRepository.findById(employeId)
	        .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
	    
	    Diplome diplome = diplomeRepository.findById(diplomeId)
	        .orElseThrow(() -> new RuntimeException("Diplôme non trouvé"));
	    
	    // Vérifier si le diplôme est déjà assigné à un autre employé
	    
	    
	    diplome.setEmploye(employe);
	    diplome.setDateObtention(dateObtention);
	    return diplomeRepository.save(diplome);
	}

	@Transactional
	public Diplome updateDiplomeAssignment(Long oldDiplomeId, Long employeId, Long newDiplomeId, LocalDate dateObtention) {
	    // 1. Désassigner l'ancien diplôme
	    Diplome oldDiplome = diplomeRepository.findById(oldDiplomeId)
	        .orElseThrow(() -> new RuntimeException("Ancien diplôme non trouvé"));
	    
	    if (!oldDiplome.getEmploye().getId().equals(employeId)) {
	        throw new RuntimeException("L'ancien diplôme n'appartient pas à cet employé");
	    }
	    
	    oldDiplome.setEmploye(null);
	    diplomeRepository.save(oldDiplome);
	    
	    // 2. Assigner le nouveau diplôme
	    return this.assignDiplomeToEmploye(employeId, newDiplomeId, dateObtention);
	}


}
