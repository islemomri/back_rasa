package com.project.app.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.app.model.Site;
import com.project.app.repository.DirectionRepository;
import com.project.app.repository.PosteRepository;
import com.project.app.repository.SiteRepository;


@Service
public class SiteService implements SiteImpl {
	@Autowired
    private SiteRepository siteRepository;
	@Autowired
    private DirectionRepository directionRepository;

	@Autowired
    private PosteRepository posteRepository;

	@Override
	public Site ajouterSite(Site site) {
		 return siteRepository.save(site);
	}


	@Override
	public List<Site> getAllSites() {
		return siteRepository.findAll();
	}
	@Override
	public boolean deleteSite(Long id) {
		 Optional<Site> siteOpt = siteRepository.findById(id);
	        if (siteOpt.isPresent()) {
	            siteRepository.deleteById(id);
	            return true;
	        } else {
	            return false;
	        }
	}


	@Override
	public Site archiverSite(Long id) {
		 Site site = siteRepository.findById(id).orElseThrow(() -> new RuntimeException("Site not found"));
	        site.archiver(); 
	        return siteRepository.save(site);
	}


	@Override
	public List<Site> getAllSitesArchivés() {
		return siteRepository.findByArchiveTrue();
	}


	@Override
	public List<Site> getAllSitesnonArchivés() {
		return siteRepository.findByArchiveFalse();
	}


	@Override
	public Site desarchiverSite(Long id) {
		Site site = siteRepository.findById(id).orElseThrow(() -> new RuntimeException("site non trouvée"));
    site.desarchiver();
    return siteRepository.save(site);
	}
	
	

}
