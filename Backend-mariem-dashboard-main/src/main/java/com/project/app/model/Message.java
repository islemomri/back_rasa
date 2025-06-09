package com.project.app.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @ManyToOne(optional = false)  // Forces expediteur to be non-null
    @JoinColumn(name = "expediteur_id", nullable = false)
    private Utilisateur expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Utilisateur destinataire;

    private LocalDateTime dateEnvoi;

    private boolean lu = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "parent_id")
    private Message messageParent;
    
    
    @OneToMany(mappedBy = "messageParent", cascade = CascadeType.ALL)
    private List<Message> reponses = new ArrayList<>();
    public String getExpediteurNomComplet() {
        return expediteur != null ? expediteur.getNom() + ' ' + expediteur.getPrenom() : "";
    }

    public String getDestinataireNomComplet() {
        return destinataire != null ? destinataire.getNom() + ' ' + destinataire.getPrenom() : "";
    }
}
