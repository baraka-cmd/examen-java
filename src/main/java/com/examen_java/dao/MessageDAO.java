package com.examen_java.dao;

import com.examen_java.modele.Message;
import java.util.List;


public interface MessageDAO {

    
    List<Message> trouverConversationsParProprietaire(int idProprietaire);

    
    List<Message> trouverParReservation(int idReservation);

    
    boolean envoyer(Message message);

    
    boolean marquerCommeLu(int idReservation, int idUtilisateur);

    
    int compterNonLusParProprietaire(int idProprietaire);

    
    boolean reservationAppartientA(int idReservation, int idUtilisateur);
    
    
   List<Message> trouverConversationsParLocataire(int idLocataire);

   
   int compterNonLusParLocataire(int idLocataire);
}