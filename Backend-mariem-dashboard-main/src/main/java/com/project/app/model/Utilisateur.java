package com.project.app.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

		@Entity
		@Data
		@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
		public class Utilisateur {
			@Id
			@GeneratedValue(strategy = GenerationType.TABLE)
			private Long id;
			private String username;
			private String nom;
		    private String prenom;
		    private String email;
		    private String password;
		    private String role;
		    
		    public Utilisateur(String username) {
		    	this.username=username;
		    }
		    
		    public Utilisateur() {
		        this.accountLocked = false;
		    }
	
		    
		    private String telephone;
		    @ManyToMany(fetch = FetchType.EAGER)
		    @JoinTable(
		        name = "utilisateur_permissions",
		        joinColumns = @JoinColumn(name = "utilisateur_id"),
		        inverseJoinColumns = @JoinColumn(name = "permission_id")
		    )
		    private Set<Permission> permissions = new HashSet<>();
			
		    @Column(name = "last_login")
		    private LocalDateTime lastLogin;
		    
		    @Column(name = "failed_login_attempts", columnDefinition = "int default 0")
		    private int failedLoginAttempts = 0;
	
		    @Column(name = "account_locked")
		    private Boolean accountLocked = false;
	
		    @Column(name = "lock_time")
		    private LocalDateTime lockTime;
		    
		    
		    
		
		}
