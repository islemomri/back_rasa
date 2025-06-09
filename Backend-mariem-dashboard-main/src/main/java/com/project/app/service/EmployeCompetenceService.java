package com.project.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.app.dto.EmployeCompetenceDTO;
import com.project.app.model.Competence;
import com.project.app.model.Employe;
import com.project.app.model.EmployeCompetence;
import com.project.app.repository.CompetenceRepository;
import com.project.app.repository.EmployeCompetenceRepository;
import com.project.app.repository.EmployeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeCompetenceService {

    private final EmployeRepository employeRepository;
    private final CompetenceRepository competenceRepository;
    private final EmployeCompetenceRepository employeCompetenceRepository;

    public List<EmployeCompetence> getCompetencesByEmploye(Long employeId) {
        return employeCompetenceRepository.findByEmployeId(employeId);
    }

    public EmployeCompetence addCompetenceToEmploye(Long employeId, EmployeCompetenceDTO dto) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));

        Competence competence = competenceRepository.findById(dto.getCompetenceId())
                .orElseThrow(() -> new RuntimeException("Compétence non trouvée"));

       
        boolean exists = employeCompetenceRepository.existsByEmployeIdAndCompetenceId(employeId, dto.getCompetenceId());
        if (exists) {
            throw new RuntimeException("Cette compétence est déjà associée à cet employé");
        }

        EmployeCompetence ec = new EmployeCompetence();
        ec.setEmploye(employe);
        ec.setCompetence(competence);
        ec.setNiveau(dto.getNiveau());

        return employeCompetenceRepository.save(ec);
    }

    public EmployeCompetence updateNiveau(Long id, String niveau) {
        EmployeCompetence ec = employeCompetenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Association non trouvée"));

        ec.setNiveau(niveau);
        return employeCompetenceRepository.save(ec);
    }

    public void deleteAssociation(Long id) {
        EmployeCompetence ec = employeCompetenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Association non trouvée"));

        employeCompetenceRepository.delete(ec);
    }
}
