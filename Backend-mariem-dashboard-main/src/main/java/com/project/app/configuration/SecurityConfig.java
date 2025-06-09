package com.project.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.app.filters.JWTAuthenticationFilter;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import com.project.app.dto.FormationDto;

import com.project.app.dto.PosteDTO;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JWTAuthenticationFilter jwtAuthenticationFilter;

   
	@Autowired
    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter) {
		
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    return http.csrf().disable()
	            .authorizeHttpRequests(authorize -> authorize
	            		.requestMatchers( "/stages/**").hasRole("RH")
	                    .requestMatchers("/api/sites/**").hasRole("RH")
	                    .requestMatchers("/api/pdf/**").permitAll()
	                    .requestMatchers( "/messages/**").authenticated()
	                    .requestMatchers("/diplomes/**","/typediplomes/**" ).hasRole("RH")
	                    .requestMatchers("/api/directions/**","/disciplines/**", "/api/experiences/**","/api/sites/**").authenticated()
	                    .requestMatchers("/login/**").permitAll()
	                    .requestMatchers("/api/chat/**").permitAll()
	                    .requestMatchers("/api/employes/**").permitAll()
	                    .requestMatchers("/notifications/**", "/competences_poste/**",  "/api/competences/**","/employecompetences/**", "/recrutement/postes").authenticated()
	                    .requestMatchers("/api/formation-poste/**","/api/entetes/**","/formations/**").authenticated()
	                    .requestMatchers("/permissions/**", "/signup/**","/utilisateurs/**").hasAnyRole("ADMIN","SUPER_ADMIN","RH","RESPONSABLE")
	                    .requestMatchers("/password-reset/**").permitAll()
	                    .requestMatchers("/api/emergency", "/api/reporting/employes").authenticated()
	                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
	                    .anyRequest().authenticated()
	            )
	            
	            .sessionManagement(session -> session
	                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            )
	            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	            .build();
	}

    
	@Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
 // Sans cette méthode, Swagger générerait une documentation standard, sans prise en compte de certains types de données spécifiques comme MultipartFile
    //@Bean public OpenAPI customOpenAPI() sert à configurer OpenAPI (Swagger) pour personnaliser la documentation de ton API REST.
   // Il définit explicitement PosteDTO et MultipartFile, permettant d’avoir une documentation claire et bien structurée dans Swagger UI.
   // Ça rend ton API plus compréhensible et testable
    @Bean
    

    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas("PosteDTO", new Schema<PosteDTO>()
                                .addProperty("titre", new StringSchema())
                                .addProperty("niveauExperience", new StringSchema())
                                .addProperty("diplomeRequis", new StringSchema())
                                .addProperty("competencesRequises", new StringSchema())
                                .addProperty("directionIds", new ArraySchema().items(new IntegerSchema())))
                        .addSchemas("FormationDto", new Schema<FormationDto>()
                                .addProperty("titre", new StringSchema())
                                .addProperty("description", new StringSchema())
                                .addProperty("typeFormation", new StringSchema())
                                .addProperty("sousTypeFormation", new StringSchema())
                                .addProperty("dateDebutPrevue", new StringSchema().format("date"))
                                .addProperty("dateFinPrevue", new StringSchema().format("date"))
                                .addProperty("responsableEvaluationId", new IntegerSchema())
                                .addProperty("responsableEvaluationExterne", new StringSchema())
                                .addProperty("employeIds", new ArraySchema().items(new IntegerSchema()))
                                .addProperty("organisateurId", new IntegerSchema()) // Nouveau champ
                                .addProperty("titrePoste", new StringSchema())) // Nouveau champ
                        .addSchemas("MultipartFile", new Schema<MultipartFile>()
                                .type("string")
                                .format("binary")));
    }

   
}