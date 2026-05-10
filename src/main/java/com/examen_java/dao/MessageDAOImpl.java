package com.examen_java.dao;

import com.examen_java.modele.Message;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class MessageDAOImpl implements MessageDAO {

    
    private static final String SQL_CONVERSATIONS_PROPRIETAIRE =
            "SELECT m.id_message, m.id_reservation, m.id_expediteur, m.contenu, " +
            "m.date_envoi, m.est_lu, " +
            "u.nom AS exp_nom, u.prenom AS exp_prenom, " +
            "ma.titre AS maison_titre, " +
            "loc.nom AS loc_nom, loc.prenom AS loc_prenom, " +
            "prop.nom AS prop_nom, prop.prenom AS prop_prenom " +
            "FROM messages m " +
            "JOIN utilisateurs u ON m.id_expediteur = u.id_utilisateur " +
            "JOIN reservations r ON m.id_reservation = r.id_reservation " +
            "JOIN maisons ma ON r.id_maison = ma.id_maison " +
            "JOIN utilisateurs loc ON r.id_locataire = loc.id_utilisateur " +
            "JOIN utilisateurs prop ON ma.id_proprietaire = prop.id_utilisateur " +
            "WHERE ma.id_proprietaire = ? " +
            "AND m.id_message IN (" +
            "   SELECT MAX(m2.id_message) FROM messages m2 " +
            "   GROUP BY m2.id_reservation" +
            ") " +
            "ORDER BY m.date_envoi DESC";

   
    private static final String SQL_MESSAGES_PAR_RESERVATION =
            "SELECT m.id_message, m.id_reservation, m.id_expediteur, m.contenu, " +
            "m.date_envoi, m.est_lu, " +
            "u.nom AS exp_nom, u.prenom AS exp_prenom, " +
            "ma.titre AS maison_titre, " +
            "loc.nom AS loc_nom, loc.prenom AS loc_prenom, " +
            "prop.nom AS prop_nom, prop.prenom AS prop_prenom " +
            "FROM messages m " +
            "JOIN utilisateurs u ON m.id_expediteur = u.id_utilisateur " +
            "JOIN reservations r ON m.id_reservation = r.id_reservation " +
            "JOIN maisons ma ON r.id_maison = ma.id_maison " +
            "JOIN utilisateurs loc ON r.id_locataire = loc.id_utilisateur " +
            "JOIN utilisateurs prop ON ma.id_proprietaire = prop.id_utilisateur " +
            "WHERE m.id_reservation = ? " +
            "ORDER BY m.date_envoi ASC";

    
    private static final String SQL_ENVOYER =
            "INSERT INTO messages (id_reservation, id_expediteur, contenu) " +
            "VALUES (?, ?, ?)";

    
    private static final String SQL_MARQUER_LU =
            "UPDATE messages SET est_lu = TRUE " +
            "WHERE id_reservation = ? AND id_expediteur != ? AND est_lu = FALSE";

    
    private static final String SQL_COMPTER_NON_LUS_PROPRIETAIRE =
            "SELECT COUNT(*) FROM messages m " +
            "JOIN reservations r ON m.id_reservation = r.id_reservation " +
            "JOIN maisons ma ON r.id_maison = ma.id_maison " +
            "WHERE ma.id_proprietaire = ? " +
            "AND m.est_lu = FALSE " +
            "AND m.id_expediteur != ?";

    
    private static final String SQL_RESERVATION_APPARTIENT =
            "SELECT COUNT(*) FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE r.id_reservation = ? " +
            "AND (m.id_proprietaire = ? OR r.id_locataire = ?)";

   

    @Override
    public List<Message> trouverConversationsParProprietaire(int idProprietaire) {
        List<Message> conversations = new ArrayList<>();

        if (idProprietaire <= 0) {
            return conversations;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CONVERSATIONS_PROPRIETAIRE)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    conversations.add(mapperMessage(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[MessageDAO] Erreur trouverConversationsParProprietaire : " + e.getMessage());
        }

        return conversations;
    }

    @Override
    public List<Message> trouverParReservation(int idReservation) {
        List<Message> messages = new ArrayList<>();

        if (idReservation <= 0) {
            return messages;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_MESSAGES_PAR_RESERVATION)) {

            requete.setInt(1, idReservation);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    messages.add(mapperMessage(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[MessageDAO] Erreur trouverParReservation : " + e.getMessage());
        }

        return messages;
    }

    @Override
    public boolean envoyer(Message message) {
        if (message == null || message.getIdReservation() <= 0 
                || message.getIdExpediteur() <= 0) {
            return false;
        }

        if (message.getContenu() == null || message.getContenu().trim().isEmpty()) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_ENVOYER, 
                     Statement.RETURN_GENERATED_KEYS)) {

            requete.setInt(1, message.getIdReservation());
            requete.setInt(2, message.getIdExpediteur());
            requete.setString(3, message.getContenu().trim());

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                ResultSet rs = requete.getGeneratedKeys();
                if (rs.next()) {
                    message.setIdMessage(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[MessageDAO] Erreur envoyer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean marquerCommeLu(int idReservation, int idUtilisateur) {
        if (idReservation <= 0 || idUtilisateur <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_MARQUER_LU)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idUtilisateur);

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[MessageDAO] " + lignes + " message(s) marqué(s) comme lu.");
            }
            return true;

        } catch (SQLException e) {
            System.err.println("[MessageDAO] Erreur marquerCommeLu : " + e.getMessage());
        }

        return false;
    }

    @Override
    public int compterNonLusParProprietaire(int idProprietaire) {
        if (idProprietaire <= 0) {
            return 0;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_COMPTER_NON_LUS_PROPRIETAIRE)) {

            requete.setInt(1, idProprietaire);
            requete.setInt(2, idProprietaire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[MessageDAO] Erreur compterNonLusParProprietaire : " + e.getMessage());
        }

        return 0;
    }

    @Override
    public boolean reservationAppartientA(int idReservation, int idUtilisateur) {
        if (idReservation <= 0 || idUtilisateur <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_RESERVATION_APPARTIENT)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idUtilisateur);
            requete.setInt(3, idUtilisateur);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[MessageDAO] Erreur reservationAppartientA : " + e.getMessage());
        }

        return false;
    }

    
    private Message mapperMessage(ResultSet rs) throws SQLException {
        Message message = new Message();

        
        message.setIdMessage(rs.getInt("id_message"));
        message.setIdReservation(rs.getInt("id_reservation"));
        message.setIdExpediteur(rs.getInt("id_expediteur"));
        message.setContenu(rs.getString("contenu"));
        message.setEstLu(rs.getBoolean("est_lu"));

        Timestamp dateEnvoi = rs.getTimestamp("date_envoi");
        if (dateEnvoi != null) {
            message.setDateEnvoi(dateEnvoi);
        }

        
        message.setNomExpediteur(rs.getString("exp_nom"));
        message.setPrenomExpediteur(rs.getString("exp_prenom"));

        
        message.setMaisonTitre(rs.getString("maison_titre"));

       
        message.setLocataireNom(rs.getString("loc_nom"));
        message.setLocatairePrenom(rs.getString("loc_prenom"));

     
        message.setProprietaireNom(rs.getString("prop_nom"));
        message.setProprietairePrenom(rs.getString("prop_prenom"));

        return message;
    }
    
    private static final String SQL_CONVERSATIONS_LOCATAIRE =
        "SELECT m.id_message, m.id_reservation, m.id_expediteur, m.contenu, " +
        "m.date_envoi, m.est_lu, " +
        "u.nom AS exp_nom, u.prenom AS exp_prenom, " +
        "ma.titre AS maison_titre, " +
        "loc.nom AS loc_nom, loc.prenom AS loc_prenom, " +
        "prop.nom AS prop_nom, prop.prenom AS prop_prenom " +
        "FROM messages m " +
        "JOIN utilisateurs u ON m.id_expediteur = u.id_utilisateur " +
        "JOIN reservations r ON m.id_reservation = r.id_reservation " +
        "JOIN maisons ma ON r.id_maison = ma.id_maison " +
        "JOIN utilisateurs loc ON r.id_locataire = loc.id_utilisateur " +
        "JOIN utilisateurs prop ON ma.id_proprietaire = prop.id_utilisateur " +
        "WHERE r.id_locataire = ? " +
        "AND m.id_message IN (" +
        "   SELECT MAX(m2.id_message) FROM messages m2 GROUP BY m2.id_reservation" +
        ") " +
        "ORDER BY m.date_envoi DESC";

private static final String SQL_COMPTER_NON_LUS_LOCATAIRE =
        "SELECT COUNT(*) FROM messages m " +
        "JOIN reservations r ON m.id_reservation = r.id_reservation " +
        "WHERE r.id_locataire = ? " +
        "AND m.est_lu = FALSE " +
        "AND m.id_expediteur != ?";

@Override
public List<Message> trouverConversationsParLocataire(int idLocataire) {
    List<Message> conversations = new ArrayList<>();
    if (idLocataire <= 0) return conversations;

    try (Connection connexion = DatabaseConnection.getConnection();
         PreparedStatement requete = connexion.prepareStatement(SQL_CONVERSATIONS_LOCATAIRE)) {
        requete.setInt(1, idLocataire);
        try (ResultSet resultat = requete.executeQuery()) {
            while (resultat.next()) {
                conversations.add(mapperMessage(resultat));
            }
        }
    } catch (SQLException e) {
        System.err.println("[MessageDAO] Erreur trouverConversationsParLocataire : " + e.getMessage());
    }
    return conversations;
}

@Override
public int compterNonLusParLocataire(int idLocataire) {
    if (idLocataire <= 0) return 0;
    try (Connection connexion = DatabaseConnection.getConnection();
         PreparedStatement requete = connexion.prepareStatement(SQL_COMPTER_NON_LUS_LOCATAIRE)) {
        requete.setInt(1, idLocataire);
        requete.setInt(2, idLocataire);
        try (ResultSet rs = requete.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    } catch (SQLException e) {
        System.err.println("[MessageDAO] Erreur compterNonLusParLocataire : " + e.getMessage());
    }
    return 0;
}
}