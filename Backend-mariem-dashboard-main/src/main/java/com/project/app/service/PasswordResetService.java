package com.project.app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.app.model.PasswordResetToken;
import com.project.app.model.Utilisateur;
import com.project.app.repository.PasswordResetTokenRepository;
import com.project.app.repository.UtilisateurRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
@Service
public class PasswordResetService {
    
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(UtilisateurRepository utilisateurRepository, 
                                PasswordResetTokenRepository passwordResetTokenRepository, 
                                EmailService emailService, 
                                PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void sendPasswordResetEmail(String email) throws MessagingException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email non trouvé"));

        System.out.println("Utilisateur trouvé : " + utilisateur);

        // Supprimer l'ancien token s'il existe
        passwordResetTokenRepository.deleteByUtilisateur(utilisateur);
        System.out.println("Ancien token supprimé pour l'utilisateur : " + utilisateur.getId());

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUtilisateur(utilisateur);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));

        passwordResetTokenRepository.save(resetToken);
        System.out.println("Nouveau token enregistré pour l'utilisateur : " + utilisateur.getId());

        // Envoyer l'email avec le lien de réinitialisation
        String resetLink = "http://localhost:4200/reset-password?token=" + token;
        String emailBody = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink;
        emailService.sendEmail(email, "Réinitialisation de mot de passe", emailBody);
    }

    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nouveau mot de passe ne peut pas être vide.");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token invalide"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expiré");
        }

        Utilisateur utilisateur = resetToken.getUtilisateur();
        utilisateur.setPassword(passwordEncoder.encode(newPassword));
        utilisateurRepository.save(utilisateur);

        passwordResetTokenRepository.delete(resetToken); // Supprimer le token après utilisation
    }
}