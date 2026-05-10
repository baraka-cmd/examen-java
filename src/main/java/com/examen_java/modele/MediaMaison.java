package com.examen_java.modele;


public class MediaMaison {

   

    private int idMedia;
    private int idMaison;
    private String cheminFichier;
    private String legende;
    private boolean estImagePrincipale;
    private int ordreAffichage;

    

    public MediaMaison() {
        this.estImagePrincipale = false;
        this.ordreAffichage = 0;
    }

    
    public MediaMaison(int idMaison, String cheminFichier) {
        this();
        this.idMaison = idMaison;
        this.cheminFichier = cheminFichier;
    }

    
    public MediaMaison(int idMaison, String cheminFichier, String legende, 
                       boolean estImagePrincipale, int ordreAffichage) {
        this.idMaison = idMaison;
        this.cheminFichier = cheminFichier;
        this.legende = legende;
        this.estImagePrincipale = estImagePrincipale;
        this.ordreAffichage = ordreAffichage;
    }

  

    public int getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(int idMedia) {
        this.idMedia = idMedia;
    }

    public int getIdMaison() {
        return idMaison;
    }

    public void setIdMaison(int idMaison) {
        this.idMaison = idMaison;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public String getLegende() {
        return legende;
    }

    public void setLegende(String legende) {
        this.legende = (legende != null) ? legende.trim() : null;
    }

    public boolean isEstImagePrincipale() {
        return estImagePrincipale;
    }

    public void setEstImagePrincipale(boolean estImagePrincipale) {
        this.estImagePrincipale = estImagePrincipale;
    }

    public int getOrdreAffichage() {
        return ordreAffichage;
    }

    public void setOrdreAffichage(int ordreAffichage) {
        this.ordreAffichage = (ordreAffichage >= 0) ? ordreAffichage : 0;
    }

    
    public String getCheminComplet(String contextPath) {
        if (cheminFichier == null || cheminFichier.isEmpty()) {
            return contextPath + "/assets/images/placeholder.jpg";
        }
        return contextPath + "/assets/images/uploads/" + cheminFichier;
    }

   
    public boolean aUneLegende() {
        return legende != null && !legende.isEmpty();
    }

    @Override
    public String toString() {
        return "MediaMaison{" +
                "id=" + idMedia +
                ", maison=" + idMaison +
                ", chemin='" + cheminFichier + '\'' +
                ", principale=" + estImagePrincipale +
                ", ordre=" + ordreAffichage +
                '}';
    }
}