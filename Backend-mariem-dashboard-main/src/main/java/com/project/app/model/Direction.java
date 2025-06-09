	package com.project.app.model;



import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Direction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom_direction;    
    private boolean archive = false;

    public Direction() {
        this.archive = false;
    }

    public void archiver() {
        this.archive = true;
    }

    public void desarchiver() {
        this.archive = false;
    }
    
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
        name = "site_direction",
        joinColumns = @JoinColumn(name = "direction_id"),
        inverseJoinColumns = @JoinColumn(name = "site_id")
    )
    private Set<Site> sites; 
    
    
}
