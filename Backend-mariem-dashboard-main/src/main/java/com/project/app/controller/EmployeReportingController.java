package com.project.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.EmployeReportingDTO;
import com.project.app.dto.EmployeReportingProjectionDTO;
import com.project.app.service.EmployeReportingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reporting/employes")
@RequiredArgsConstructor
public class EmployeReportingController {
    
    private final EmployeReportingService reportingService;
    
    @GetMapping
    public ResponseEntity<List<EmployeReportingDTO>> getReportingEmployes() {
        return ResponseEntity.ok(reportingService.generateReport());
    }
}