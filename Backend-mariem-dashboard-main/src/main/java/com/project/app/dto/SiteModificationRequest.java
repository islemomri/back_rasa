package com.project.app.dto;

public class SiteModificationRequest {
    private boolean archiver;
    private boolean desarchiver;

    // Constructeurs, getters, et setters

    public boolean isArchiver() {
        return archiver;
    }

    public void setArchiver(boolean archiver) {
        this.archiver = archiver;
    }

    public boolean isDesarchiver() {
        return desarchiver;
    }

    public void setDesarchiver(boolean desarchiver) {
        this.desarchiver = desarchiver;
    }
}
