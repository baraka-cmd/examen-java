package com.examen_java.dao;

import com.examen_java.modele.Maison;
import java.util.List;

public interface MaisonDAO {
    List<Maison> trouverParProprietaire(int idProprietaire);
    Maison trouverParId(int idMaison);
    int creer(Maison maison);
    boolean modifier(Maison maison);
    boolean desactiver(int idMaison, int idProprietaire);
    boolean appartientA(int idMaison, int idProprietaire);
    int compterActives(int idProprietaire);
    List<Maison> trouverDernieresMaisons(int limite);
}