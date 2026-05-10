package com.examen_java.dao;

import com.examen_java.modele.MediaMaison;
import java.util.List;

public interface MediaMaisonDAO {
    List<MediaMaison> trouverParMaison(int idMaison);
    MediaMaison trouverPhotoPrincipale(int idMaison);
    boolean ajouter(MediaMaison media);
    boolean supprimer(int idMedia);
    boolean supprimerParMaison(int idMaison);
    boolean definirCommePrincipale(int idMedia, int idMaison);
}