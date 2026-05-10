package com.examen_java.dao;

import com.examen_java.modele.ProfilInscription;
import com.examen_java.modele.UtilisateurInscription;


public interface InscriptionDAO {

    
    boolean emailExiste(String email);

    
    int creerUtilisateur(UtilisateurInscription utilisateur);

    
    void creerProfil(ProfilInscription profil);

}