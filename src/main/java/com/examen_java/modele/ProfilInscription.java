package com.examen_java.modele;

import java.sql.Timestamp;


public class ProfilInscription {

    
    public enum Role {
        LOCATAIRE("Locataire", "Peut réserver des maisons"),
        PROPRIETAIRE("Propriétaire", "Peut publier des annonces"),
        ADMINISTRATEUR("Administrateur", "Accès complet à l'administration");

        private final String libelle;
        private final String description;

        Role(String libelle, String description) {
            this.libelle = libelle;
            this.description = description;
        }

        public String getLibelle() {
            return libelle;
        }

        public String getDescription() {
            return description;
        }

        
        public static Role fromString(String valeur) {
            if (valeur == null) {
                throw new IllegalArgumentException("Le rôle ne peut pas être null");
            }
            for (Role role : values()) {
                if (role.name().equalsIgnoreCase(valeur.trim())) {
                    return role;
                }
            }
            throw new IllegalArgumentException("Rôle invalide : " + valeur
                    + ". Valeurs possibles : LOCATAIRE, PROPRIETAIRE");
        }
    }

    
    private int idProfil;

  
    private int idUtilisateur;

    
    private Role role;

  
    private Timestamp dateAttribution;

  
    private boolean estVerifie;

  
    public ProfilInscription() {
        this.estVerifie = false;
    }

    
    public ProfilInscription(int idUtilisateur, Role role) {
        this();
        this.idUtilisateur = idUtilisateur;
        this.role = role;
    }

    
    public ProfilInscription(int idUtilisateur, String roleString) {
        this(idUtilisateur, Role.fromString(roleString));
    }

   

    public int getIdProfil() {
        return idProfil;
    }

    public void setIdProfil(int idProfil) {
        this.idProfil = idProfil;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    
    public void setRoleString(String roleString) {
        this.role = Role.fromString(roleString);
    }

    
    public String getRoleString() {
        return this.role != null ? this.role.name() : null;
    }

    public Timestamp getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(Timestamp dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    public boolean isEstVerifie() {
        return estVerifie;
    }

    public void setEstVerifie(boolean estVerifie) {
        this.estVerifie = estVerifie;
    }

    

    
    public boolean estProprietaire() {
        return this.role == Role.PROPRIETAIRE;
    }

    
    public boolean estLocataire() {
        return this.role == Role.LOCATAIRE;
    }

    @Override
    public String toString() {
        return "ProfilInscription{"
                + "id=" + idProfil
                + ", idUtilisateur=" + idUtilisateur
                + ", role=" + (role != null ? role.getLibelle() : "null")
                + ", estVerifie=" + estVerifie
                + '}';
    }
}