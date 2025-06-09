package com.project.app.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.app.dto.AdminRegisterDto;
import com.project.app.dto.DirecteurRegisterDTO;
import com.project.app.dto.RHRegisterDTO;
import com.project.app.dto.RegisterDto;
import com.project.app.dto.ResponsableRegisterDTO;
import com.project.app.model.Administrateur;
import com.project.app.model.Directeur;
import com.project.app.model.PasswordResetToken;
import com.project.app.model.RH;
import com.project.app.model.Responsable;
import com.project.app.model.SuperAdmin;
import com.project.app.model.Utilisateur;
import com.project.app.repository.PasswordResetTokenRepository;
import com.project.app.repository.UtilisateurRepository;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@Service
public class AuthServiceImpl implements AuthService{
	
	private final UtilisateurRepository utilisateurRepository;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
    public AuthServiceImpl(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        
    }

	@Override
	@Transactional
	public Utilisateur createUser(RegisterDto registerDto) {
	    if (utilisateurRepository.existsByUsername(registerDto.getUsername())) {
	        return null;
	    }
	    Utilisateur utilisateur;

	    if (registerDto instanceof AdminRegisterDto) {
	        utilisateur = createUserFromAdminDto((AdminRegisterDto) registerDto);
	    } else if (registerDto instanceof RHRegisterDTO) {
	        utilisateur = createUserFromRHDto((RHRegisterDTO) registerDto);
	    } else if (registerDto instanceof DirecteurRegisterDTO) {
	        utilisateur = createUserFromDirecteurDto((DirecteurRegisterDTO) registerDto);
	    } else if (registerDto instanceof ResponsableRegisterDTO) {
	        utilisateur = createUserFromResponsableDto((ResponsableRegisterDTO) registerDto);
	    } else {
	        throw new IllegalArgumentException("Invalid RegisterDto type");
	    }

	    utilisateurRepository.save(utilisateur);
	    return utilisateur;
	}


	
	@Transactional
	private Utilisateur createUserFromAdminDto(AdminRegisterDto Adto) {
		
		Administrateur administrateur = new Administrateur();
		administrateur.setUsername(Adto.getUsername());
		administrateur.setNom(Adto.getNom());
		administrateur.setPrenom(Adto.getPrenom());
		administrateur.setEmail(Adto.getEmail());
		administrateur.setPassword(passwordEncoder.encode(Adto.getPassword()));
		administrateur.setRole(Adto.getRole());
		Utilisateur utilisateur = utilisateurRepository.save(administrateur);
		administrateur.setId(utilisateur.getId());
		return entityManager.merge(administrateur);
	}
	
	@Transactional
	private Utilisateur createUserFromDirecteurDto(DirecteurRegisterDTO Ddto) {
	    Directeur directeur = new Directeur();
	    directeur.setNom(Ddto.getNom());
	    directeur.setUsername(Ddto.getUsername());
	    directeur.setPrenom(Ddto.getPrenom());
	    directeur.setEmail(Ddto.getEmail());
	    directeur.setPassword(passwordEncoder.encode(Ddto.getPassword()));
	    directeur.setRole(Ddto.getRole());
	    Utilisateur utilisateur = utilisateurRepository.save(directeur);
	    directeur.setId(utilisateur.getId());
	    return entityManager.merge(directeur);
	}
	
	@Transactional
	private Utilisateur createUserFromRHDto(RHRegisterDTO Rdto) {
	    RH rh = new RH();
	    rh.setUsername(Rdto.getUsername());
	    rh.setNom(Rdto.getNom());
	    rh.setPrenom(Rdto.getPrenom());
	    rh.setEmail(Rdto.getEmail());
	    rh.setPassword(passwordEncoder.encode(Rdto.getPassword()));
	    rh.setRole(Rdto.getRole());
	    Utilisateur utilisateur = utilisateurRepository.save(rh);
	    rh.setId(utilisateur.getId());
	    return entityManager.merge(rh);
	}
	
	@Transactional
	private Utilisateur createUserFromResponsableDto(ResponsableRegisterDTO RDto) {
	    Responsable responsable = new Responsable();
	    responsable.setUsername(RDto.getUsername());
	    responsable.setNom(RDto.getNom());
	    responsable.setPrenom(RDto.getPrenom());
	    responsable.setEmail(RDto.getEmail());
	    responsable.setPassword(passwordEncoder.encode(RDto.getPassword()));
	    responsable.setRole(RDto.getRole());
	    Utilisateur utilisateur = utilisateurRepository.save(responsable);
	    responsable.setId(utilisateur.getId());
	    return entityManager.merge(responsable);
	}
	
	private String generateRandomPassword(String nom) {
	    SecureRandom random = new SecureRandom();
	    int randomNumber = 100 + random.nextInt(900); 
	    return nom + randomNumber;
	}
	
	@Transactional
	public String resetPassword(Long userId) {
	    return utilisateurRepository.findById(userId)
	            .map(utilisateur -> {
	                // Empêcher la réinitialisation du mot de passe du Super Admin
	                if (utilisateur instanceof SuperAdmin) {
	                    throw new AccessDeniedException("Le mot de passe du Super Admin ne peut pas être réinitialisé");
	                }
	                String newPassword = generateRandomPassword(utilisateur.getNom());
	                utilisateur.setPassword(passwordEncoder.encode(newPassword));
	                utilisateurRepository.save(utilisateur);

	                // Vérifier si l'utilisateur a une adresse e-mail avant d'envoyer
	                if (utilisateur.getEmail() != null && !utilisateur.getEmail().isEmpty()) {
	                    try {
	                        String emailBody = "<p>Bonjour " + utilisateur.getNom() + " " + utilisateur.getPrenom() + ",</p>"
	                                + "<p>Votre nouveau mot de passe est : <strong>" + newPassword + "</strong></p>"
	                                + "<p>Merci de le modifier après votre connexion.</p>";

	                        emailService.sendEmail(utilisateur.getEmail(), "Réinitialisation de mot de passe", emailBody);
	                    } catch (MessagingException e) {
	                        throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
	                    }
	                }

	                return newPassword;
	            })
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	}
	
	
	@Override
	@Transactional
	public Utilisateur incrementFailedLoginAttempts(String username) {
	    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	    
	    if (utilisateur instanceof SuperAdmin) {
	        return utilisateur; 
	    }
	    
	    int newAttempts = utilisateur.getFailedLoginAttempts() + 1;
	    utilisateur.setFailedLoginAttempts(newAttempts);
	    
	    if (newAttempts >= 3) {
	        utilisateur.setAccountLocked(true);
	        utilisateur.setLockTime(LocalDateTime.now());
	    }
	    
	    utilisateurRepository.save(utilisateur);
	    return utilisateur;
	}
	
	@Transactional
	public void resetFailedLoginAttempts(String username) {
	    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	    
	    utilisateur.setFailedLoginAttempts(0);
	    utilisateur.setAccountLocked(false);
	    utilisateur.setLockTime(null);
	    utilisateurRepository.save(utilisateur);
	}

	
	@Transactional
	public boolean isAccountLocked(String username) {
	    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	    
	    if (utilisateur instanceof SuperAdmin) {
	        return false; // Le compte Super Admin ne peut pas être verrouillé
	    }
	    
	    Boolean locked = utilisateur.getAccountLocked();
	    
	    // Par défaut, on considère que le compte n'est pas verrouillé si null
	    if (Boolean.TRUE.equals(locked)) {
	        // Déverrouiller automatiquement après 30 minutes
	        if (utilisateur.getLockTime() != null &&
	            utilisateur.getLockTime().plusMinutes(30).isBefore(LocalDateTime.now())) {
	            resetFailedLoginAttempts(username);
	            return false;
	        }
	        return true;
	    }
	    
	    return false;
	}

	
	
	@Transactional
	public int getFailedAttempts(String username) {
	    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	    return utilisateur.getFailedLoginAttempts();
	}
	
	@Transactional
	public void lockAccount(String username) {
	    Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
	    
	    utilisateur.setAccountLocked(true);
	    utilisateur.setLockTime(LocalDateTime.now());
	    utilisateurRepository.save(utilisateur);
	}
	
	@Transactional
	public void unlockAccount(String username) {
	    this.resetFailedLoginAttempts(username); // Réutilise la méthode existante
	}
	

	



}