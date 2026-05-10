package com.examen_java.modele;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Avis {

    private int idAvis;
    private int idReservation;
    private int idMaison;
    private int idAuteur;
    private int noteProprete;
    private int noteCommunication;
    private int noteEmplacement;
    private int noteRapportQualitePrix;
    private double noteMoyenne;
    private String commentaire;
    private Timestamp datePublication;

    
    private String auteurNom;
    private String auteurPrenom;

    private String maisonTitre;
    public Avis() {}

    
    public int getIdAvis() { return idAvis; }
    public void setIdAvis(int idAvis) { this.idAvis = idAvis; }

    public int getIdReservation() { return idReservation; }
    public void setIdReservation(int idReservation) { this.idReservation = idReservation; }

    public int getIdMaison() { return idMaison; }
    public void setIdMaison(int idMaison) { this.idMaison = idMaison; }

    public int getIdAuteur() { return idAuteur; }
    public void setIdAuteur(int idAuteur) { this.idAuteur = idAuteur; }

    public int getNoteProprete() { return noteProprete; }
    public void setNoteProprete(int noteProprete) { this.noteProprete = noteProprete; }

    public int getNoteCommunication() { return noteCommunication; }
    public void setNoteCommunication(int noteCommunication) { this.noteCommunication = noteCommunication; }

    public int getNoteEmplacement() { return noteEmplacement; }
    public void setNoteEmplacement(int noteEmplacement) { this.noteEmplacement = noteEmplacement; }

    public int getNoteRapportQualitePrix() { return noteRapportQualitePrix; }
    public void setNoteRapportQualitePrix(int noteRapportQualitePrix) { this.noteRapportQualitePrix = noteRapportQualitePrix; }

    public double getNoteMoyenne() { return noteMoyenne; }
    public void setNoteMoyenne(double noteMoyenne) { this.noteMoyenne = noteMoyenne; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Timestamp getDatePublication() { return datePublication; }
    public void setDatePublication(Timestamp datePublication) { this.datePublication = datePublication; }

    public String getAuteurNom() { return auteurNom; }
    public void setAuteurNom(String auteurNom) { this.auteurNom = auteurNom; }

    public String getAuteurPrenom() { return auteurPrenom; }
    public void setAuteurPrenom(String auteurPrenom) { this.auteurPrenom = auteurPrenom; }

    public String getAuteurNomComplet() {
        if (auteurPrenom != null && auteurNom != null) return auteurPrenom + " " + auteurNom;
        if (auteurPrenom != null) return auteurPrenom;
        if (auteurNom != null) return auteurNom;
        return "Anonyme";
    }
    
    public String getMaisonTitre() { return maisonTitre; }
    public void setMaisonTitre(String maisonTitre) { this.maisonTitre = maisonTitre; }

    public String getDateFormatee() {
        if (datePublication == null) return "";
        return new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("fr", "FR")).format(datePublication);
    }
}


