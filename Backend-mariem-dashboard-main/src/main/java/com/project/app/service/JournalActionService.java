package com.project.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.JournalAction;
import com.project.app.model.Utilisateur;
import com.project.app.repository.JournalActionRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JournalActionService {

    private final JournalActionRepository journalActionRepository;
    private final HttpServletRequest request;

    @Autowired
    public JournalActionService(JournalActionRepository journalActionRepository,
    		HttpServletRequest request) {
        this.journalActionRepository = journalActionRepository;
        this.request=request;
    }

    public void logAction(Utilisateur utilisateur, String action, String description) {
        JournalAction journal = new JournalAction();
        journal.setUtilisateur(utilisateur);
        journal.setAction(action);
        journal.setDescription(description);
        journal.setTimestamp(LocalDateTime.now());
        journal.setIpAddress(getClientIp());
        journalActionRepository.save(journal);
    }
    
    private String getClientIp() {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public List<JournalAction> getUserActivity(Long userId) {
        return journalActionRepository.findByUtilisateurIdOrderByTimestampDesc(userId);
    }

    public List<JournalAction> getAllActivities() {
        return journalActionRepository.findAllByOrderByTimestampDesc();
    }
    
    public void logActionIfNeeded(Utilisateur utilisateur, String action, String description) {
        // Vérifier si une action similaire a déjà été enregistrée pour cet utilisateur dans les 30 dernières minutes
        LocalDateTime thirtyMinutesAgo = LocalDateTime.now().minusMinutes(30);
        
        // Chercher les actions de l'utilisateur qui ont eu lieu dans les 30 dernières minutes
        List<JournalAction> recentActions = journalActionRepository.findByUtilisateurIdAndTimestampAfterOrderByTimestampDesc(
            utilisateur.getId(), thirtyMinutesAgo);
        
        // Vérifier si une action similaire a déjà été enregistrée
        boolean actionExists = recentActions.stream()
            .anyMatch(actionRecord -> actionRecord.getAction().equals(action) && actionRecord.getDescription().equals(description));
        
        if (!actionExists) {
            // Si aucune action similaire n'a été trouvée, alors enregistrer l'action
            JournalAction journal = new JournalAction();
            journal.setUtilisateur(utilisateur);
            journal.setAction(action);
            journal.setDescription(description);
            journal.setTimestamp(LocalDateTime.now());
            journal.setIpAddress(getClientIp());
            journalActionRepository.save(journal);
        }
    }
    
    public List<JournalAction> getAllJournalActions() {
        return journalActionRepository.findAllByOrderByTimestampDesc();
    }


}

