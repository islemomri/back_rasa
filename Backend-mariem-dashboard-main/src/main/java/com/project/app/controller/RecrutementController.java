package com.project.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.Candidat;
import com.project.app.model.Poste;
import com.project.app.repository.CandidatRepository;
import com.project.app.repository.PosteRepository;
import com.project.app.service.RecrutementService;

@RestController
@RequestMapping("/recrutement")
public class RecrutementController {

    @Autowired
    private RecrutementService recrutementService;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private PosteRepository posteRepository;

    @GetMapping("/suggere/{poste_id}")
    public List<Candidat> suggererCandidats(@PathVariable("poste_id") Long posteId) {
        Poste poste = posteRepository.findById(posteId)
                .orElseThrow(() -> new RuntimeException("Poste non trouvé"));
        
        List<Candidat> candidats = candidatRepository.findAll();
        
        return recrutementService.suggererCandidats(poste, candidats);
    }
}
