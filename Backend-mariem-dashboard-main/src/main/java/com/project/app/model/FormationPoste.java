package com.project.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FormationPoste {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "formation_id", nullable = false)
    private Long formationId;

    @Column(name = "poste_id", nullable = false)
    private Long posteId;

    // Constructeurs, getters et setters
    public FormationPoste() {}

    public FormationPoste(Long formationId, Long posteId) {
        this.formationId = formationId;
        this.posteId = posteId;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFormationId() {
        return formationId;
    }

    public void setFormationId(Long formationId) {
        this.formationId = formationId;
    }

    public Long getPosteId() {
        return posteId;
    }

    public void setPosteId(Long posteId) {
        this.posteId = posteId;
    }
}