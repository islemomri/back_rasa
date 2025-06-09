package com.project.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.model.ConversationMessage;
import com.project.app.service.ChatHistoryService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatHistoryService chatHistoryService;

    public ChatController(ChatHistoryService chatHistoryService) {
        this.chatHistoryService = chatHistoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveMessage(@RequestBody Map<String, String> payload) {
        String sender = payload.get("sender");
        String message = payload.get("message");
        String sessionId = payload.get("sessionId"); 
        chatHistoryService.saveMessage(sender, message, sessionId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/conversations")
    public ResponseEntity<List<String>> getAllConversations() {
        return ResponseEntity.ok(chatHistoryService.getAllConversations());
    }

    @GetMapping("/conversations/{sessionId}")
    public ResponseEntity<List<ConversationMessage>> getConversation(@PathVariable String sessionId) {
        return ResponseEntity.ok(chatHistoryService.getMessagesBySessionId(sessionId));
    }

    @DeleteMapping("/conversations/{sessionId}")
    public ResponseEntity<?> deleteConversation(@PathVariable String sessionId) {
        chatHistoryService.deleteConversation(sessionId);
        return ResponseEntity.ok().build();
    }
}