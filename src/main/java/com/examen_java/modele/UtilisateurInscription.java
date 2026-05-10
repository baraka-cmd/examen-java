package com.examen_java.modele;

import java.sql.Timestamp;


public class UtilisateurInscription {

    
    private int idUtilisateur;

    
    private String email;

    
    private String motDePasseHash;


    private String nom;

  
    private String prenom;

    
    private String telephone;

  
    private Timestamp dateInscription;

    
    private boolean emailVerifie;

    
    private boolean estActif;

    
    public UtilisateurInscription() {
        this.emailVerifie = false;
        this.estActif = true;
    }

   
    public UtilisateurInscription(String email, String motDePasseHash, String nom, String prenom) {
        this();
        this.email = email;
        this.motDePasseHash = motDePasseHash;
        this.nom = nom;
        this.prenom = prenom;
    }

    

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = (email != null) ? email.toLowerCase().trim() : null;
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = (nom != null) ? nom.trim() : null;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = (prenom != null) ? prenom.trim() : null;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = (telephone != null) ? telephone.trim() : null;
    }

    public Timestamp getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Timestamp dateInscription) {
        this.dateInscription = dateInscription;
    }

    public boolean isEmailVerifie() {
        return emailVerifie;
    }

    public void setEmailVerifie(boolean emailVerifie) {
        this.emailVerifie = emailVerifie;
    }

    public boolean isEstActif() {
        return estActif;
    }

    public void setEstActif(boolean estActif) {
        this.estActif = estActif;
    }

    
    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    @Override
    public String toString() {
        return "UtilisateurInscription{"
                + "id=" + idUtilisateur
                + ", email='" + email + '\''
                + ", nomComplet='" + getNomComplet() + '\''
                + ", estActif=" + estActif
                + '}';
    }
}