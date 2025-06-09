package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.project.app.model.ConversationMessage;

public interface ConversationMessageRepository extends JpaRepository<ConversationMessage, Long>  {
	List<ConversationMessage> findBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
    @Query("SELECT DISTINCT c.sessionId FROM ConversationMessage c")
    List<String> findDistinctSessionIds();
}
