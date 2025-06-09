package com.project.app.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.project.app.model.Permission;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UtilisateurResponseDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String username;
    private String role;
    private LocalDateTime lastLogin;
    private Set<Permission> permissions;
    private Boolean accountLocked; // Ajoutez cette ligne
    
    public UtilisateurResponseDto(Long id, String nom, String prenom, String email, 
                                String username, String role, Set<Permission> permissions, 
                                LocalDateTime lastLogin, Boolean accountLocked) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.username = username;
        this.role = role;
        this.permissions = permissions;
        this.lastLogin = lastLogin;
        this.accountLocked = accountLocked;
    }
}