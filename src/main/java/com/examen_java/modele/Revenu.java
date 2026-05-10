package com.examen_java.modele;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Revenu {

    
    private int idPaiement;

    
    private int idReservation;

    
    private double montant;

   
    private String methodePaiement;

   
    private String statut;

    
    private String referenceTransaction;

    
    private Timestamp datePaiement;

    
    
    private String maisonTitre;

    
    private int idMaison;

    
    private int idProprietaire;

    
    private String locataireNom;

    
    private String locatairePrenom;

    
    private Date dateDebut;

    
    private Date dateFin;

 

    public Revenu() {
        this.montant = 0.0;
    }

   

    public int getIdPaiement() {
        return idPaiement;
    }

    public void setIdPaiement(int idPaiement) {
        this.idPaiement = idPaiement;
    }

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getMethodePaiement() {
        return methodePaiement;
    }

    public void setMethodePaiement(String methodePaiement) {
        this.methodePaiement = methodePaiement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getReferenceTransaction() {
        return referenceTransaction;
    }

    public void setReferenceTransaction(String referenceTransaction) {
        this.referenceTransaction = referenceTransaction;
    }

    public Timestamp getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(Timestamp datePaiement) {
        this.datePaiement = datePaiement;
    }

   

    public String getMaisonTitre() {
        return maisonTitre;
    }

    public void setMaisonTitre(String maisonTitre) {
        this.maisonTitre = maisonTitre;
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    
    public String getLocataireNomComplet() {
        StringBuilder sb = new StringBuilder();
        if (locatairePrenom != null && !locatairePrenom.isEmpty()) {
            sb.append(locatairePrenom);
        }
        if (locataireNom != null && !locataireNom.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(locataireNom);
        }
        return sb.length() > 0 ? sb.toString() : "-";
    }

   
    public String getMontantFormate() {
        return String.format("%.2f €", montant);
    }

   
    public String getDatePaiementFormatee() {
        if (datePaiement == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy").format(datePaiement);
    }

    
    public String getPeriodeSejour() {
        if (dateDebut == null || dateFin == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dateDebut) + " → " + sdf.format(dateFin);
    }

    
    public boolean estReussi() {
        return "REUSSI".equalsIgnoreCase(statut);
    }

    
    public String getStatutLibelle() {
        if (statut == null) return "Inconnu";
        switch (statut.toUpperCase()) {
            case "REUSSI":    return "Réussi";
            case "ECHOUE":    return "Échoué";
            case "REMBOURSE": return "Remboursé";
            case "EN_COURS":  return "En cours";
            default:          return statut;
        }
    }

    
    public String getStatutClass() {
        if (statut == null) return "statut-INCONNU";
        return "statut-" + statut.toUpperCase();
    }

    @Override
    public String toString() {
        return "Revenu{" +
                "id=" + idPaiement +
                ", maison='" + maisonTitre + '\'' +
                ", locataire='" + getLocataireNomComplet() + '\'' +
                ", montant=" + getMontantFormate() +
                ", statut='" + getStatutLibelle() + '\'' +
                '}';
    }
}