package com.project.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.Permission;
import com.project.app.model.SuperAdmin;
import com.project.app.model.Utilisateur;
import com.project.app.repository.PermissionRepository;
import com.project.app.repository.UtilisateurRepository;

@Service
public class PermissionService {
	
	private final PermissionRepository permissionRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository, UtilisateurRepository utilisateurRepository) {
        this.permissionRepository = permissionRepository;
        this.utilisateurRepository = utilisateurRepository;
    }
    
    public Permission createPermission(String name) {
        if (permissionRepository.findByName(name).isPresent()) {
            throw new RuntimeException("Cette permission existe déjà !");
        }
        Permission permission = new Permission();
        permission.setName(name);
        return permissionRepository.save(permission);
    }
    
    public Utilisateur addPermissionToUser(Long userId, String permissionName) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée : " + permissionName));

        // Vérifier si l'utilisateur a déjà la permission
        if (utilisateur.getPermissions().contains(permission)) {
            throw new RuntimeException("L'utilisateur possède déjà cette permission !");
        }

        // Ajouter la permission
        utilisateur.getPermissions().add(permission);
        return utilisateurRepository.save(utilisateur);
    }


    public Utilisateur removePermissionFromUser(Long userId, String permissionName) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (utilisateur instanceof SuperAdmin) {
            throw new RuntimeException("Impossible de retirer une permission au Super Admin !");
        }

        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée : " + permissionName));

        if (!utilisateur.getPermissions().contains(permission)) {
            throw new RuntimeException("L'utilisateur ne possède pas cette permission !");
        }

        utilisateur.getPermissions().remove(permission);
        return utilisateurRepository.save(utilisateur);
    }


    
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

}