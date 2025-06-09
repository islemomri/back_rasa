package com.project.app.service;

import java.util.List;

import com.project.app.model.Stage;

public interface IStageService {
    List<Stage> getStagesByEmployeId(Long employeId);
    Stage addStageToEmploye(Long employeId, Stage stage);
    void removeStageFromEmploye(Long employeId, Long stageId);
    Stage updateStage(Long stageId, Stage updatedStage);
}