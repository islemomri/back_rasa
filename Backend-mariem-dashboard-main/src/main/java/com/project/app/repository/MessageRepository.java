package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByDestinataireIdOrderByDateEnvoiDesc(Long destinataireId);
    List<Message> findByExpediteurIdOrderByDateEnvoiDesc(Long expediteurId);
}
