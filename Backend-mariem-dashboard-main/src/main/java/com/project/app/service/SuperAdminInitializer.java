package com.project.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.app.model.SuperAdmin;
import com.project.app.model.Utilisateur;
import com.project.app.repository.UtilisateurRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class SuperAdminInitializer {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        Optional<Utilisateur> existing = utilisateurRepository.findByUsername("superadmin");
        if (existing.isEmpty()) {
            SuperAdmin superAdmin = new SuperAdmin();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("HoucemAdmin"));
            superAdmin.setNom("Super");
            superAdmin.setPrenom("Admin");
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setRole("SUPER_ADMIN"); // Doit être exactement 'SUPER_ADMIN' // Assurez-vous que le rôle est correctement défini
            utilisateurRepository.save(superAdmin);
        }
    }
}