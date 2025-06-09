package com.project.app.service;

import java.util.List;

import com.project.app.model.CompetencePoste;

public interface ICompetencePosteService {
    CompetencePoste create(CompetencePoste competence);
    CompetencePoste update(Long id, CompetencePoste competence);
    void delete(Long id);
    CompetencePoste getById(Long id);
    List<CompetencePoste> getAll();
}
