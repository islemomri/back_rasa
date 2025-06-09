package com.project.app.model;

import java.util.Date;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@DiscriminatorValue("ANTERIEUR")
@Data
@EqualsAndHashCode(callSuper = true) // Les champs de Experience sont bien pris en compte dans equals() et hashCode()
public class ExperienceAnterieure extends Experience {
    
    private String societe;
    public ExperienceAnterieure() {
        super(); // Appel explicite au constructeur sans argument de la classe parente
    }


    public ExperienceAnterieure(Long id, Date dateDebut, Date dateFin, String poste, String societe, Employe employe) {
        super(id, dateDebut, dateFin, poste, employe);
        this.societe = societe;
    }
}
