package com.examen_java.dao;

import com.examen_java.modele.Avis;
import java.util.List;


public interface AvisDAO {

    
    boolean creer(Avis avis);

    
    boolean existePourReservation(int idReservation);

    
    List<Avis> trouverParMaison(int idMaison);

    
    Avis trouverParReservation(int idReservation);

    
    boolean peutDonnerAvis(int idReservation, int idLocataire);
    
    List<Avis> trouverParProprietaire(int idProprietaire);
}