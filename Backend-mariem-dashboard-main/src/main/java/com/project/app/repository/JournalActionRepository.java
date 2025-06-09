package com.project.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.app.model.JournalAction;

public interface JournalActionRepository extends JpaRepository<JournalAction, Long> {
    List<JournalAction> findByUtilisateurIdOrderByTimestampDesc(Long utilisateurId);
    List<JournalAction> findAllByOrderByTimestampDesc();
    List<JournalAction> findByUtilisateurIdAndTimestampAfterOrderByTimestampDesc(Long utilisateurId, LocalDateTime since);
    

}

