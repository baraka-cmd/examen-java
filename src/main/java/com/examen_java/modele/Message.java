package com.examen_java.modele;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Message {

    
    private int idMessage;

    
    private int idReservation;


    private int idExpediteur;

    
    private String contenu;

    
    private Timestamp dateEnvoi;

    
    private boolean estLu;

    
    private String nomExpediteur;

   
    private String prenomExpediteur;

   
    private String maisonTitre;

  
    private String locataireNom;

  
    private String locatairePrenom;

 
    private String proprietaireNom;

  
    private String proprietairePrenom;

    
    public Message() {
        this.estLu = false;
    }

    
    public Message(int idReservation, int idExpediteur, String contenu) {
        this();
        this.idReservation = idReservation;
        this.idExpediteur = idExpediteur;
        this.contenu = contenu;
    }

    

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdExpediteur() {
        return idExpediteur;
    }

    public void setIdExpediteur(int idExpediteur) {
        this.idExpediteur = idExpediteur;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = (contenu != null) ? contenu.trim() : null;
    }

    public Timestamp getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Timestamp dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public boolean isEstLu() {
        return estLu;
    }

    public void setEstLu(boolean estLu) {
        this.estLu = estLu;
    }

    

    public String getNomExpediteur() {
        return nomExpediteur;
    }

    public void setNomExpediteur(String nomExpediteur) {
        this.nomExpediteur = nomExpediteur;
    }

    public String getPrenomExpediteur() {
        return prenomExpediteur;
    }

    public void setPrenomExpediteur(String prenomExpediteur) {
        this.prenomExpediteur = prenomExpediteur;
    }

    public String getMaisonTitre() {
        return maisonTitre;
    }

    public void setMaisonTitre(String maisonTitre) {
        this.maisonTitre = maisonTitre;
    }

    public String getLocataireNom() {
        return locataireNom;
    }

    public void setLocataireNom(String locataireNom) {
        this.locataireNom = locataireNom;
    }

    public String getLocatairePrenom() {
        return locatairePrenom;
    }

    public void setLocatairePrenom(String locatairePrenom) {
        this.locatairePrenom = locatairePrenom;
    }

    public String getProprietaireNom() {
        return proprietaireNom;
    }

    public void setProprietaireNom(String proprietaireNom) {
        this.proprietaireNom = proprietaireNom;
    }

    public String getProprietairePrenom() {
        return proprietairePrenom;
    }

    public void setProprietairePrenom(String proprietairePrenom) {
        this.proprietairePrenom = proprietairePrenom;
    }

    
    public String getNomCompletExpediteur() {
        StringBuilder sb = new StringBuilder();
        if (prenomExpediteur != null && !prenomExpediteur.isEmpty()) {
            sb.append(prenomExpediteur);
        }
        if (nomExpediteur != null && !nomExpediteur.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(nomExpediteur);
        }
        return sb.length() > 0 ? sb.toString() : "Utilisateur";
    }

    
    public String getDateEnvoiFormatee() {
        if (dateEnvoi == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm").format(dateEnvoi);
    }

   
    public String getHeureEnvoi() {
        if (dateEnvoi == null) return "";
        return new SimpleDateFormat("HH:mm").format(dateEnvoi);
    }

    
    public String getApercu() {
        if (contenu == null || contenu.isEmpty()) return "";
        if (contenu.length() <= 50) return contenu;
        return contenu.substring(0, 47) + "...";
    }

    
    public boolean aDuContenu() {
        return contenu != null && !contenu.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + idMessage +
                ", reservation=" + idReservation +
                ", expediteur=" + idExpediteur +
                ", estLu=" + estLu +
                ", apercu='" + getApercu() + '\'' +
                '}';
    }
}