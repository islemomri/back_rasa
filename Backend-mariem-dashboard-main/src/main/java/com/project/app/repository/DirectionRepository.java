package com.project.app.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.app.model.Direction;

@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {
	List<Direction> findByArchiveFalse(); 
	List<Direction> findByArchiveTrue();

}
