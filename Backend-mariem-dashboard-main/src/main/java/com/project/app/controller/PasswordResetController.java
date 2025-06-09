package com.project.app.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.service.PasswordResetService;

import jakarta.mail.MessagingException;
@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            passwordResetService.sendPasswordResetEmail(email);
            return ResponseEntity.ok("Email de réinitialisation envoyé avec succès.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoi de l'email.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String newPassword = request.get("newPassword");
            String confirmPassword = request.get("confirmPassword"); // Ajouter la validation
            
            // Ajouter une validation des mots de passe
            if (!newPassword.equals(confirmPassword)) {
                throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
            }
            
            passwordResetService.resetPassword(token, newPassword);
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("message", "Mot de passe réinitialisé avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}