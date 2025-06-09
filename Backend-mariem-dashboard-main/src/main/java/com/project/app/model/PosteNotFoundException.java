package com.project.app.model;

public class PosteNotFoundException extends RuntimeException {
    public PosteNotFoundException(String titrePoste, Integer matricule) {
        super("Le poste '" + titrePoste + "' n'est pas associé à l'employé de matricule " + matricule);
    }
}
