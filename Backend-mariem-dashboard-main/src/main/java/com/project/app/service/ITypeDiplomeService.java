package com.project.app.service;


import java.util.List;

import com.project.app.model.TypeDiplome;

public interface ITypeDiplomeService {
	
	/*public TypeDiplome saveTypeDiplome(String libelleTypeDiplome, Long id);*/
	public TypeDiplome archiverTypeDiplome(Long id);
	public List<TypeDiplome> getAllTypeDiplomenonArchives();
	public List<TypeDiplome> getAllTypeDiplomeArchives() ;
	public TypeDiplome desarchiverTypeDiplome(Long id);

}
