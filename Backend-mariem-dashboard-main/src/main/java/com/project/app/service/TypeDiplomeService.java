package com.project.app.service;


import com.project.app.model.TypeDiplome;
import com.project.app.repository.TypeDiplomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeDiplomeService implements ITypeDiplomeService{
    
    @Autowired
    private TypeDiplomeRepository typeDiplomeRepository;

    public TypeDiplome saveTypeDiplome(String libelleTypeDiplome) {
        
        Optional<TypeDiplome> existingTypeDiplome = typeDiplomeRepository.findByLibelleTypeDiplome(libelleTypeDiplome);

        if (existingTypeDiplome.isPresent()) {
            throw new RuntimeException("Ce type de diplôme existe déjà !");
        }

        
        TypeDiplome typeDiplome = new TypeDiplome();
        typeDiplome.setLibelleTypeDiplome(libelleTypeDiplome);
        return typeDiplomeRepository.save(typeDiplome);
    }


    
    public List<TypeDiplome> getAllTypeDiplomes() {
        return typeDiplomeRepository.findAll();
    }
    
    public TypeDiplome updateTypeDiplome(Long id, String newLibelle) {
        Optional<TypeDiplome> existingTypeDiplome = typeDiplomeRepository.findById(id);

        if (existingTypeDiplome.isPresent()) {
            TypeDiplome typeDiplome = existingTypeDiplome.get();
            typeDiplome.setLibelleTypeDiplome(newLibelle);
            return typeDiplomeRepository.save(typeDiplome);
        } else {
            throw new RuntimeException("TypeDiplome non trouvé avec ID: " + id);
        }
    }

    
    public Optional<TypeDiplome> getTypeDiplomeById(Long id) {
        return typeDiplomeRepository.findById(id);
    }
    
    public void deleteTypeDiplome(Long id) {
        if (typeDiplomeRepository.existsById(id)) {
            typeDiplomeRepository.deleteById(id);
        } else {
            throw new RuntimeException("TypeDiplome non trouvé avec ID: " + id);
        }
    }
    @Override
    public TypeDiplome archiverTypeDiplome(Long id) {
    	TypeDiplome TypeDiplome = typeDiplomeRepository.findById(id).orElseThrow(()->new RuntimeException("Diplome n'existe pas"));
    	TypeDiplome.archiver();
        return typeDiplomeRepository.save(TypeDiplome);
    }



	@Override
	public List<TypeDiplome> getAllTypeDiplomenonArchives() {
		return typeDiplomeRepository.findByArchiveFalse();
	}
	
	@Override
	public List<TypeDiplome> getAllTypeDiplomeArchives() {
		return typeDiplomeRepository.findByArchiveTrue();
	}



	@Override
	public TypeDiplome desarchiverTypeDiplome(Long id) {
		TypeDiplome TypeDiplome = typeDiplomeRepository.findById(id).orElseThrow(()->new RuntimeException("Diplome n'existe pas"));
    	TypeDiplome.desarchiver();
        return typeDiplomeRepository.save(TypeDiplome);
	}
    
    
    
}
