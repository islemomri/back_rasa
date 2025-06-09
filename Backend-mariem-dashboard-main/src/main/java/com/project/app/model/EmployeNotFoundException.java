package com.project.app.model;

public class EmployeNotFoundException extends RuntimeException {
    public EmployeNotFoundException(Integer matricule) {
        super("Aucun employé trouvé avec le matricule " + matricule);
    }
}

