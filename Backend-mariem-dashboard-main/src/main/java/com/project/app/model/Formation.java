package com.project.app.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private TypeFormation typeFormation; 

    @Enumerated(EnumType.STRING)
    private SousTypeFormation sousTypeFormation;

    private LocalDate dateDebutPrevue;
    private LocalDate dateFinPrevue;
    private LocalDate dateDebutReelle;
    private LocalDate dateFinReelle;
    private boolean emailEnvoye;
    private String fichierPdfUrl;
    private boolean valide= false;
    @Column(length = 1000)
    private String commentaire;
    private boolean commente = false;
    private boolean probleme = false; 
    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PeriodeFormation> periodes = new ArrayList<>();
   
    @Column(nullable = false)
    private boolean annuler = false;

    private LocalDate dateAnnulation;
    // Getters et setters
    public String getFichierPdfUrl() {
        return fichierPdfUrl;
    }

    public void setFichierPdfUrl(String fichierPdfUrl) {
        this.fichierPdfUrl = fichierPdfUrl;
    }
    // Getters et setters
    public boolean isEmailEnvoye() {
        return emailEnvoye;
    }

    public void setEmailEnvoye(boolean emailEnvoye) {
        this.emailEnvoye = emailEnvoye;
    }

    @ManyToOne
    @JsonIgnore
    private RH organisateur;

    @ManyToOne
    @JoinColumn(name = "responsable_evaluation_id")
    private Utilisateur responsableEvaluation; 
    
    @ManyToOne
    @JoinColumn(name = "entete_id") // clé étrangère
    private Entete entete;

    private String responsableEvaluationExterne; 
    
    private String titrePoste; 
    @ManyToMany
    @JoinTable(
        name = "formation_employe",
        joinColumns = @JoinColumn(name = "formation_id"),
        inverseJoinColumns = @JoinColumn(name = "employe_id")
    )
    private List<Employe> employes = new ArrayList<>();
    private LocalDate dateRappel; // Optionnelle : si null, on prend dateFinPrevue - 2

    @Column(name = "rappel_envoye", columnDefinition = "boolean default false")
    private boolean rappelEnvoye = false;

    // Getters & Setters
    public boolean isRappelEnvoye() {
        return rappelEnvoye;
    }

    public void setRappelEnvoye(boolean rappelEnvoye) {
        this.rappelEnvoye = rappelEnvoye;
    }
    
    
    @Column(name = "rappel_obligation_envoye", columnDefinition = "boolean default false")
    private boolean rappelObligationEnvoye = false;

    // Getters & Setters
    public boolean isRappelObligationEnvoye() {
        return rappelObligationEnvoye;
    }

    public void setRappelObligationEnvoye(boolean rappelObligationEnvoye) {
        this.rappelObligationEnvoye = rappelObligationEnvoye;
    }
    
    @Column(name = "confirmation_organisateur_envoyee", columnDefinition = "boolean default false")
    private boolean confirmationOrganisateurEnvoyee = false;

    // Getters & Setters
    public boolean isConfirmationOrganisateurEnvoyee() {
        return confirmationOrganisateurEnvoyee;
    }

    public void setConfirmationOrganisateurEnvoyee(boolean confirmationOrganisateurEnvoyee) {
        this.confirmationOrganisateurEnvoyee = confirmationOrganisateurEnvoyee;
    }
    


    
}