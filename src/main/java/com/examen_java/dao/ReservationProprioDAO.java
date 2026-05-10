package com.examen_java.dao;

import com.examen_java.modele.ReservationProprio;
import java.util.List;


public interface ReservationProprioDAO {

    
    List<ReservationProprio> trouverParProprietaire(int idProprietaire);

    
    List<ReservationProprio> trouverParProprietaireEtStatut(int idProprietaire, String statut);

    
    ReservationProprio trouverParId(int idReservation);

    
    boolean appartientAProprietaire(int idReservation, int idProprietaire);

    
    boolean confirmer(int idReservation);

    
    boolean annuler(int idReservation);

   
    int compterEnAttente(int idProprietaire);
}