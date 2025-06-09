package com.project.app.model;

import java.util.Date;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ASSAD") // Identifie ce type d’expérience
@Data
@EqualsAndHashCode(callSuper = true) // Inclut les champs de Experience dans equals() et hashCode()

public class ExperienceAssad extends Experience {

    private String direction;
    private String modeAffectation; // Mutation, Nouveau recrutement ou Promotion
    
    public ExperienceAssad() {
        super(); // Appel explicite au constructeur sans argument de la classe parente
    }


    public ExperienceAssad(Long id, Date dateDebut, Date dateFin, String poste, String direction, String modeAffectation, Employe employe) {
        super(id, dateDebut, dateFin, poste, employe);
        this.direction = direction;
        this.modeAffectation = modeAffectation;
    }
}