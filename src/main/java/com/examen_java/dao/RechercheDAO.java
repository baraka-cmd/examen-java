package com.examen_java.dao;

import com.examen_java.modele.Maison;
import java.util.List;


public interface RechercheDAO {

    
    List<Maison> rechercher(String ville, String dateDebut, String dateFin, 
                            int capacite, double prixMax);

    
    List<Maison> trouverDernieresMaisons(int limite);

    
    int compterResultats(String ville, int capacite, double prixMax);
}