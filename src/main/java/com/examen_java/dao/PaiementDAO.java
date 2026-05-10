package com.examen_java.dao;

import com.examen_java.modele.ReservationProprio;
import java.util.List;


public interface PaiementDAO {

    
    boolean effectuerPaiement(int idReservation, int idLocataire, double montant, String methodePaiement);

    
    boolean peutPayer(int idReservation, int idLocataire);

    
    boolean paiementExiste(int idReservation);
    
   List<ReservationProprio> trouverParLocataire(int idLocataire);
}