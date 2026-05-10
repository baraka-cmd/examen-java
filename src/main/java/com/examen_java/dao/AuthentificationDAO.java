package com.examen_java.dao;

import com.examen_java.modele.ProfilInscription;
import com.examen_java.modele.UtilisateurInscription;
import java.util.List;

public interface AuthentificationDAO {
    UtilisateurInscription trouverParEmail(String email);
    List<ProfilInscription> trouverProfilsParUtilisateur(int idUtilisateur);
    void mettreAJourDerniereConnexion(int idUtilisateur);
    boolean estCompteActif(String email);

    
UtilisateurInscription trouverParId(int idUtilisateur);


boolean mettreAJour(UtilisateurInscription utilisateur);


boolean mettreAJourMotDePasse(UtilisateurInscription utilisateur);
}