package com.project.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Notification;

import jakarta.transaction.Transactional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUtilisateurIdAndLueFalse(Long utilisateurId);
    List<Notification> findByUtilisateurId(Long utilisateurId);
    
    @Transactional
    void deleteByLueTrueAndDateNotificationBefore(LocalDateTime date);
}

