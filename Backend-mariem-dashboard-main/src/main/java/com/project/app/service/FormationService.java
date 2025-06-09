package com.project.app.service;
import java.util.Optional;

import java.time.LocalDate;

import java.util.Comparator;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.app.dto.EmployeResultatDto;
import com.project.app.dto.FormationDto;
import com.project.app.dto.FormationDto_Resultat;
import com.project.app.model.Employe;
import com.project.app.model.Entete;
import com.project.app.model.Formation;
import com.project.app.model.PeriodeFormation;
import com.project.app.model.RH;
import com.project.app.model.Responsable;
import com.project.app.model.ResultatFormation;

import com.project.app.model.Utilisateur;
import com.project.app.repository.EmployeRepository;
import com.project.app.repository.EnteteRepository;
import com.project.app.repository.FormationRepository;
import com.project.app.repository.RHRepository;
import com.project.app.repository.UtilisateurRepository;
import com.project.app.repository.employeFormationRepository;


import java.io.IOException;
import com.project.app.model.formation_employe;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class FormationService implements IFormationService {

	private final FormationRepository formationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final EmployeRepository employeRepository; // Ajoutez ce repository

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailservice;
    
    @Autowired
    private employeFormationRepository employeFormationRepository;
    @Autowired
    private RHRepository rhrepository ;
    @Autowired
    private EnteteRepository enteteRepository;

    @Autowired
    public FormationService(FormationRepository formationRepository, UtilisateurRepository utilisateurRepository, EmployeRepository employeRepository) {
        this.formationRepository = formationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.employeRepository = employeRepository;
    }
    @Autowired
    private employeFormationRepository formationEmployeRepository;

    @Override
    @Transactional
    public Formation creerFormation(FormationDto formationDto, Long rhId) {
        RH organisateur = (RH) utilisateurRepository.findById(rhId)
                .orElseThrow(() -> new UsernameNotFoundException("RH non trouvé"));

        Formation formation = new Formation();
        formation.setTitre(formationDto.getTitre());
        formation.setDescription(formationDto.getDescription());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setSousTypeFormation(formationDto.getSousTypeFormation());
        formation.setDateDebutPrevue(formationDto.getDateDebutPrevue());
        formation.setDateFinPrevue(formationDto.getDateFinPrevue());
        formation.setOrganisateur(organisateur);

        if (formationDto.getResponsableEvaluationId() != null) {
            Utilisateur responsable = utilisateurRepository.findById(formationDto.getResponsableEvaluationId())
                    .orElseThrow(() -> new UsernameNotFoundException("Responsable introuvable"));
            formation.setResponsableEvaluation(responsable);
            notificationService.creerNotification(responsable, 
                    "Vous avez été assigné comme responsable d'évaluation pour la formation : " + formation.getTitre());
        } else {
            formation.setResponsableEvaluationExterne(formationDto.getResponsableEvaluationExterne());
        }

        if (formationDto.getEmployeIds() != null && !formationDto.getEmployeIds().isEmpty()) {
            List<Employe> employes = employeRepository.findAllById(formationDto.getEmployeIds());
            formation.setEmployes(employes);
        }

        return formationRepository.save(formation);
    }
 

    @Override
    public List<Formation> getFormationsParRH(Long rhId) {
        return formationRepository.findByOrganisateurId(rhId);
    }
    
    public List<Formation> getFormationsParResponsable(Long responsableId) {
        return formationRepository.findFormationsFiltrees(responsableId);
    }
    
    public List<Formation> getFormationsParEmploye(Long employeId) {
        return formationRepository.findByEmployesId(employeId);
    }

    @Scheduled(cron = "0 * * * * ?") // Exécuté chaque minute
    public void verifierFormationsTerminees() {
        System.out.println("🔹 Tâche planifiée exécutée ! Vérification des formations terminées...");

        LocalDate aujourdHui = LocalDate.now();
        List<Formation> formations = formationRepository.findAllFormations(); // Récupère toutes les formations

        // Filtrer les formations dont la date de fin prévue est avant aujourd'hui et qui n'ont pas encore envoyé d'email
        List<Formation> formationsTerminees = formations.stream()
            .filter(f -> f.getDateFinPrevue().isBefore(aujourdHui) && !f.isEmailEnvoye()) // Filtre les formations non traitées
            .sorted(Comparator.comparing(Formation::getDateFinPrevue))  // Trie les formations par date de fin
            .collect(Collectors.toList());

        System.out.println("🔹 Nombre de formations trouvées : " + formationsTerminees.size());

        for (Formation formation : formationsTerminees) {
            // Vérifie s'il y a un responsable d'évaluation
            if (formation.getResponsableEvaluation() != null) {
                String destinataire = formation.getResponsableEvaluation().getEmail();
                System.out.println("🔹 Envoi d'email à (responsable évaluation) : " + destinataire);

                try {
                    emailservice.sendEmail(
                        destinataire, 
                        "Fin de la formation : " + formation.getTitre(),
                        "Bonjour " + formation.getResponsableEvaluation().getNom() + ",<br><br>"
                        + "La formation <b>\"" + formation.getTitre() + "\"</b> est terminée.<br>"
                        + "Veuillez évaluer tous les employés de cette formation afin de la valider avec succès.<br><br>"
                        + "Cordialement,<br>Votre système RH."
                    );
                    System.out.println("✅ Email envoyé avec succès !");
                    
                    // Mettre à jour le champ emailEnvoye à true
                    formation.setEmailEnvoye(true);
                    formationRepository.save(formation);
                } catch (MessagingException e) {
                    System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
                }
            } else {
                System.out.println("⚠️ Aucun responsable d'évaluation pour la formation : " + formation.getTitre());
            }
        }

    }

   
    @Scheduled(cron = "0 * * * * ?") // Tous les jours à minuit
    @Transactional
    public void envoyerRappelResponsableEvaluation() {
        System.out.println("📌 Tâche de rappel en cours...");

        LocalDate aujourdHui = LocalDate.now();
        List<Formation> formations = formationRepository.findAllFormationsWhereRappelNotSentOrNull();

        for (Formation formation : formations) {
            LocalDate dateRappel = formation.getDateRappel() != null 
                                    ? formation.getDateRappel() 
                                    : formation.getDateFinPrevue().minusDays(2);

            if (aujourdHui.equals(dateRappel) && !formation.isRappelEnvoye()) {
                Utilisateur responsable = formation.getResponsableEvaluation();
                if (responsable != null) {
                    try {
                        emailservice.sendEmail(
                            responsable.getEmail(),
                            "📢 Rappel : Formation \"" + formation.getTitre() + "\" arrive à sa fin",
                            "Bonjour " + responsable.getNom() + ",<br><br>"
                            + "La formation <b>\"" + formation.getTitre() + "\"</b> touche à sa fin le "
                            + formation.getDateFinPrevue().toString() + ".<br>"
                            + "Veuillez évaluer tous les employés de cette formation pour la valider avec succès.<br><br>"
                            + "Cordialement,<br>Votre système RH."
                        );
                        formation.setRappelEnvoye(true);
                        formationRepository.save(formation);
                        System.out.println("📨 Rappel envoyé à " + responsable.getEmail());
                    } catch (MessagingException e) {
                        System.err.println("❌ Erreur d'envoi de rappel : " + e.getMessage());
                    }
                }
            }
        }
    }
    
    
    @Scheduled(cron = "0 * * * * ?") // Exécuté tous les jours à minuit
    @Transactional
    public void envoyerRappelObligationValidation() {
        System.out.println("🔔 Tâche de rappel d'obligation de validation en cours...");

        LocalDate aujourdHui = LocalDate.now();
        // Récupère les formations non validées avec date fin dépassée de 2 jours
        List<Formation> formations = formationRepository.findByValideFalseAndDateFinPrevueBefore(aujourdHui.minusDays(2));

        for (Formation formation : formations) {
            // Vérifier si le rappel d'obligation n'a pas déjà été envoyé
            if (!formation.isRappelObligationEnvoye() && formation.getResponsableEvaluation() != null) {
                try {
                    emailservice.sendEmail(
                        formation.getResponsableEvaluation().getEmail(),
                        "⚠️ Action Requise: Validation obligatoire de la formation \"" + formation.getTitre() + "\"",
                        "Bonjour " + formation.getResponsableEvaluation().getNom() + ",<br><br>"
                        + "La formation <b>\"" + formation.getTitre() + "\"</b> n'a toujours pas été validée alors que "
                        + "la date de fin (" + formation.getDateFinPrevue().toString() + ") est dépassée depuis plus de 2 jours.<br><br>"
                        + "<b style='color:red'>ACTION REQUISE:</b> Vous devez impérativement valider cette formation dans les plus brefs délais.<br><br>"
                        + "Cordialement,<br>Votre système RH."
                    );
                    
                    // Marquer le rappel comme envoyé
                    formation.setRappelObligationEnvoye(true);
                    formationRepository.save(formation);
                    
                    System.out.println("📩 Email d'obligation envoyé à " + formation.getResponsableEvaluation().getEmail());
                } catch (MessagingException e) {
                    System.err.println("❌ Erreur d'envoi du rappel d'obligation: " + e.getMessage());
                }
            }
        }
    }
    
    @Scheduled(cron = "0 * * * * ?") // Exécuté tous les jours à minuit
    @Transactional
    public void envoyerConfirmationOrganisateur() {
        System.out.println("📬 Tâche d'envoi de confirmation aux organisateurs en cours...");

        // Récupère les formations validées mais où la confirmation n'a pas encore été envoyée
        List<Formation> formations = formationRepository.findByValideTrueAndConfirmationOrganisateurEnvoyeeFalse();

        for (Formation formation : formations) {
            if (formation.getOrganisateur() != null) {
                try {
                    // Envoi de l'email de confirmation
                    emailservice.sendEmail(
                        formation.getOrganisateur().getEmail(),
                        "✅ Confirmation de validation : Formation \"" + formation.getTitre() + "\"",
                        "Bonjour " + formation.getOrganisateur().getNom() + ",<br><br>"
                        + "Nous vous informons que la formation <b>\"" + formation.getTitre() + "\"</b> "
                        + "a été validée avec succès.<br><br>"
                        + "Détails de la formation:<br>"
                        + "- Type: " + formation.getTypeFormation() + "<br>"
                        + "- Date de début: " + formation.getDateDebutPrevue() + "<br>"
                        + "- Date de fin: " + formation.getDateFinPrevue() + "<br><br>"
                        + "Merci pour votre organisation.<br><br>"
                        + "Cordialement,<br>Votre système RH."
                    );

                    // Marquer la confirmation comme envoyée
                    formation.setConfirmationOrganisateurEnvoyee(true);
                    formationRepository.save(formation);
                    
                    System.out.println("📧 Confirmation envoyée à l'organisateur: " + formation.getOrganisateur().getEmail());
                } catch (MessagingException e) {
                    System.err.println("❌ Erreur d'envoi de confirmation à l'organisateur: " + e.getMessage());
                }
            }
        }
    }    
    
    
    
    public Long ajouterFormation(FormationDto formationDto) throws IOException {
        Formation formation = new Formation();
        formation.setTitre(formationDto.getTitre());
        formation.setDescription(formationDto.getDescription());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setSousTypeFormation(formationDto.getSousTypeFormation());
        formation.setDateDebutPrevue(formationDto.getDateDebutPrevue());
        formation.setDateFinPrevue(formationDto.getDateFinPrevue());
        formation.setTitrePoste(formationDto.getTitrePoste()); 
     

        if (formationDto.getPeriodes() != null && !formationDto.getPeriodes().isEmpty()) {
            for (FormationDto.PeriodeDto periodeDto : formationDto.getPeriodes()) {
                PeriodeFormation periode = new PeriodeFormation();
                periode.setDateDebut(periodeDto.getDateDebut());
                periode.setDateFin(periodeDto.getDateFin());
                periode.setFormateur(periodeDto.getFormateur());
                periode.setProgramme(periodeDto.getProgramme()); 
                periode.setFormation(formation); // Ceci est crucial
                formation.getPeriodes().add(periode);
            }
        }
        if (formationDto.getEnteteId() != null) {
            Entete entete = enteteRepository.findById(formationDto.getEnteteId())
                .orElseThrow(() -> new IllegalArgumentException("Entête introuvable avec ID: " + formationDto.getEnteteId()));
            formation.setEntete(entete);
        }

        if (formationDto.getResponsableEvaluationId() != null) {
            Utilisateur responsable = utilisateurRepository.findById(formationDto.getResponsableEvaluationId())
                    .orElseThrow(() -> new UsernameNotFoundException("Responsable introuvable"));

            formation.setResponsableEvaluation(responsable);

            System.out.println("➡️ Création de notification pour le responsable : " + responsable.getNom());
            
            notificationService.creerNotification(responsable, 
                "Vous avez été assigné comme responsable d'évaluation pour la formation : " + formation.getTitre());
        }

        // Récupérer l'objet RH à partir de l'ID
        Long organisateurId = formationDto.getOrganisateurId();
        if (organisateurId != null) {
            java.util.Optional<RH> organisateur = rhrepository.findById(organisateurId);
            if (organisateur.isPresent()) {
                formation.setOrganisateur(organisateur.get()); // Affecter l'objet RH
            } else {
                throw new IllegalArgumentException("Organisateur not found with ID: " + organisateurId);
            }
        }

        // Récupérer l'objet Utilisateur à partir de l'ID (non obligatoire)
        Long responsableEvaluationId = formationDto.getResponsableEvaluationId();
        if (responsableEvaluationId != null) {
            java.util.Optional<Utilisateur> responsableEvaluation = utilisateurRepository.findById(responsableEvaluationId);
            if (responsableEvaluation.isPresent()) {
                formation.setResponsableEvaluation(responsableEvaluation.get()); // Affecter l'objet Utilisateur
            } else {
                throw new IllegalArgumentException("Responsable evaluation not found with ID: " + responsableEvaluationId);
            }
        }
        
        
        // Ajouter le responsable externe (non obligatoire)
        formation.setResponsableEvaluationExterne(formationDto.getResponsableEvaluationExterne());

        Formation savedFormation = formationRepository.save(formation);

      

        List<Long> employeIds = formationDto.getEmployeIds();
        List<Employe> employes = employeRepository.findAllById(employeIds);

        for (Employe employe : employes) {
            formation_employe formationEmploye = new formation_employe();
            formationEmploye.setEmploye(employe);
            formationEmploye.setFormation(savedFormation);
        
            formationEmployeRepository.save(formationEmploye);
        }
        
        
        

        return savedFormation.getId(); // Retourner uniquement l'ID de la formation
    }
    
    
    @Transactional
    public void modifierDocumentEmployeFormation(Long formationId, Long employeId, MultipartFile fichierPdf) throws IOException {
        // Vérifier si l'employé et la formation existent
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé avec l'ID : " + employeId));

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + formationId));

        // Récupérer l'entrée dans la table formation_employe
        Optional<formation_employe> optionalFormationEmploye = employeFormationRepository.findByEmployeIdAndFormationId(employeId, formationId);
        formation_employe formationEmploye = optionalFormationEmploye
                .orElseThrow(() -> new EntityNotFoundException("Aucune entrée trouvée pour cet employé et cette formation."));

        // Mettre à jour le document
        byte[] pdfBytes = fichierPdf.getBytes();
        formationEmploye.setDocument(pdfBytes);
        formationEmploye.setEvalue(true);
        // Sauvegarder les modifications
        formationEmployeRepository.save(formationEmploye);
    }
    
    
    
    
    @Transactional
    public void verifierEtValiderFormation(Long formationId) {
        // Récupérer la formation
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée avec l'ID : " + formationId));

        // Récupérer tous les employés associés à cette formation
        List<formation_employe> formationsEmployes = employeFormationRepository.findByFormationId(formationId);

        // Vérifier si tous les employés ont été évalués
        boolean tousEvalués = formationsEmployes.stream()
                .allMatch(formationEmploye -> formationEmploye.isEvalue());

        // Si tous les employés ont été évalués, mettre à jour le champ valide de la formation
        if (tousEvalués) {
            formation.setValide(true);
            formation.setCommente(false);
            formation.setCommentaire(null);
            formation.setProbleme(false);
            formationRepository.save(formation);
        } else {
            throw new IllegalStateException("Tous les employés n'ont pas été évalués pour cette formation.");
        }
    }
    
    
    
    
    @Transactional
    public Formation addCommentaire(Long formationId, String nouveauCommentaire) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Concaténer le nouveau commentaire avec l'ancien (s'il existe)
        String ancienCommentaire = formation.getCommentaire();
        if (ancienCommentaire == null || ancienCommentaire.isEmpty()) {
            formation.setCommentaire(nouveauCommentaire);
        } else {
            formation.setCommentaire(ancienCommentaire + "\n" + nouveauCommentaire);
        }

        formation.setCommente(true); // Marquer que la formation a été commentée
        return formationRepository.save(formation);
    }
    
    
    
    

    @Transactional
    public void modifierFormation(Long id, FormationDto formationDto) throws IOException {
        // Vérifier si la formation existe
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formation non trouvée avec l'ID : " + id));

        // Vérifier si la formation est déjà validée
        if (formation.isValide()) {
            throw new IllegalArgumentException("La formation est déjà validée et ne peut pas être modifiée.");
        }

        // Mettre à jour les champs de la formation
        formation.setTitre(formationDto.getTitre());
        formation.setDescription(formationDto.getDescription());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setSousTypeFormation(formationDto.getSousTypeFormation());
        formation.setDateDebutPrevue(formationDto.getDateDebutPrevue());
        formation.setDateFinPrevue(formationDto.getDateFinPrevue());
        formation.setTitrePoste(formationDto.getTitrePoste());
      
        // Mettre à jour l'organisateur
        Long organisateurId = formationDto.getOrganisateurId();
        if (organisateurId != null) {
            RH organisateur = rhrepository.findById(organisateurId)
                    .orElseThrow(() -> new IllegalArgumentException("Organisateur non trouvé avec l'ID : " + organisateurId));
            formation.setOrganisateur(organisateur);
        }

        // Mettre à jour le responsable d'évaluation (interne ou externe)
        Long responsableEvaluationId = formationDto.getResponsableEvaluationId();
        if (responsableEvaluationId != null) {
            Utilisateur responsableEvaluation = utilisateurRepository.findById(responsableEvaluationId)
                    .orElseThrow(() -> new IllegalArgumentException("Responsable d'évaluation non trouvé avec l'ID : " + responsableEvaluationId));
            formation.setResponsableEvaluation(responsableEvaluation);
        } else {
            formation.setResponsableEvaluationExterne(formationDto.getResponsableEvaluationExterne());
        }

        // Mettre à jour le fichier PDF si fourni
        MultipartFile fichierPdf = formationDto.getFichierPdf();
        if (fichierPdf != null && !fichierPdf.isEmpty()) {
            byte[] pdfBytes = fichierPdf.getBytes();
            formation.setFichierPdfUrl("url_du_fichier_pdf"); // Mettre à jour l'URL du fichier PDF
        }

        // Sauvegarder la formation mise à jour
        Formation updatedFormation = formationRepository.save(formation);

        // Mettre à jour les employés associés
        List<Long> employeIds = formationDto.getEmployeIds();
        List<Employe> employes = employeRepository.findAllById(employeIds);

        // Supprimer les anciennes associations
        formationEmployeRepository.deleteByFormationId(updatedFormation.getId());

        // Ajouter les nouvelles associations
        for (Employe employe : employes) {
        	formation_employe formationEmploye = new formation_employe();
            formationEmploye.setEmploye(employe);
            formationEmploye.setFormation(updatedFormation);
            formationEmploye.setDocument(fichierPdf != null ? fichierPdf.getBytes() : null);
            formation.getPeriodes().clear();
            for (FormationDto.PeriodeDto periodeDto : formationDto.getPeriodes()) {
                PeriodeFormation periode = new PeriodeFormation();
                periode.setFormateur(periodeDto.getFormateur());
                periode.setProgramme(periodeDto.getProgramme());
                periode.setDateDebut(periodeDto.getDateDebut());
                periode.setDateFin(periodeDto.getDateFin());
                periode.setFormation(formation);
                formation.getPeriodes().add(periode);
            }

            formationEmployeRepository.save(formationEmploye);
        }
    }
    
    
    
    @Transactional
    public void ajouterResultat(Long formationId, Long employeId, ResultatFormation resultat, boolean habilite) {
        formation_employe formationEmploye = employeFormationRepository.findByEmployeIdAndFormationId(employeId, formationId)
                .orElseThrow(() -> new EntityNotFoundException("Aucune entrée trouvée pour cet employé et cette formation."));

        formationEmploye.setResultat(resultat);
        formationEmploye.setRes(true); // Changer le booléen Resultat en true
        if (resultat == ResultatFormation.REUSSI && habilite) {
            formationEmploye.setCapabilite(true);
        } else {
            formationEmploye.setCapabilite(false);
        }
        employeFormationRepository.save(formationEmploye);
    }
    @Transactional
    public Map<String, Object> getResultatFormation(Long formationId, Long employeId) {
        formation_employe formationEmploye = employeFormationRepository.findByEmployeIdAndFormationId(employeId, formationId)
                .orElseThrow(() -> new EntityNotFoundException("Aucune entrée trouvée pour cet employé et cette formation."));

        Map<String, Object> response = new HashMap<>();

        // Ajouter le résultat de la formation
        if (formationEmploye.getResultat() != null) {
            response.put("resultat", formationEmploye.getResultat().name());
        } else {
            response.put("resultat", "Aucun résultat disponible");
        }

        // Ajouter la propriété res de formation_employe
        response.put("res", formationEmploye.isRes());

        return response;
    }
   
    
    
    @Transactional
    public Formation creerFormationavecResultat(FormationDto_Resultat formationDto, Long rhId) {
        RH organisateur = (RH) utilisateurRepository.findById(rhId)
                .orElseThrow(() -> new UsernameNotFoundException("RH non trouvé"));

        Formation formation = new Formation();
        formation.setTitre(formationDto.getTitre());
        formation.setDescription(formationDto.getDescription());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setSousTypeFormation(formationDto.getSousTypeFormation());
        formation.setDateDebutPrevue(formationDto.getDateDebutPrevue());
        formation.setDateFinPrevue(formationDto.getDateFinPrevue());
        formation.setOrganisateur(organisateur);
        formation.setValide(true);
        // Ajoutez cette partie pour gérer le responsable
        if (formationDto.getResponsableEvaluationId() != null) {
            Responsable responsable = (Responsable) utilisateurRepository.findById(formationDto.getResponsableEvaluationId())
                    .orElseThrow(() -> new EntityNotFoundException("Responsable non trouvé"));
            formation.setResponsableEvaluation(responsable);
        } else if (formationDto.getResponsableEvaluationExterne() != null) {
            formation.setResponsableEvaluationExterne(formationDto.getResponsableEvaluationExterne());
        }
        
        formationRepository.save(formation);
        
        for (EmployeResultatDto employeDto : formationDto.getEmployes()) {
            Employe employe = employeRepository.findById(employeDto.getEmployeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));
            
            formation_employe formationEmploye = new formation_employe();
            formationEmploye.setEmploye(employe);
            formationEmploye.setFormation(formation);
            formationEmploye.setResultat(employeDto.getResultat());
            formationEmploye.setRes(true);
            employeFormationRepository.save(formationEmploye);
        }
        
        return formation;
    }
    
    @Transactional
    public Formation modifierFormationAvecResultat(FormationDto_Resultat formationDto, Long rhId, Long formationId) {
        // Vérifier que le RH existe
        RH organisateur = (RH) utilisateurRepository.findById(rhId)
                .orElseThrow(() -> new UsernameNotFoundException("RH non trouvé"));

        // Récupérer la formation existante
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée"));

        // Mettre à jour les propriétés de la formation
        formation.setTitre(formationDto.getTitre());
        formation.setDescription(formationDto.getDescription());
        formation.setTypeFormation(formationDto.getTypeFormation());
        formation.setSousTypeFormation(formationDto.getSousTypeFormation());
        formation.setDateDebutPrevue(formationDto.getDateDebutPrevue());
        formation.setDateFinPrevue(formationDto.getDateFinPrevue());
        formation.setOrganisateur(organisateur);
        
        if (formationDto.getResponsableEvaluationId() != null) {
            Responsable responsable = (Responsable) utilisateurRepository.findById(formationDto.getResponsableEvaluationId())
                    .orElseThrow(() -> new EntityNotFoundException("Responsable non trouvé"));
            formation.setResponsableEvaluation(responsable);
            formation.setResponsableEvaluationExterne(null); // Effacer le responsable externe si on passe à un responsable interne
        } else if (formationDto.getResponsableEvaluationExterne() != null) {
            formation.setResponsableEvaluationExterne(formationDto.getResponsableEvaluationExterne());
            formation.setResponsableEvaluation(null); // Effacer le responsable interne si on passe à un responsable externe
        } else {
            // Si aucun responsable n'est fourni, conserver les valeurs existantes
        }
        
        // Sauvegarder les modifications de la formation
        formationRepository.save(formation);
        
        // Supprimer les anciennes associations employés-formation
        employeFormationRepository.deleteByFormationId(formationId);
        
        // Créer les nouvelles associations
        for (EmployeResultatDto employeDto : formationDto.getEmployes()) {
            Employe employe = employeRepository.findById(employeDto.getEmployeId())
                    .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));
            
            formation_employe formationEmploye = new formation_employe();
            formationEmploye.setEmploye(employe);
            formationEmploye.setFormation(formation);
            formationEmploye.setResultat(employeDto.getResultat());
            formationEmploye.setRes(true);
            employeFormationRepository.save(formationEmploye);
        }
        
        return formation;
    }
    @Transactional
    public List<formation_employe> getFormationsWithDetailsByEmploye(Long employeId) {
        return formationEmployeRepository.findByEmployeIdWithDetails(employeId);
    }
    
    
    @Transactional
    public Formation signalerProbleme(Long formationId, String commentaire) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée"));
        
        // Mettre à jour les champs
        formation.setProbleme(true);
        formation.setValide(false);
        
        // Remplacer l'ancien commentaire par le nouveau
        if (commentaire != null && !commentaire.isEmpty()) {
            formation.setCommentaire(commentaire);
            formation.setCommente(true);
        } else {
            formation.setCommentaire(null);
            formation.setCommente(false);
        }
        
        return formationRepository.save(formation);
    }

    

    @Transactional
    public Formation retirerProbleme(Long formationId) {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée"));

        // Réinitialiser les champs liés au problème
        formation.setProbleme(false);
        formation.setValide(true); // ou false selon ton cas métier
        formation.setCommentaire(null);
        formation.setCommente(false);

        return formationRepository.save(formation);
    }

    
    public Formation annulerFormation(Long id) {
        Formation f = formationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée"));
        f.setAnnuler(true);
        f.setDateAnnulation(LocalDate.now());
        return formationRepository.save(f);
    }

    // Réactiver (désanuler) la formation
    public Formation reactiverFormation(Long id) {
        Formation f = formationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Formation non trouvée"));
        f.setAnnuler(false);
        f.setDateAnnulation(null);
        return formationRepository.save(f);
    }
    
    
    
    
}
    
    
    
    
    
    
    
	

   

