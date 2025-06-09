package com.project.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.project.app.model.Notification;
import com.project.app.model.Utilisateur;
import com.project.app.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void creerNotification(Utilisateur utilisateur, String message) {
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateur);
        notification.setMessage(message);
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsNonLues(Long utilisateurId) {
        return notificationRepository.findByUtilisateurIdAndLueFalse(utilisateurId);
    }

    public void marquerCommeLues(Long utilisateurId) {
        List<Notification> notifications = notificationRepository.findByUtilisateurIdAndLueFalse(utilisateurId);
        notifications.forEach(n -> n.setLue(true));
        notificationRepository.saveAll(notifications);
    }
    
    public void marquerUneCommeLue(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow();
        notification.setLue(true);
        notificationRepository.save(notification);
    }
    
    public List<Notification> getNotificationsParUtilisateur(Long utilisateurId) {
        return notificationRepository.findByUtilisateurId(utilisateurId);
    }
    
    @Scheduled(fixedRate = 300000) 
    public void supprimerNotificationsLues() {
        LocalDateTime dateLimite = LocalDateTime.now().minusMinutes(5);
        notificationRepository.deleteByLueTrueAndDateNotificationBefore(dateLimite);
    }

    
    
}
