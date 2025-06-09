package com.project.app.service;

import com.project.app.dto.RegisterDto;
import com.project.app.model.Utilisateur;

import jakarta.transaction.Transactional;

public interface AuthService {
	Utilisateur createUser(RegisterDto registerDto);
	@Transactional
	Utilisateur incrementFailedLoginAttempts(String username);

}
