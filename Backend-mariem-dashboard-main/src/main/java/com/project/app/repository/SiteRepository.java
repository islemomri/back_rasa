package com.project.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.project.app.model.Site;



@Repository
public interface SiteRepository extends JpaRepository<Site, Long> {
	List<Site> findByArchiveFalse(); 
	List<Site> findByArchiveTrue();
}
