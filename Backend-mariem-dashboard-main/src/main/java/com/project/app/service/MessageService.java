package com.project.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.Message;
import com.project.app.model.Utilisateur;
import com.project.app.repository.MessageRepository;
import com.project.app.repository.UtilisateurRepository;
	
	@Service
	public class MessageService {
	
	    @Autowired
	    private MessageRepository messageRepository;
	
	    @Autowired
	    private UtilisateurRepository utilisateurRepository;
	
	    public List<Message> envoyerMessage(Long expediteurId, List<Long> destinataireIds, String sujet, String contenu, Long parentId) {
	        if (expediteurId == null || destinataireIds == null || destinataireIds.isEmpty()) {
	            throw new IllegalArgumentException("Expéditeur et au moins un destinataire doivent être spécifiés");
	        }

	        Utilisateur expediteur = utilisateurRepository.findById(expediteurId)
	                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));

	        Message messageParent = null;
	        if (parentId != null) {
	            messageParent = messageRepository.findById(parentId)
	                    .orElseThrow(() -> new RuntimeException("Message parent non trouvé"));
	        }

	        List<Message> messages = new ArrayList<>();

	        for (Long destinataireId : destinataireIds) {
	            Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
	                    .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

	            Message message = new Message();
	            message.setExpediteur(expediteur);
	            message.setDestinataire(destinataire);
	            message.setSujet(sujet);
	            message.setContenu(contenu);
	            message.setDateEnvoi(LocalDateTime.now());
	            message.setMessageParent(messageParent);

	            messages.add(messageRepository.save(message));
	        }

	        return messages;
	    }
	
	
	    public List<Message> getFilDiscussion(Long messageId) {
	        if (messageId == null) {
	            throw new IllegalArgumentException("L'ID du message ne peut pas être nul");
	        }
	        Message message = messageRepository.findById(messageId)
	                .orElseThrow(() -> new RuntimeException("Message non trouvé"));
	        
	        Message root = message.getMessageParent() == null ? message : message.getMessageParent();
	        
	        return flattenThread(root);
	    }
	
	    
	    private List<Message> flattenThread(Message root) {
	        List<Message> thread = new ArrayList<>();
	        thread.add(root);
	        collectReplies(root, thread);
	        return thread;
	    }
	    
	    private void collectReplies(Message parent, List<Message> thread) {
	        for (Message reply : parent.getReponses()) {
	            thread.add(reply);
	            collectReplies(reply, thread);
	        }
	    }
	
	    public List<Message> getMessagesRecus(Long userId) {
	        return messageRepository.findByDestinataireIdOrderByDateEnvoiDesc(userId);
	    }
	
	    public List<Message> getMessagesEnvoyes(Long userId) {
	        return messageRepository.findByExpediteurIdOrderByDateEnvoiDesc(userId);
	    }
	
	    public void marquerCommeLu(Long messageId) {
	        Message message = messageRepository.findById(messageId)
	                .orElseThrow(() -> new RuntimeException("Message non trouvé"));
	        message.setLu(true);
	        messageRepository.save(message);
	    }
	    
	    public Message repondreMessage(Long expediteurId, Long destinataireId, String sujet, String contenu, Long parentId) {
	        if (expediteurId == null || destinataireId == null) {
	            throw new IllegalArgumentException("Expéditeur ou destinataire ne peuvent pas être nuls");
	        }
	
	        Utilisateur expediteur = utilisateurRepository.findById(expediteurId)
	                .orElseThrow(() -> new RuntimeException("Expéditeur non trouvé"));
	
	        Utilisateur destinataire = utilisateurRepository.findById(destinataireId)
	                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
	
	        Message parent = messageRepository.findById(parentId)
	                .orElseThrow(() -> new RuntimeException("Message parent non trouvé"));
	
	        Message message = new Message();
	        message.setExpediteur(expediteur);
	        message.setDestinataire(destinataire);
	        message.setSujet(sujet);
	        message.setContenu(contenu);
	        message.setDateEnvoi(LocalDateTime.now());
	        message.setMessageParent(parent);  
	
	        return messageRepository.save(message);
	    }
	    
	    
	
	}
