package com.examen_java.dao;

import com.examen_java.modele.Revenu;
import java.util.List;


public interface RevenuDAO {

   
    List<Revenu> trouverParProprietaire(int idProprietaire);

    
    List<Revenu> trouverParProprietaireEtPeriode(int idProprietaire, String dateDebut, String dateFin);

    
    double calculerTotal(int idProprietaire);

    
    double calculerTotalMoisEnCours(int idProprietaire);

    
    int compterTransactions(int idProprietaire);

    
    List<Object[]> calculerRevenuParMaison(int idProprietaire);
}