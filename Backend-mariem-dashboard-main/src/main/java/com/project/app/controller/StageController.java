package com.project.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.Stage;
import com.project.app.service.StageService;

@RestController
@RequestMapping("/stages")
public class StageController {

    @Autowired
    private StageService stageService;

    @PostMapping("/{employeId}")
    public ResponseEntity<Stage> addStageToEmploye(@PathVariable("employeId") Long employeId, @RequestBody Stage stage) {
        Stage savedStage = stageService.addStageToEmploye(employeId, stage);
        return ResponseEntity.ok(savedStage);
    }

    @DeleteMapping("/{employeId}/stages/{stageId}")
    public void removeStageFromEmploye(@PathVariable ("employeId") Long employeId, @PathVariable ("stageId")Long stageId) {
        stageService.removeStageFromEmploye(employeId, stageId);
    }

    @PutMapping("/{stageId}")
    public Stage updateStage(@PathVariable ("stageId")  Long stageId, @RequestBody Stage updatedStage) {
        return stageService.updateStage(stageId, updatedStage);
    }

    @GetMapping("/{employeId}/stages")
    public List<Stage> getStagesByEmployeId(@PathVariable  ("employeId") Long employeId) {
        return stageService.getStagesByEmployeId(employeId);
    }
}