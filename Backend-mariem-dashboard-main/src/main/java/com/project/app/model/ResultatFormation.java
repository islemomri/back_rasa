package com.project.app.model;

public enum ResultatFormation {
    REUSSI("reussi"),
    ECHEC("echec"),
    PROGRAMME_COMPLEMENTAIRE("programme complementaire");

    private final String valeur;

    ResultatFormation(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return valeur;
    }
    
    
    
    
    
}