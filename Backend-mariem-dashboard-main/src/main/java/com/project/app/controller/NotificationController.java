package com.project.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.Notification;
import com.project.app.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    

    @PostMapping("/utilisateur/{id}/lire")
    public void marquerNotificationsCommeLues(@PathVariable("id") Long id) {
        notificationService.marquerCommeLues(id);
    }
    
    @PostMapping("/{id}/lire")
    public void marquerUneCommeLue(@PathVariable("id") Long id) {
        notificationService.marquerUneCommeLue(id);
    }
    
    @GetMapping("/utilisateur/{id}")
    public List<Notification> getNotifications(@PathVariable("id") Long id) {
        return notificationService.getNotificationsParUtilisateur(id);
    }


}
