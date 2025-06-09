package com.project.app.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.app.dto.LoginDto;
import com.project.app.dto.LoginResponse;
import com.project.app.dto.UtilisateurResponseDto;
import com.project.app.model.JournalAction;
import com.project.app.model.SuperAdmin;
import com.project.app.model.Utilisateur;
import com.project.app.repository.JournalActionRepository;
import com.project.app.repository.UtilisateurRepository;
import com.project.app.service.AuthServiceImpl;
import com.project.app.service.JournalActionService;
import com.project.app.service.UtilisateurUserDetails;
import com.project.app.service.UtilisateurUserDetailsService;
import com.project.app.utils.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/login")
public class LoginController {
	
	private final AuthenticationManager authenticationManager;
    private final UtilisateurUserDetailsService utilisateurUserDetailsService;
    private final JwtUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository; // Ajouté
    
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private JournalActionRepository journalActionRepository;
    
    
    @Autowired
    private JournalActionService journalActionService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager,
                         UtilisateurUserDetailsService utilisateurUserDetailsService,
                         JwtUtil jwtUtil,
                         UtilisateurRepository utilisateurRepository) {
        this.authenticationManager = authenticationManager;
        this.utilisateurUserDetailsService = utilisateurUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.utilisateurRepository = utilisateurRepository;
    }

    
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        try {
        	
        	if (authService.isAccountLocked(loginRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Compte temporairement verrouillé. Veuillez réessayer plus tard.");
            }
            // Authentification utilisateur par username
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            
         // Réinitialiser les tentatives échouées après une connexion réussie
            authService.resetFailedLoginAttempts(loginRequest.getUsername());
            
         // Mettre à jour lastLogin après authentification réussie
            Utilisateur utilisateur = utilisateurRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
                
                utilisateur.setLastLogin(LocalDateTime.now());
                utilisateurRepository.save(utilisateur);
                
                
                journalActionService.logAction(utilisateur, "Login", "Connexion réussie");
                
        } catch (BadCredentialsException e) {
        	 try {
                 Utilisateur utilisateur = authService.incrementFailedLoginAttempts(loginRequest.getUsername());
                 // Journaliser l'échec de connexion
                 journalActionService.logAction(
                     utilisateur, 
                     "TENTATIVE_CONNEXION_ECHOUEE", 
                     "Échec de la connexion - Tentative " + utilisateur.getFailedLoginAttempts()
                 );

                 if (authService.isAccountLocked(loginRequest.getUsername())) {
                     return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("Trop de tentatives échouées. Compte verrouillé pendant 30 minutes.");
                 }
                 if (utilisateur instanceof SuperAdmin) {
                     return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("Identifiant ou mot de passe incorrect.");
                 }
                 
                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                         .body("Identifiant ou mot de passe incorrect. Tentatives restantes: " + 
                              (3 - utilisateur.getFailedLoginAttempts()));
             } catch (UsernameNotFoundException ex) {
                 // Cas où l'utilisateur n'existe pas
                 return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                         .body("Identifiant ou mot de passe incorrect.");
             }
        }
        
        // Charger les détails de l'utilisateur
        UserDetails userDetails = utilisateurUserDetailsService.loadUserByUsername(loginRequest.getUsername());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String role = "ROLE_USER"; // Défaut
        Long utilisateurId = null;

        // Vérification et définition du rôle
        if (userDetails instanceof UtilisateurUserDetails) {
            utilisateurId = ((UtilisateurUserDetails) userDetails).getId();
        }

        for (GrantedAuthority authority : authorities) {
        	
        	if (authority.getAuthority().equals("ROLE_SUPER_ADMIN")) {
        	    role = "SUPER_ADMIN";
        	    break;
            }else if(authority.getAuthority().equals("ROLE_ADMIN")) {
                role = "ADMIN";
                break;
            } else if (authority.getAuthority().equals("ROLE_DIRECTEUR")) {
                role = "DIRECTEUR";
                break;
            } else if (authority.getAuthority().equals("ROLE_RH")) {
                role = "RH";
                break;
            } else if (authority.getAuthority().equals("ROLE_RESPONSABLE")) {
                role = "RESPONSABLE";
                break;
            }
        }


        // Générer un token JWT
        String jwt = jwtUtil.generateToken(userDetails.getUsername(), authorities);

        // Construire la réponse
        LoginResponse response = new LoginResponse(jwt, role, utilisateurId);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/utilisateurs/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable ("id") Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'id : " + id));
        return ResponseEntity.ok(utilisateur);
    }
    
    


}
