package com.project.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date; // à importer pour le champ date
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Entete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    private String reference;

    private Integer numerorevision;

    @Temporal(TemporalType.DATE)
    private Date dateApplication;

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public Integer getNumerorevision() { return numerorevision; }
    public void setNumerorevision(Integer numerorevision) { this.numerorevision = numerorevision; }

    public Date getDateApplication() { return dateApplication; }
    public void setDateApplication(Date dateApplication) { this.dateApplication = dateApplication; }
}
