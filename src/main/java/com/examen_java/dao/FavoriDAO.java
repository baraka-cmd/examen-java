package com.examen_java.dao;

import com.examen_java.modele.Maison;
import java.util.List;


public interface FavoriDAO {

    
    boolean ajouter(int idUtilisateur, int idMaison);

    
    boolean supprimer(int idUtilisateur, int idMaison);

    
    boolean estFavori(int idUtilisateur, int idMaison);

    
    List<Maison> trouverParUtilisateur(int idUtilisateur);

    
    int compter(int idUtilisateur);
}