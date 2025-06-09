package com.project.app.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.CreatePermissionRequest;
import com.project.app.dto.PermissionRequest;
import com.project.app.model.Permission;
import com.project.app.model.Utilisateur;
import com.project.app.repository.PermissionRepository;
import com.project.app.repository.UtilisateurRepository;
import com.project.app.service.PermissionService;

@RestController
@RequestMapping("/permissions")
public class PermissionController {


    @Autowired
	private PermissionService permissionService;

    @PostMapping("/add")
    public ResponseEntity<Permission> createPermission(@RequestBody CreatePermissionRequest request) {
        Permission permission = permissionService.createPermission(request.getName());
        return ResponseEntity.ok(permission);
    }
    
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @PostMapping("/assign")
    public ResponseEntity<Utilisateur> assignPermission(@RequestBody PermissionRequest request) {
        Utilisateur utilisateur = permissionService.addPermissionToUser(request.getUserId(), request.getPermissionName());
        return ResponseEntity.ok(utilisateur);
    }
    
    @PostMapping("/remove")
    public ResponseEntity<Utilisateur> removePermission(@RequestBody PermissionRequest request) {
        Utilisateur utilisateur = permissionService.removePermissionFromUser(request.getUserId(), request.getPermissionName());
        return ResponseEntity.ok(utilisateur);
    }
}
