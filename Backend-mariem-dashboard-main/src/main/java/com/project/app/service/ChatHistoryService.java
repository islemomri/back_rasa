package com.project.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.app.model.ConversationMessage;
import com.project.app.repository.ConversationMessageRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatHistoryService {

    private final ConversationMessageRepository repository;

    public ChatHistoryService(ConversationMessageRepository repository) {
        this.repository = repository;
    }

    public void saveMessage(String sender, String message, String sessionId) {
        ConversationMessage msg = new ConversationMessage();
        msg.setSender(sender);
        msg.setMessage(message);
        msg.setTimestamp(LocalDateTime.now());
        msg.setSessionId(sessionId);
        repository.save(msg);
    }
    public List<String> getAllConversations() {
        return repository.findDistinctSessionIds();
    }

    public List<ConversationMessage> getMessagesBySessionId(String sessionId) {
        return repository.findBySessionId(sessionId);
    }

    @Transactional
    public void deleteConversation(String sessionId) {
    	repository.deleteBySessionId(sessionId);
    }
    
    
    
    
    
    
}