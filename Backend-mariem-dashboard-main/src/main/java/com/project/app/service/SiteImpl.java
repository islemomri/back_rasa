package com.project.app.service;

import java.util.List;

import com.project.app.model.Site;

public interface SiteImpl {
	 public Site ajouterSite(Site site);
	    public List<Site> getAllSites();
	 
	    public boolean deleteSite(Long id);
	    public Site archiverSite(Long id);
	    public List<Site> getAllSitesArchivés();
	    public List<Site> getAllSitesnonArchivés();
	    
	    public Site desarchiverSite(Long id) ;
	    
	   
	   
	    
	    
}
