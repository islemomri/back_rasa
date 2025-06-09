package com.project.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.Employe;
import com.project.app.model.Stage;
import com.project.app.repository.EmployeRepository;
import com.project.app.repository.StageRepository;

@Service
public class StageService implements IStageService {

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Override
    public List<Stage> getStagesByEmployeId(Long employeId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        return employe.getStages();
    }

    @Override
    public Stage addStageToEmploye(Long employeId, Stage stage) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        stage.setEmploye(employe);
        return stageRepository.save(stage);
    }

    @Override
    public void removeStageFromEmploye(Long employeId, Long stageId) {
        Employe employe = employeRepository.findById(employeId)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé"));
        
        employe.getStages().remove(stage);
        stage.setEmploye(null);
        
        employeRepository.save(employe);
        stageRepository.delete(stage);
    }

    @Override
    public Stage updateStage(Long stageId, Stage updatedStage) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new RuntimeException("Stage non trouvé"));
        
        stage.setSociete(updatedStage.getSociete());
        stage.setDateDebut(updatedStage.getDateDebut());
        stage.setDateFin(updatedStage.getDateFin());
        
        return stageRepository.save(stage);
    }
}