package com.examen_java.modele;

import java.sql.Timestamp;
import java.util.List;


public class Maison {

    
    private int idMaison;
    private int idProprietaire;
    private String titre;
    private String description;
    private double prixBaseNuit;
    private int capacitePersonnes;
    private int nombreChambres;
    private int nombreSallesBain;
    private int superficieMetresCarres;
    private String adresseLigne1;
    private String adresseLigne2;
    private String ville;
    private String codePostal;
    private String pays;
    private String equipements;
    private String reglementInterieur;
    private boolean estVisible;
    private Timestamp dateCreation;
    private Timestamp dateModification;

    
    private List<MediaMaison> medias;
    private MediaMaison photoPrincipale;
    private String nomProprietaire;

    

    public Maison() {
        this.estVisible = true;
        this.pays = "France";
        this.prixBaseNuit = 0.0;
        this.capacitePersonnes = 1;
        this.nombreChambres = 1;
        this.nombreSallesBain = 1;
    }

    
    public Maison(int idProprietaire, String titre, String ville, double prixBaseNuit, int capacitePersonnes) {
        this();
        this.idProprietaire = idProprietaire;
        this.titre = titre;
        this.ville = ville;
        this.prixBaseNuit = prixBaseNuit;
        this.capacitePersonnes = capacitePersonnes;
    }

    

    public int getIdMaison() {
        return idMaison;
    }

    public void setIdMaison(int idMaison) {
        this.idMaison = idMaison;
    }

    public int getIdProprietaire() {
        return idProprietaire;
    }

    public void setIdProprietaire(int idProprietaire) {
        this.idProprietaire = idProprietaire;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = (titre != null) ? titre.trim() : null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = (description != null) ? description.trim() : null;
    }

    public double getPrixBaseNuit() {
        return prixBaseNuit;
    }

    public void setPrixBaseNuit(double prixBaseNuit) {
        this.prixBaseNuit = (prixBaseNuit > 0) ? prixBaseNuit : 0.0;
    }

    public int getCapacitePersonnes() {
        return capacitePersonnes;
    }

    public void setCapacitePersonnes(int capacitePersonnes) {
        this.capacitePersonnes = (capacitePersonnes > 0) ? capacitePersonnes : 1;
    }

    public int getNombreChambres() {
        return nombreChambres;
    }

    public void setNombreChambres(int nombreChambres) {
        this.nombreChambres = (nombreChambres >= 0) ? nombreChambres : 0;
    }

    public int getNombreSallesBain() {
        return nombreSallesBain;
    }

    public void setNombreSallesBain(int nombreSallesBain) {
        this.nombreSallesBain = (nombreSallesBain >= 0) ? nombreSallesBain : 0;
    }

    public int getSuperficieMetresCarres() {
        return superficieMetresCarres;
    }

    public void setSuperficieMetresCarres(int superficieMetresCarres) {
        this.superficieMetresCarres = (superficieMetresCarres >= 0) ? superficieMetresCarres : 0;
    }

    public String getAdresseLigne1() {
        return adresseLigne1;
    }

    public void setAdresseLigne1(String adresseLigne1) {
        this.adresseLigne1 = (adresseLigne1 != null) ? adresseLigne1.trim() : null;
    }

    public String getAdresseLigne2() {
        return adresseLigne2;
    }

    public void setAdresseLigne2(String adresseLigne2) {
        this.adresseLigne2 = (adresseLigne2 != null) ? adresseLigne2.trim() : null;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = (ville != null) ? ville.trim() : null;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = (codePostal != null) ? codePostal.trim() : null;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = (pays != null) ? pays.trim() : "France";
    }

    public String getEquipements() {
        return equipements;
    }

    public void setEquipements(String equipements) {
        this.equipements = equipements;
    }

    public String getReglementInterieur() {
        return reglementInterieur;
    }

    public void setReglementInterieur(String reglementInterieur) {
        this.reglementInterieur = (reglementInterieur != null) ? reglementInterieur.trim() : null;
    }

    public boolean isEstVisible() {
        return estVisible;
    }

    public void setEstVisible(boolean estVisible) {
        this.estVisible = estVisible;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Timestamp getDateModification() {
        return dateModification;
    }

    public void setDateModification(Timestamp dateModification) {
        this.dateModification = dateModification;
    }

    public List<MediaMaison> getMedias() {
        return medias;
    }

    public void setMedias(List<MediaMaison> medias) {
        this.medias = medias;
    }

    public MediaMaison getPhotoPrincipale() {
        return photoPrincipale;
    }

    public void setPhotoPrincipale(MediaMaison photoPrincipale) {
        this.photoPrincipale = photoPrincipale;
    }

    public String getNomProprietaire() {
        return nomProprietaire;
    }

    public void setNomProprietaire(String nomProprietaire) {
        this.nomProprietaire = nomProprietaire;
    }

    
    public String getAdresseComplete() {
        StringBuilder adresse = new StringBuilder();
        adresse.append(adresseLigne1 != null ? adresseLigne1 : "");
        if (adresseLigne2 != null && !adresseLigne2.isEmpty()) {
            adresse.append(", ").append(adresseLigne2);
        }
        adresse.append(", ").append(codePostal != null ? codePostal : "");
        adresse.append(" ").append(ville != null ? ville : "");
        adresse.append(", ").append(pays != null ? pays : "");
        return adresse.toString();
    }

    
    public String getPrixFormate() {
        return String.format("%.2f €", prixBaseNuit);
    }

    
    public boolean aDesPhotos() {
        return medias != null && !medias.isEmpty();
    }

   
    public String getCheminPhotoPrincipale() {
        if (photoPrincipale != null) {
            return photoPrincipale.getCheminFichier();
        }
        if (medias != null && !medias.isEmpty()) {
            for (MediaMaison media : medias) {
                if (media.isEstImagePrincipale()) {
                    return media.getCheminFichier();
                }
            }
            return medias.get(0).getCheminFichier();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Maison{" +
                "id=" + idMaison +
                ", titre='" + titre + '\'' +
                ", ville='" + ville + '\'' +
                ", prix=" + prixBaseNuit +
                ", visible=" + estVisible +
                '}';
    }
}