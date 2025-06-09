package com.project.app.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.app.model.Candidat;
import com.project.app.model.Poste;

@Service
public class RecrutementService {

    public double calculerScoreCompatibilite(Poste poste, Candidat candidat) {
        int score = 0;
        
        
        if (poste.getDiplomeRequis().equalsIgnoreCase(candidat.getDiplome())) {
            score += 30;
        }

        
        if (poste.getNiveauExperience().equalsIgnoreCase(candidat.getNiveauExperience())) {
            score += 20;
        }

        
        List<String> competencesRequises = Arrays.asList(poste.getCompetencesRequises().split(","));
        List<String> competencesCandidat = Arrays.asList(candidat.getCompetences().split(","));

        int competencesMatch = (int) competencesCandidat.stream()
                .filter(competencesRequises::contains)
                .count();

        score += competencesMatch * 10;
        
        return Math.min(score, 100); 
    }

    public List<Candidat> suggererCandidats(Poste poste, List<Candidat> candidats) {
        return candidats.stream()
                .peek(c -> c.setScoreRecommandation(calculerScoreCompatibilite(poste, c)))
                .sorted(Comparator.comparingDouble(Candidat::getScoreRecommandation).reversed())
                .collect(Collectors.toList());
    }
}

