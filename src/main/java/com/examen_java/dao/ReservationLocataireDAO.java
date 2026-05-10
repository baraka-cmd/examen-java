package com.examen_java.dao;

import com.examen_java.modele.ReservationProprio;
import java.util.List;


public interface ReservationLocataireDAO {

    
    boolean estDisponible(int idMaison, String dateDebut, String dateFin);

    
    boolean creer(int idMaison, int idLocataire, String dateDebut, String dateFin,
                  int nombrePersonnes, double montantTotal);

    
    List<ReservationProprio> trouverParLocataire(int idLocataire);

    
    List<ReservationProprio> trouverParLocataireEtStatut(int idLocataire, String statut);

    
    boolean annuler(int idReservation, int idLocataire);

    
    boolean appartientA(int idReservation, int idLocataire);
}