package com.project.app.repository;

import com.project.app.model.Diplome;
import com.project.app.model.TypeDiplome;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeDiplomeRepository extends JpaRepository<TypeDiplome, Long> {
    Optional<TypeDiplome> findByLibelleTypeDiplome(String libelleTypeDiplome);
    boolean existsByLibelleTypeDiplome(String libelleTypeDiplome);
    List<TypeDiplome> findByArchiveFalse();
	List<TypeDiplome> findByArchiveTrue();
    

}
