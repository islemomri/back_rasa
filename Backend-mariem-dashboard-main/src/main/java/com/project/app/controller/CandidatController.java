package com.project.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.Candidat;
import com.project.app.repository.CandidatRepository;

@RestController
@RequestMapping("/recrutement/candidats")
public class CandidatController {

    @Autowired
    private CandidatRepository candidatRepository;

    @PostMapping
    public Candidat ajouterCandidat(@RequestBody Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    @GetMapping
    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }
}
