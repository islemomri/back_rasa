package com.project.app.controller;

import java.util.List;
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

import com.project.app.dto.MessageDto;
import com.project.app.model.Message;
import com.project.app.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/envoyer")
    public ResponseEntity<?> envoyerMessage(@RequestBody MessageDto messageDto) {
        if (messageDto.getExpediteurId() == null || messageDto.getDestinataireIds() == null || messageDto.getDestinataireIds().isEmpty()) {
            return ResponseEntity.badRequest().body("Expéditeur et destinataires doivent être spécifiés");
        }
        List<Message> messages = messageService.envoyerMessage(
                messageDto.getExpediteurId(),
                messageDto.getDestinataireIds(),
                messageDto.getSujet(),
                messageDto.getContenu(),
                messageDto.getMessageParentId());
        return ResponseEntity.ok(messages);
    }




    @GetMapping("/recus/{userId}")
    public ResponseEntity<List<Message>> getMessagesRecus(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(messageService.getMessagesRecus(userId));
    }

    @GetMapping("/envoyes/{userId}")
    public ResponseEntity<List<Message>> getMessagesEnvoyes(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(messageService.getMessagesEnvoyes(userId));
    }

    @PutMapping("/lu/{messageId}")
    public ResponseEntity<?> marquerCommeLu(@PathVariable("messageId") Long messageId) {
        messageService.marquerCommeLu(messageId);
        return ResponseEntity.ok("Message marqué comme lu");
    }
    
    @GetMapping("/thread/{messageId}")
    public ResponseEntity<List<MessageDto>> getFilDiscussion(@PathVariable("messageId") Long messageId) {
        List<Message> messages = messageService.getFilDiscussion(messageId);
        List<MessageDto> dtos = messages.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private MessageDto convertToDto(Message m) {
        MessageDto dto = new MessageDto();
        dto.setId(m.getId());
        dto.setSujet(m.getSujet());
        dto.setContenu(m.getContenu());
        
        // Handle expediteur
        if (m.getExpediteur() != null) {
            dto.setExpediteurId(m.getExpediteur().getId());
            dto.setExpediteurNom(m.getExpediteur().getNom());
            dto.setExpediteurPrenom(m.getExpediteur().getPrenom());
        }
        
        // Handle destinataire
        if (m.getDestinataire() != null) {
            dto.setDestinataireId(m.getDestinataire().getId());
            dto.setDestinataireNom(m.getDestinataire().getNom());
            dto.setDestinatairePrenom(m.getDestinataire().getPrenom());
        }
        
        dto.setLu(m.isLu());
        dto.setMessageParentId(m.getMessageParent() != null ? m.getMessageParent().getId() : null);
        return dto;
    }

    
    @PostMapping("/repondre")
    public ResponseEntity<?> repondreMessage(@RequestBody MessageDto dto) {
        if (dto.getExpediteurId() == null || dto.getDestinataireId() == null) {
            return ResponseEntity.badRequest().body("Expéditeur et destinataire doivent être spécifiés");
        }

        Message reponse = messageService.repondreMessage(
            dto.getExpediteurId(),
            dto.getDestinataireId(),
            dto.getSujet(),
            dto.getContenu(),
            dto.getMessageParentId()
        );

        return ResponseEntity.ok(reponse);
    }




}

