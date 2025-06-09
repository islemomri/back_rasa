package com.project.app.dto;

import lombok.Data;

@Data
public class RegisterDto {
	private String nom;
    private String prenom;
	private String email;
    private String password;
    private String role;
    private String username;
	
}
