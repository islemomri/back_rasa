package com.project.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.project.app.dto.PosteDTO;
import com.project.app.model.Direction;
import com.project.app.model.Poste;

public interface IPosteService {
	
	public Poste ajouterPoste(Poste poste);
	public List<Poste> getAllPostes();
	public Poste getPosteById(Long id);
	 public Poste updatePoste(Long id, PosteDTO posteDto)throws IOException;
	public void deletePoste(Long id);
	 public List<Poste> getAllPostesnonArchivés();
	 public Poste archiverPoste(Long id);
	 public List<Poste> getAllPostesArchivés();
	 public Poste desarchiverPoste(Long id);
	
	 public Poste addPosteWithDirections(PosteDTO posteDTO)throws IOException ;
	 public Optional<Set<Direction>> getDirectionsByPosteId(Long id);
	 
	
	

}