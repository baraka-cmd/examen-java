package com.examen_java.modele;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ReservationProprio {

    
    private int idReservation;

    
    private int idMaison;

    
    private int idLocataire;

    
    private Date dateDebut;

   
    private Date dateFin;

    
    private int nombrePersonnes;

    
    private double montantTotal;

    
    private String statut;

    
    private Timestamp dateReservation;

 
    private String messageLocataire;

   
    private String maisonTitre;

    
    private String maisonVille;

    
    private String locataireNom;

    
    private String locatairePrenom;

    
    private String locataireEmail;

    
    private String locataireTelephone;
    
    
    private MediaMaison photoPrincipale;

   
    public ReservationProprio() {
        this.nombrePersonnes = 1;
        this.montantTotal = 0.0;
    }

   

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdMaison() {
        return idMaison;
    }

    public void setIdMaison(int idMaison) {
        this.idMaison = idMaison;
    }

    public int getIdLocataire() {
        return idLocataire;
    }

    public void setIdLocataire(int idLocataire) {
        this.idLocataire = idLocataire;
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

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = (nombrePersonnes > 0) ? nombrePersonnes : 1;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Timestamp getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Timestamp dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getMessageLocataire() {
        return messageLocataire;
    }

    public void setMessageLocataire(String messageLocataire) {
        this.messageLocataire = messageLocataire;
    }

   

    public String getMaisonTitre() {
        return maisonTitre;
    }

    public void setMaisonTitre(String maisonTitre) {
        this.maisonTitre = maisonTitre;
    }

    public String getMaisonVille() {
        return maisonVille;
    }

    public void setMaisonVille(String maisonVille) {
        this.maisonVille = maisonVille;
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

    public String getLocataireEmail() {
        return locataireEmail;
    }

    public void setLocataireEmail(String locataireEmail) {
        this.locataireEmail = locataireEmail;
    }

    public String getLocataireTelephone() {
        return locataireTelephone;
    }

    public void setLocataireTelephone(String locataireTelephone) {
        this.locataireTelephone = locataireTelephone;
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
        return sb.toString();
    }

    
    public long getNombreNuits() {
        if (dateDebut == null || dateFin == null) {
            return 0;
        }
        long diff = dateFin.getTime() - dateDebut.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    
    public String getDateDebutFormatee() {
        if (dateDebut == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy").format(dateDebut);
    }

   
    public String getDateFinFormatee() {
        if (dateFin == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy").format(dateFin);
    }

    
    public String getMontantFormate() {
        return String.format("%.2f €", montantTotal);
    }

    
    public boolean estEnAttente() {
        return "EN_ATTENTE_PAIEMENT".equalsIgnoreCase(statut);
    }

    
    public boolean estConfirmee() {
        return "CONFIRMEE".equalsIgnoreCase(statut);
    }

    
    public boolean estAnnulee() {
        return statut != null && statut.toUpperCase().contains("ANNULEE");
    }

    
    public boolean estTerminee() {
        return "TERMINEE".equalsIgnoreCase(statut);
    }

    
    public String getStatutLibelle() {
        if (statut == null) return "Inconnu";
        switch (statut.toUpperCase()) {
            case "BROUILLON":               return "Brouillon";
            case "EN_ATTENTE_PAIEMENT":     return "En attente";
            case "CONFIRMEE":               return "Confirmée";
            case "ANNULEE_PAR_LOCATAIRE":   return "Annulée par locataire";
            case "ANNULEE_PAR_PROPRIETAIRE": return "Annulée par vous";
            case "TERMINEE":                return "Terminée";
            default:                        return statut;
        }
    }

    
    public String getStatutClass() {
        if (statut == null) return "statut-INCONNU";
        return "statut-" + statut.toUpperCase().replace(" ", "_");
    }

    
    public boolean peutEtreConfirmee() {
        return estEnAttente();
    }

    
    public boolean peutEtreAnnulee() {
        return !estAnnulee() && !estTerminee();
    }

    public MediaMaison getPhotoPrincipale() {
        return photoPrincipale;
    }
    
    
    public void setPhotoPrincipale(MediaMaison photoPrincipale) {
        this.photoPrincipale = photoPrincipale;
    }
    @Override
    public String toString() {
        return "ReservationProprio{" +
                "id=" + idReservation +
                ", maison='" + maisonTitre + '\'' +
                ", locataire='" + getLocataireNomComplet() + '\'' +
                ", dates=" + getDateDebutFormatee() + " → " + getDateFinFormatee() +
                ", statut='" + getStatutLibelle() + '\'' +
                ", montant=" + getMontantFormate() +
                '}';
    }
}