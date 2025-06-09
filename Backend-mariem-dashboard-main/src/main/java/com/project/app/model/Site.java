package com.project.app.model;



import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom_site;    
    private boolean archive = false;



    public Site() {
        this.archive = false;
    }

    public void archiver() {
        this.archive = true;
    }

    public void desarchiver() {
        this.archive = false;
    }
}
