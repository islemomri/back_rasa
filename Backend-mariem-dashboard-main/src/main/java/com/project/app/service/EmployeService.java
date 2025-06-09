package com.project.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.Employe;
import com.project.app.model.EmployeCompetence;
import com.project.app.model.EmployePoste;
import com.project.app.model.Experience;
import com.project.app.model.ExperienceAnterieure;
import com.project.app.model.ExperienceAssad;
import com.project.app.model.Poste;
import com.project.app.model.Site;
import com.project.app.dto.EmployeDTO;
import com.project.app.dto.EmployeHabilitationDto;
import com.project.app.dto.EmployePosteDetailDto;
import com.project.app.dto.EmployePosteRequest;
import com.project.app.dto.EmployeUpdateDTO;
import com.project.app.dto.PosteAvecDatesDTO;
import com.project.app.dto.PosteDtoHabilite;
import com.project.app.model.Competence;
import com.project.app.model.CompetencePoste;
import com.project.app.model.Direction;
import com.project.app.model.Discipline;
import com.project.app.repository.EmployeRepository;
import com.project.app.repository.PosteRepository;
import com.project.app.repository.SiteRepository;
import com.project.app.repository.employeFormationRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.project.app.repository.CompetenceRepository;
import com.project.app.repository.DirectionRepository;
import com.project.app.repository.DisciplineRepository;
import com.project.app.repository.EmployeCompetenceRepository;
import com.project.app.repository.EmployePosteRepository;

@Service
public class EmployeService implements IEmployeService {
    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private DisciplineRepository disciplineRepository;
    
    @Autowired
    private DirectionRepository directionRepository;
    
    @Autowired
    private EmployePosteRepository employeposterepository;
    
    @Autowired
    private PosteRepository posteRepository;
    
    @Autowired
    private employeFormationRepository formationemployeRepository;
    @Autowired
    private SiteRepository siteRepository;
    
    @Autowired
    private EmployeCompetenceRepository  employeCompetenceRepository;

    @Autowired
    private CompetenceRepository competenceRepository;

    public List<Employe> getAllEmployes() {
        return employeRepository.findEmployesWhereAjoutIsTrue();
    }

    public Employe addEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    public Optional<Employe> getEmployeById(Long id) {
        return employeRepository.findById(id);
    }

    public void deleteEmploye(Long id) {
        employeRepository.deleteById(id);
    }

    
    public List<Discipline> getDisciplinesByEmploye(Long employeId) {
        return disciplineRepository.findByEmployeId(employeId);
    }

    
    public Discipline addDisciplineToEmploye(Long employeId, Discipline discipline) {
        // Récupérer l'employé par son ID
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

        // Associer la discipline à l'employé
        discipline.setEmploye(employe);

        // Sauvegarder la discipline
        return disciplineRepository.save(discipline);
    }

    public void removeDisciplineFromEmploye(Long employeId, Long disciplineId) {
    	 Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline non trouvée"));
        
        employe.getDisciplines().remove(discipline); 
        discipline.setEmploye(null); 
        
        employeRepository.save(employe);
        disciplineRepository.delete(discipline); 
    }
    
    public Discipline updateDiscipline(Long disciplineId, Discipline updatedDiscipline) {
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline non trouvée"));
        
        
        discipline.setNom(updatedDiscipline.getNom());
        discipline.setDateDebut(updatedDiscipline.getDateDebut());
        discipline.setDateFin(updatedDiscipline.getDateFin());
        
        return disciplineRepository.save(discipline); // Sauvegarder les modifications
    }
    
  public List<Discipline> getDisciplinesByEmployeId(Long employeId) {
	  Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        return employe.getDisciplines(); // Retourner la liste des disciplines
    }
    @Transactional
	@Override
	public Employe addEmployeWithExperience(Employe employe) {
    	 
        if (employe.getExperiences() == null) {
            employe.setExperiences(new ArrayList<>());
        }

        // Associe chaque expérience à l'employé avant de sauvegarder
        for (Experience exp : employe.getExperiences()) {
            exp.setEmploye(employe);
        }

        // Sauvegarde de l'employé avec ses expériences
        return employeRepository.save(employe);
	}

    @Override
    @Transactional
    public Employe ajouterEmployeAvecPoste(Employe employe, Long posteId, LocalDate dateDebut, LocalDate dateFin) {
        // Récupérer le poste par son ID
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new RuntimeException("Poste non trouvé"));

        // Sauvegarder l'employé d'abord
        employe = employeRepository.save(employe);

        // Créer une nouvelle instance d'EmployePoste pour gérer l'association
        EmployePoste employePoste = new EmployePoste();
        employePoste.setEmploye(employe);
        employePoste.setPoste(poste);
        employePoste.setDateDebut(dateDebut);
        employePoste.setDateFin(dateFin);

        // Initialiser la liste employePostes si elle est null
        if (employe.getEmployePostes() == null) {
            employe.setEmployePostes(new ArrayList<>());
        }

        // Ajouter l'association à l'employé
        employe.getEmployePostes().add(employePoste);

        // Ajouter l'association à la collection des postes de l'employé
        poste.getEmployePostes().add(employePoste);

        // Sauvegarder l'entité EmployePoste pour persister l'association avec les dates
        employeposterepository.save(employePoste);

        return employe;
    }
	
	@Override
	public Employe ajouterEmploye(EmployeDTO employeDTO) {
        Employe employe = new Employe();
        employe.setNom(employeDTO.getNom());
        employe.setPrenom(employeDTO.getPrenom());
        employe.setMatricule(employeDTO.getMatricule());
        employe.setDateNaissance(employeDTO.getDateNaissance());
        employe.setDateRecrutement(employeDTO.getDateRecrutement());
        employe.setActif(true); // Par défaut, on met l'employé actif
employe.setAjout(false);
        return employeRepository.save(employe);
    }
	
	
	
	
	
	
	@Override
	public Employe updateEmploye(Long id, EmployeUpdateDTO employeUpdateDTO) {
	    /*Employe employe = employeRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

	    // Mettre à jour les champs
	    if (employeUpdateDTO.getNom() != null) employe.setNom(employeUpdateDTO.getNom());
	    if (employeUpdateDTO.getPrenom() != null) employe.setPrenom(employeUpdateDTO.getPrenom());
	    if (employeUpdateDTO.getMatricule() != null) employe.setMatricule(employeUpdateDTO.getMatricule());
	    if (employeUpdateDTO.getActif() != null) employe.setActif(employeUpdateDTO.getActif());  // Correction ici
	    if (employeUpdateDTO.getDateNaissance() != null) employe.setDateNaissance(employeUpdateDTO.getDateNaissance());
	    if (employeUpdateDTO.getDateRecrutement() != null) employe.setDateRecrutement(employeUpdateDTO.getDateRecrutement());
	    if (employeUpdateDTO.getSexe() != null) employe.setSexe(employeUpdateDTO.getSexe());
	    if (employeUpdateDTO.getEmail() != null) employe.setEmail(employeUpdateDTO.getEmail());
	    if (employeUpdateDTO.getSite() != null) employe.setSite(employeUpdateDTO.getSite());
	    if (employeUpdateDTO.getDirection() != null) employe.setDirection(employeUpdateDTO.getDirection());
	    if (employeUpdateDTO.getPhoto() != null) employe.setPhoto(employeUpdateDTO.getPhoto());*/

	    return null;
	}

	
	

	@Override
	    public List<Discipline> getAllDisciplines(Long employeId) {
	        Optional<Employe> employeOptional = employeRepository.findById(employeId);
	        if (employeOptional.isPresent()) {
	            return employeOptional.get().getDisciplines();  // Retourne la liste des disciplines
	        }
	        throw new RuntimeException("Employé non trouvé");
	    }

	@Override
	public List<ExperienceAssad> getAllExperiencesAssadByEmployeId(Long id) {
		Optional<Employe> employe = employeRepository.findById(id);
        if (employe.isPresent()) {
            return employe.get().getExperiences().stream()
                          .filter(exp -> exp instanceof ExperienceAssad)
                          .map(exp -> (ExperienceAssad) exp)
                          .toList();
        } else {
            throw new RuntimeException("Employé non trouvé avec l'ID : " + id);
        }
	}

	@Override
	public List<ExperienceAnterieure> getAllExperiencesAnterieuresByEmployeId(Long id) {
		 Optional<Employe> employe = employeRepository.findById(id);
	        if (employe.isPresent()) {
	            return employe.get().getExperiences().stream()
	                          .filter(exp -> exp instanceof ExperienceAnterieure)
	                          .map(exp -> (ExperienceAnterieure) exp)
	                          .toList();
	        } else {
	            throw new RuntimeException("Employé non trouvé avec l'ID : " + id);
	        }
	}

	@Transactional
	@Override
	public PosteAvecDatesDTO ajouterPosteAEmploye(Long employeId, Long posteId, Long directionId, Long siteId, LocalDate dateDebut, LocalDate dateFin) {
	    // Récupérer l'employé par son ID
	    Employe employe = employeRepository.findById(employeId)
	            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

	    // Récupérer le poste par son ID
	    Poste poste = posteRepository.findById(posteId)
	            .orElseThrow(() -> new RuntimeException("Poste non trouvé"));

	    // Récupérer la direction par son ID et vérifier si elle est associée au poste
	    Direction direction = poste.getDirections().stream()
	            .filter(d -> d.getId().equals(directionId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Direction non trouvée pour ce poste"));

	    // Récupérer le site par son ID et vérifier s'il est associé à la direction
	    Site site = direction.getSites().stream()
	            .filter(s -> s.getId().equals(siteId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Site non trouvé pour cette direction"));

	    // Créer une nouvelle instance d'EmployePoste pour gérer l'association
	    EmployePoste employePoste = new EmployePoste();
	    employePoste.setEmploye(employe);
	    employePoste.setPoste(poste);
	    employePoste.setDateDebut(dateDebut);

	    // Si dateFin est présente, l'assigner, sinon ne rien faire
	    if (dateFin != null) {
	        employePoste.setDateFin(dateFin);
	    }

	    employePoste.setNomDirection(direction.getNom_direction()); // Assigner le nom de la direction
	    employePoste.setNomSite(site.getNom_site()); // Assigner le nom du site

	    // Initialiser la liste employePostes si elle est null
	    if (employe.getEmployePostes() == null) {
	        employe.setEmployePostes(new ArrayList<>());
	    }

	    // Ajouter l'association à l'employé
	    employe.getEmployePostes().add(employePoste);

	    // Initialiser la liste employePostes du poste si elle est null
	    if (poste.getEmployePostes() == null) {
	        poste.setEmployePostes(new ArrayList<>());
	    }

	    // Ajouter l'association à la collection des postes de l'employé
	    poste.getEmployePostes().add(employePoste);

	    // Sauvegarder l'entité EmployePoste pour persister l'association avec les dates
	    employeposterepository.save(employePoste);

	    // Sauvegarder l'employé
	    employeRepository.save(employe);

	    // Retourner le DTO avec la direction et le site
	    return new PosteAvecDatesDTO(poste.getId(), poste.getTitre(), dateDebut, dateFin, direction.getNom_direction(), site.getNom_site());
	}


	@Override
	@Transactional
	public List<PosteAvecDatesDTO> getPostesByEmploye(Long employeId) {
	    try {
	        // Récupérer l'employé avec ses EmployePostes et les Postes associés
	        Employe employe = employeRepository.findById(employeId)
	                .orElseThrow(() -> {
	                    System.out.println("Employé non trouvé avec l'ID : " + employeId);
	                    return new RuntimeException("Employé non trouvé");
	                });

	        // Charger les EmployePostes avec les Postes associés
	        List<EmployePoste> employePostes = employeposterepository.findByEmployeIdWithPoste(employeId);

	        if (employePostes == null || employePostes.isEmpty()) {
	            System.out.println("Aucun poste trouvé pour l'employé avec l'ID : " + employeId);
	            return new ArrayList<>();
	        }

	        // Convertir les EmployePoste en PosteAvecDatesDTO
	        return employePostes.stream()
	                .map(employePoste -> {
	                    Poste poste = employePoste.getPoste();
	                    if (poste == null) {
	                        System.out.println("Poste non trouvé pour EmployePoste avec l'ID : " + employePoste.getId());
	                        return null;
	                    }

	                    String nomDirection = (employePoste.getNomDirection() != null) ? employePoste.getNomDirection() : "Non spécifié";
	                    String nomSite = (employePoste.getNomSite() != null) ? employePoste.getNomSite() : "Non spécifié";

	                    return new PosteAvecDatesDTO(
	                            poste.getId(),
	                            poste.getTitre(),
	                            employePoste.getDateDebut(),
	                            employePoste.getDateFin(),
	                            nomDirection,
	                            nomSite
	                    );
	                })
	                .filter(Objects::nonNull)
	                .collect(Collectors.toList());
	    } catch (Exception e) {
	        System.err.println("Erreur lors de la récupération des postes : " + e.getMessage());
	        throw e;
	    }
	}
	@Override
	public PosteAvecDatesDTO modifierPosteAEmploye(Long employeId, Long posteId, Long directionId, Long siteId, LocalDate dateDebut, LocalDate dateFin) {
	    // Récupérer l'employé par son ID
	    Employe employe = employeRepository.findById(employeId)
	            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

	    // Récupérer le poste par son ID
	    Poste poste = posteRepository.findById(posteId)
	            .orElseThrow(() -> new RuntimeException("Poste non trouvé"));

	    // Récupérer l'association de l'employé avec son poste
	    EmployePoste employePoste = employe.getEmployePostes().stream()
	            .filter(ep -> ep.getPoste().getId().equals(posteId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Association entre l'employé et le poste non trouvée"));

	    // Récupérer la direction par son ID et vérifier si elle est associée au poste
	    Direction direction = poste.getDirections().stream()
	            .filter(d -> d.getId().equals(directionId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Direction non trouvée pour ce poste"));

	    // Récupérer le site par son ID et vérifier s'il est associé à la direction
	    Site site = direction.getSites().stream()
	            .filter(s -> s.getId().equals(siteId))
	            .findFirst()
	            .orElseThrow(() -> new RuntimeException("Site non trouvé pour cette direction"));

	    // Mettre à jour les dates et informations de direction et de site dans l'association
	    employePoste.setDateDebut(dateDebut);

	    // Si dateFin est nul, on la laisse telle quelle, sinon on la met à jour
	    if (dateFin != null) {
	        employePoste.setDateFin(dateFin);
	    } else {
	        employePoste.setDateFin(null); // Si dateFin est null, on la définit explicitement comme null
	    }

	    employePoste.setNomDirection(direction.getNom_direction()); // Mettre à jour le nom de la direction
	    employePoste.setNomSite(site.getNom_site()); // Mettre à jour le nom du site

	    // Sauvegarder l'association mise à jour
	    employeposterepository.save(employePoste);

	    // Sauvegarder l'employé pour que les modifications soient persistées
	    employeRepository.save(employe);

	    // Retourner le DTO mis à jour avec les nouvelles informations
	    return new PosteAvecDatesDTO(poste.getId(), poste.getTitre(), dateDebut, dateFin, direction.getNom_direction(), site.getNom_site());
	}

	@Override
	@Transactional
	 public Optional<EmployePoste> getPosteDetailsByEmployeIdAndPosteId(Long employeId, Long posteId) {
        return employeposterepository.findPosteDetailsByEmployeIdAndPosteId(employeId, posteId);
    }
	
	@Override
	@Transactional
	public EmployePoste getPosteDetailsById(Long employePosteId) {
        return employeposterepository.findByIdWithDetails(employePosteId)
            .orElseThrow(() -> new RuntimeException("Association employé-poste non trouvée"));
    }

	@Override
	public void supprimerPostePourEmploye(Long employeId, Long posteId) {
		employeposterepository.deleteByEmployeIdAndPosteId(employeId, posteId);
		
	}

	@Override
	@Transactional
    public Employe ajouterEmployeAvecPoste(Long posteId, Long directionId, Long siteId, Employe employe, LocalDate dateDebut, LocalDate dateFin) {
        // Récupérer le poste, la direction et le site
        Poste poste = posteRepository.findById(posteId).orElseThrow(() -> new RuntimeException("Poste introuvable"));
        Direction direction = directionRepository.findById(directionId).orElseThrow(() -> new RuntimeException("Direction introuvable"));
        Site site = siteRepository.findById(siteId).orElseThrow(() -> new RuntimeException("Site introuvable"));

        // Créer l'employé et le sauvegarder
        employeRepository.save(employe);

        // Créer l'association EmployePoste
        EmployePoste employePoste = new EmployePoste();
        employePoste.setEmploye(employe);
        employePoste.setPoste(poste);
        employePoste.setNomDirection(direction.getNom_direction());
        employePoste.setNomSite(site.getNom_site());
        employePoste.setDateDebut(dateDebut);
        employePoste.setDateFin(dateFin);

        // Ajouter l'employé au poste
        poste.getEmployePostes().add(employePoste);
        posteRepository.save(poste);

        return employe;
    }

	@Override
	@Transactional
	public Employe modifierEmploye(Long id, Employe employeModifie, Long posteId, Long directionId, Long siteId,
			LocalDate dateDebut, LocalDate dateFin) {
		 // Vérifier si l'employé existe
	    Employe employe = employeRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

	    // Mettre à jour les champs
	    employe.setNom(employeModifie.getNom());
	    employe.setPrenom(employeModifie.getPrenom());
	    employe.setMatricule(employeModifie.getMatricule());
	    employe.setActif(employeModifie.isActif());
	    employe.setDateNaissance(employeModifie.getDateNaissance());
	    employe.setDateRecrutement(employeModifie.getDateRecrutement());
	    employe.setSexe(employeModifie.getSexe());
	    employe.setEmail(employeModifie.getEmail());
	    employe.setAjout(true);

	    // Vérifier et mettre à jour le poste, direction et site
	    if (posteId != null && directionId != null && siteId != null) {
	        Poste poste = posteRepository.findById(posteId)
	                .orElseThrow(() -> new RuntimeException("Poste introuvable"));
	        Direction direction = directionRepository.findById(directionId)
	                .orElseThrow(() -> new RuntimeException("Direction introuvable"));
	        Site site = siteRepository.findById(siteId)
	                .orElseThrow(() -> new RuntimeException("Site introuvable"));

	        // Vérifier si l'employé a déjà un poste
	        Optional<EmployePoste> employePosteOpt = employe.getEmployePostes().stream().findFirst();

	        if (employePosteOpt.isPresent()) {
	            // Modifier le poste existant
	            EmployePoste employePoste = employePosteOpt.get();
	            employePoste.setPoste(poste);
	            employePoste.setNomDirection(direction.getNom_direction());
	            employePoste.setNomSite(site.getNom_site());
	            employePoste.setDateDebut(dateDebut);
	            employePoste.setDateFin(dateFin);
	            employeposterepository.save(employePoste);
	        } else {
	            // Créer un nouveau lien employé-poste
	            EmployePoste employePoste = new EmployePoste();
	            employePoste.setEmploye(employe);
	            employePoste.setPoste(poste);
	            employePoste.setNomDirection(direction.getNom_direction());
	            employePoste.setNomSite(site.getNom_site());
	            employePoste.setDateDebut(dateDebut);
	            employePoste.setDateFin(dateFin);
	            employeposterepository.save(employePoste);
	        }
	    }

	    return employeRepository.save(employe);
	}

	@Override
	public List<Employe> getEmployesWithoutPoste() {
		 return employeRepository.findEmployesWhereAjoutIsFalse();
	}

	@Override
	@Transactional
	public byte[] getDocumentByEmployeIdAndFormationId(Long employeId, Long formationId) {
	    return formationemployeRepository.findDocumentByEmployeIdAndFormationId(employeId, formationId);
	}

	@Override
	@Transactional
	public PosteAvecDatesDTO changerPosteEmploye(Long employeId, Long nouveauPosteId, Long directionId, Long siteId) {

	    Employe employe = employeRepository.findById(employeId)
	            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

	    // Clôture du poste actuel
	    employe.getEmployePostes().stream()
	            .filter(ep -> ep.getDateFin() == null)
	            .findFirst()
	            .ifPresent(posteActuel -> {
	                posteActuel.setDateFin(LocalDate.now());
	                employeposterepository.save(posteActuel);
	            });

	    // Affectation du nouveau poste
	    PosteAvecDatesDTO posteAvecDatesDTO = ajouterPosteAEmploye(
	            employeId, 
	            nouveauPosteId, 
	            directionId, 
	            siteId, 
	            LocalDate.now(), 
	            null
	    );

	    // ⚠️ Ajouter les compétences associées au poste
	    Poste nouveauPoste = posteRepository.findById(nouveauPosteId)
	            .orElseThrow(() -> new RuntimeException("Poste non trouvé"));

	    Set<CompetencePoste> competencesPoste = nouveauPoste.getCompetencePostes();

	    for (CompetencePoste cp : competencesPoste) {
	        String nomCompetence = cp.getNom();

	        // Vérifie si la compétence existe déjà dans Competence (via nom)
	        Competence competence = competenceRepository.findByNom(nomCompetence)
	                .orElseGet(() -> {
	                    Competence nouvelleCompetence = new Competence();
	                    nouvelleCompetence.setNom(nomCompetence);
	                    return competenceRepository.save(nouvelleCompetence);
	                });

	        // Vérifie si l'employé a déjà cette compétence
	        boolean exists = employeCompetenceRepository.existsByEmployeIdAndCompetenceId(employeId, competence.getId());

	        if (!exists) {
	            // Création de la relation Employe <-> Compétence avec niveau (exemple "Débutant")
	            EmployeCompetence ec = new EmployeCompetence();
	            ec.setEmploye(employe);
	            ec.setCompetence(competence);
	            ec.setNiveau("Débutant"); // à personnaliser selon besoin
	            employeCompetenceRepository.save(ec);
	        }
	    }

	    return posteAvecDatesDTO;
	}

	
	@Override
	public void supprimerPostePourEmployeParId(Long employePosteId) {
	    employeposterepository.deleteById(employePosteId);
	}
	
	@Transactional
	public PosteAvecDatesDTO modifierPosteComplet(
	        Long employePosteId, 
	        Long nouveauPosteId,
	        Long directionId, 
	        Long siteId,
	        LocalDate dateDebut, 
	        LocalDate dateFin) {
	    
	    EmployePoste employePoste = employeposterepository.findById(employePosteId)
	            .orElseThrow(() -> new RuntimeException("Association employé-poste non trouvée"));

	    // Mise à jour du poste si fourni
	    if (nouveauPosteId != null) {
	        Poste nouveauPoste = posteRepository.findById(nouveauPosteId)
	                .orElseThrow(() -> new RuntimeException("Nouveau poste non trouvé"));
	        employePoste.setPoste(nouveauPoste);
	    }

	    // Mise à jour des autres champs
	    employePoste.setDateDebut(dateDebut);
	    employePoste.setDateFin(dateFin);
	    
	    Direction direction = directionRepository.findById(directionId)
	            .orElseThrow(() -> new RuntimeException("Direction non trouvée"));
	    Site site = siteRepository.findById(siteId)
	            .orElseThrow(() -> new RuntimeException("Site non trouvé"));
	    
	    employePoste.setNomDirection(direction.getNom_direction());
	    employePoste.setNomSite(site.getNom_site());

	    employeposterepository.saveAndFlush(employePoste); // Sauvegarde explicite

	    return new PosteAvecDatesDTO(
	            employePoste.getId(),
	            employePoste.getPoste().getId(),
	            employePoste.getPoste().getTitre(),
	            employePoste.getDateDebut(),
	            employePoste.getDateFin(),
	            employePoste.getNomDirection(),
	            employePoste.getNomSite()
	    );
	}

	@Override
	public PosteAvecDatesDTO modifierPosteAEmployeParId(Long employePosteId, Long directionId, Long siteId,
			LocalDate dateDebut, LocalDate dateFin) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	@Transactional
	public List<EmployeHabilitationDto> getEmployesAvecPostesHabilitesProches() {
	    List<Employe> employes = employeRepository.findEmployesWhereAjoutIsTrue();
	    List<Poste> tousLesPostes = posteRepository.findAll();

	    return employes.stream().map(employe -> {
	        List<String> competencesEmploye = employe.getEmployeCompetences().stream()
	            .map(ec -> ec.getCompetence().getNom())
	            .collect(Collectors.toList());

	        List<PosteDtoHabilite> postesHabilitesProches = tousLesPostes.stream()
	            .filter(poste -> {
	                List<String> competencesPoste = poste.getCompetencePostes().stream()
	                    .map(CompetencePoste::getNom)
	                    .collect(Collectors.toList());

	                long matchingCompetencesCount = competencesEmploye.stream()
	                    .filter(competencesPoste::contains)
	                    .count();

	                double matchingRatio = (double) matchingCompetencesCount / competencesPoste.size();
	                return matchingRatio >= 0.5;
	            })
	            .map(poste -> {
	                List<String> competencesPoste = poste.getCompetencePostes().stream()
	                    .map(CompetencePoste::getNom)
	                    .collect(Collectors.toList());

	                List<String> competencesManquantes = competencesPoste.stream()
	                    .filter(c -> !competencesEmploye.contains(c))
	                    .collect(Collectors.toList());

	                List<String> competencesSupplementaires = competencesEmploye.stream()
	                    .filter(c -> !competencesPoste.contains(c))
	                    .collect(Collectors.toList());

	                return new PosteDtoHabilite(
	                    poste.getId(),
	                    poste.getNiveauExperience(),
	                    poste.getDiplomeRequis(),
	                    poste.getTitre(),
	                    competencesPoste,
	                    competencesManquantes,
	                    competencesSupplementaires
	                );
	            })
	            .collect(Collectors.toList());

	        return new EmployeHabilitationDto(
	        		  employe.getId(), 
	            employe.getNom(),
	            employe.getPrenom(),
	            employe.getMatricule(),
	            employe.getEmail(),
	            competencesEmploye,
	            postesHabilitesProches
	        );
	    }).collect(Collectors.toList());
	}
	@Override
	public String getNomDirectionPosteActuel(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

        return employe.getEmployePostes().stream()
            .filter(ep -> ep.getDateFin() == null)
            .map(EmployePoste::getNomDirection)
            .findFirst()
            .orElse(null); // ou lancer une exception si aucun poste actif
    }

	@Override
	public EmployePosteDetailDto getEmployeAvecPosteParMatriculeEtTitre(String titrePoste, Integer matricule) {
		return getEmployesAvecPostesHabilitesProches().stream()
		        .filter(e -> e.getMatricule().equals(matricule))
		        .findFirst()
		        .flatMap(employe -> {
		            return employe.getPostesHabilites().stream()
		                .filter(poste -> poste.getTitre().equalsIgnoreCase(titrePoste))
		                .findFirst()
		                .map(poste -> new EmployePosteDetailDto(
		                    employe.getId(),
		                    employe.getNom(),
		                    employe.getPrenom(),
		                    employe.getMatricule(),
		                    employe.getEmail(),
		                    employe.getCompetences(),
		                    poste
		                ));
		        }).orElse(null);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}