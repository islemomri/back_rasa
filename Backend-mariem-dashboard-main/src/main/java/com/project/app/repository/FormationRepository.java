package com.project.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.app.model.Formation;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    List<Formation> findByOrganisateurId(Long rhId);
    List<Formation> findByResponsableEvaluationId(Long responsableId);
    List<Formation> findByEmployesId(Long employeId);
    List<Formation> findByOrganisateur_Id(Long rhId);
    @Query("SELECT f FROM Formation f")
    List<Formation> findAllFormations();

    @Query("SELECT f FROM Formation f WHERE f.responsableEvaluation.id = :responsableId " +
            "AND f.sousTypeFormation IN ('INTEGRATION', 'POLYVALENCE')")
     List<Formation> findFormationsFiltrees(@Param("responsableId") Long responsableId);
    @Query("SELECT f FROM Formation f WHERE f.rappelEnvoye = false OR f.rappelEnvoye IS NULL")
    List<Formation> findAllFormationsWhereRappelNotSentOrNull();
    List<Formation> findByValideFalseAndDateFinPrevueBefore(LocalDate date);
    List<Formation> findByValideTrueAndConfirmationOrganisateurEnvoyeeFalse();
    List<Formation> findByValideFalseAndProblemeTrue();

}