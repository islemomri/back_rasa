package com.project.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.app.model.Entete;
import com.project.app.repository.EnteteRepository;

@Service
public class EnteteService {

    private final EnteteRepository enteteRepository;

    public EnteteService(EnteteRepository enteteRepository) {
        this.enteteRepository = enteteRepository;
    }

    public List<Entete> getAll() {
        return enteteRepository.findAll();
    }

    public Entete getById(Long id) {
        return enteteRepository.findById(id).orElse(null);
    }

    public Entete save(Entete entete) {
        return enteteRepository.save(entete);
    }

    public void delete(Long id) {
        enteteRepository.deleteById(id);
    }
}
