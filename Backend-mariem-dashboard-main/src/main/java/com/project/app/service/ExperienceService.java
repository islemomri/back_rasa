package com.project.app.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.Employe;
import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;
import com.project.app.repository.EmployeRepository;
import com.project.app.repository.ExperienceRepository;

import jakarta.transaction.Transactional;

@Service
public class ExperienceService implements IExperience {
	@Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private EmployeRepository employeRepository;
    
    @Override
    @Transactional
    public ExperienceAnterieure addExperienceAnterieure(Long employeId, ExperienceAnterieure experience) {
        Optional<Employe> employeOpt = employeRepository.findById(employeId);
        if (employeOpt.isPresent()) {
            experience.setEmploye(employeOpt.get());
            return experienceRepository.save(experience);
        } else {
            throw new RuntimeException("Employé non trouvé !");
        }
    }

	@Override
	@Transactional
	public ExperienceAssad addExperienceAssad(Long employeId, ExperienceAssad experience) {
		 Optional<Employe> employeOpt = employeRepository.findById(employeId);
	        if (employeOpt.isPresent()) {
	            experience.setEmploye(employeOpt.get());
	            return experienceRepository.save(experience);
	        } else {
	            throw new RuntimeException("Employé non trouvé !");
	        }
	}

	@Override
	@Transactional
	public ExperienceAssad modifierExperienceAssad(Long id, ExperienceAssad nouvelleExperience) {
		  Optional<Experience> experienceExistante = experienceRepository.findById(id);

	        if (experienceExistante.isPresent() && experienceExistante.get() instanceof ExperienceAssad) {
	            ExperienceAssad expExistante = (ExperienceAssad) experienceExistante.get();
	            
	            // Mise à jour des champs communs
	            expExistante.setDateDebut(nouvelleExperience.getDateDebut());
	            expExistante.setDateFin(nouvelleExperience.getDateFin());
	            expExistante.setPoste(nouvelleExperience.getPoste());

	            // Mise à jour des champs spécifiques
	            expExistante.setDirection(nouvelleExperience.getDirection());
	            expExistante.setModeAffectation(nouvelleExperience.getModeAffectation());

	            return experienceRepository.save(expExistante);
	        } else {
	            throw new RuntimeException("Expérience Assad non trouvée avec ID : " + id);
	        }
	}

	@Override
	@Transactional
	public ExperienceAnterieure modifierExperienceAnterieure(Long id, ExperienceAnterieure nouvelleExperience) {
		 Optional<Experience> experienceExistante = experienceRepository.findById(id);

	        if (experienceExistante.isPresent() && experienceExistante.get() instanceof ExperienceAnterieure) {
	            ExperienceAnterieure expExistante = (ExperienceAnterieure) experienceExistante.get();
	            
	            // Mise à jour des champs communs
	            expExistante.setDateDebut(nouvelleExperience.getDateDebut());
	            expExistante.setDateFin(nouvelleExperience.getDateFin());
	            expExistante.setPoste(nouvelleExperience.getPoste());

	            // Mise à jour des champs spécifiques
	            expExistante.setSociete(nouvelleExperience.getSociete());

	            return experienceRepository.save(expExistante);
	        } else {
	            throw new RuntimeException("Expérience Anterieure non trouvée avec ID : " + id);
	        }
	    }

	@Override
	public List<ExperienceAssad> getAllExperienceAssadByEmployeId(Long employeId) {
		 // Récupérer toutes les expériences
        List<Experience> experiences = experienceRepository.findByEmployeId(employeId);
        
        // Filtrer celles qui sont de type ExperienceAssad
        return experiences.stream()
                .filter(exp -> exp instanceof ExperienceAssad)
                .map(exp -> (ExperienceAssad) exp)
                .collect(Collectors.toList());
	}

	@Override
	public List<ExperienceAnterieure> getAllExperienceAnterieureByEmployeId(Long employeId) {
		// Récupérer toutes les expériences
        List<Experience> experiences = experienceRepository.findByEmployeId(employeId);
        
        // Filtrer celles qui sont de type ExperienceAnterieure
        return experiences.stream()
                .filter(exp -> exp instanceof ExperienceAnterieure)
                .map(exp -> (ExperienceAnterieure) exp)
                .collect(Collectors.toList());
    }

	@Override
	@Transactional
	public void deleteExperienceAssad(Long employeId, Long experienceId) {
	    Optional<Employe> employeOpt = employeRepository.findById(employeId);
	    
	    if (employeOpt.isPresent()) {
	        // Chercher l'expérience par ID
	        Optional<Experience> experienceOpt = experienceRepository.findById(experienceId);
	        
	        if (experienceOpt.isPresent()) {
	            Experience experience = experienceOpt.get();
	            
	            // Vérifier si l'expérience est de type ExperienceAssad
	            if (experience instanceof ExperienceAssad) {
	                ExperienceAssad experienceAssad = (ExperienceAssad) experience;

	                // Vérifier que l'expérience Assad appartient bien à l'employé
	                if (experienceAssad.getEmploye().getId().equals(employeId)) {
	                    experienceRepository.delete(experienceAssad);
	                } else {
	                    throw new RuntimeException("L'expérience Assad n'appartient pas à cet employé !");
	                }
	            } else {
	                throw new RuntimeException("L'expérience avec ID " + experienceId + " n'est pas de type ExperienceAssad");
	            }
	        } else {
	            throw new RuntimeException("Expérience Assad non trouvée avec ID : " + experienceId);
	        }
	    } else {
	        throw new RuntimeException("Employé non trouvé avec ID : " + employeId);
	    }
	}

	    // Supprimer une expérience Antérieure
	@Override
	@Transactional
	public void deleteExperienceAnterieure(Long employeId, Long experienceId) {
	    Optional<Employe> employeOpt = employeRepository.findById(employeId);
	    
	    if (employeOpt.isPresent()) {
	        // Chercher l'expérience par ID (de type Experience)
	        Optional<Experience> experienceOpt = experienceRepository.findById(experienceId);
	        
	        if (experienceOpt.isPresent()) {
	            Experience experience = experienceOpt.get();

	            // Vérifier que l'expérience est de type ExperienceAnterieure
	            if (experience instanceof ExperienceAnterieure) {
	                ExperienceAnterieure experienceAnterieure = (ExperienceAnterieure) experience;

	                // Vérifier que l'expérience Antérieure appartient bien à l'employé
	                if (experienceAnterieure.getEmploye().getId().equals(employeId)) {
	                    experienceRepository.delete(experienceAnterieure);
	                } else {
	                    throw new RuntimeException("L'expérience Antérieure n'appartient pas à cet employé !");
	                }
	            } else {
	                throw new RuntimeException("L'expérience avec ID " + experienceId + " n'est pas de type ExperienceAnterieure");
	            }
	        } else {
	            throw new RuntimeException("Expérience Antérieure non trouvée avec ID : " + experienceId);
	        }
	    } else {
	        throw new RuntimeException("Employé non trouvé avec ID : " + employeId);
	    }
	}
}
	

	



