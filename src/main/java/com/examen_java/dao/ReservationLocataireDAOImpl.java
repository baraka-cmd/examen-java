package com.examen_java.dao;

import com.examen_java.modele.ReservationProprio;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class ReservationLocataireDAOImpl implements ReservationLocataireDAO {

    
    private static final String SQL_EST_DISPONIBLE =
            "SELECT COUNT(*) FROM reservations " +
            "WHERE id_maison = ? " +
            "AND statut IN ('EN_ATTENTE_PAIEMENT', 'CONFIRMEE') " +
            "AND date_debut < ? AND date_fin > ?";

   
    private static final String SQL_CREER =
            "INSERT INTO reservations (id_maison, id_locataire, date_debut, date_fin, " +
            "nombre_personnes, montant_total, statut) " +
            "VALUES (?, ?, ?, ?, ?, ?, 'EN_ATTENTE_PAIEMENT')";

    
    private static final String SQL_TROUVER_PAR_LOCATAIRE =
            "SELECT r.id_reservation, r.id_maison, r.id_locataire, " +
            "r.date_debut, r.date_fin, r.nombre_personnes, r.montant_total, " +
            "r.statut, r.date_reservation, r.message_locataire, " +
            "m.titre AS maison_titre, m.ville AS maison_ville, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, " +
            "u.email AS locataire_email, u.telephone AS locataire_telephone " +
            "FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE r.id_locataire = ? " +
            "ORDER BY r.date_reservation DESC";

    
    private static final String SQL_TROUVER_PAR_LOCATAIRE_ET_STATUT =
            "SELECT r.id_reservation, r.id_maison, r.id_locataire, " +
            "r.date_debut, r.date_fin, r.nombre_personnes, r.montant_total, " +
            "r.statut, r.date_reservation, r.message_locataire, " +
            "m.titre AS maison_titre, m.ville AS maison_ville, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, " +
            "u.email AS locataire_email, u.telephone AS locataire_telephone " +
            "FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE r.id_locataire = ? AND r.statut = ? " +
            "ORDER BY r.date_reservation DESC";

   
    private static final String SQL_ANNULER =
            "UPDATE reservations SET statut = 'ANNULEE_PAR_LOCATAIRE' " +
            "WHERE id_reservation = ? AND id_locataire = ?";

    /** Vérifie l'appartenance d'une réservation à un locataire */
    private static final String SQL_APPARTIENT_A =
            "SELECT COUNT(*) FROM reservations " +
            "WHERE id_reservation = ? AND id_locataire = ?";

    

    @Override
    public boolean estDisponible(int idMaison, String dateDebut, String dateFin) {
        if (idMaison <= 0 || dateDebut == null || dateFin == null) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_EST_DISPONIBLE)) {

            requete.setInt(1, idMaison);
            requete.setString(2, dateFin.trim());
            requete.setString(3, dateDebut.trim());

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationLocataireDAO] Erreur estDisponible : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean creer(int idMaison, int idLocataire, String dateDebut, String dateFin,
                         int nombrePersonnes, double montantTotal) {
        if (idMaison <= 0 || idLocataire <= 0 || dateDebut == null || dateFin == null) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CREER)) {

            requete.setInt(1, idMaison);
            requete.setInt(2, idLocataire);
            requete.setString(3, dateDebut.trim());
            requete.setString(4, dateFin.trim());
            requete.setInt(5, nombrePersonnes);
            requete.setDouble(6, montantTotal);

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[ReservationLocataireDAO] Réservation créée - Maison: " 
                        + idMaison + ", Locataire: " + idLocataire);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[ReservationLocataireDAO] Erreur creer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<ReservationProprio> trouverParLocataire(int idLocataire) {
        List<ReservationProprio> reservations = new ArrayList<>();

        if (idLocataire <= 0) {
            return reservations;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_LOCATAIRE)) {

            requete.setInt(1, idLocataire);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    reservations.add(mapperReservation(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationLocataireDAO] Erreur trouverParLocataire : " + e.getMessage());
        }

        return reservations;
    }

    @Override
    public List<ReservationProprio> trouverParLocataireEtStatut(int idLocataire, String statut) {
        List<ReservationProprio> reservations = new ArrayList<>();

        if (idLocataire <= 0 || statut == null || statut.trim().isEmpty()) {
            return reservations;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_LOCATAIRE_ET_STATUT)) {

            requete.setInt(1, idLocataire);
            requete.setString(2, statut.trim().toUpperCase());

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    reservations.add(mapperReservation(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationLocataireDAO] Erreur trouverParLocataireEtStatut : " 
                    + e.getMessage());
        }

        return reservations;
    }

    @Override
    public boolean annuler(int idReservation, int idLocataire) {
        if (idReservation <= 0 || idLocataire <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_ANNULER)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idLocataire);

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[ReservationLocataireDAO] Réservation " + idReservation 
                        + " annulée par locataire " + idLocataire);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[ReservationLocataireDAO] Erreur annuler : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean appartientA(int idReservation, int idLocataire) {
        if (idReservation <= 0 || idLocataire <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_APPARTIENT_A)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idLocataire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationLocataireDAO] Erreur appartientA : " + e.getMessage());
        }

        return false;
    }

   

    private ReservationProprio mapperReservation(ResultSet rs) throws SQLException {
        ReservationProprio reservation = new ReservationProprio();

        reservation.setIdReservation(rs.getInt("id_reservation"));
        reservation.setIdMaison(rs.getInt("id_maison"));
        reservation.setIdLocataire(rs.getInt("id_locataire"));
        reservation.setDateDebut(rs.getDate("date_debut"));
        reservation.setDateFin(rs.getDate("date_fin"));
        reservation.setNombrePersonnes(rs.getInt("nombre_personnes"));
        reservation.setMontantTotal(rs.getDouble("montant_total"));
        reservation.setStatut(rs.getString("statut"));
        reservation.setMessageLocataire(rs.getString("message_locataire"));

        Timestamp dateReservation = rs.getTimestamp("date_reservation");
        if (dateReservation != null) {
            reservation.setDateReservation(dateReservation);
        }

        reservation.setMaisonTitre(rs.getString("maison_titre"));
        reservation.setMaisonVille(rs.getString("maison_ville"));
        reservation.setLocataireNom(rs.getString("locataire_nom"));
        reservation.setLocatairePrenom(rs.getString("locataire_prenom"));
        reservation.setLocataireEmail(rs.getString("locataire_email"));
        reservation.setLocataireTelephone(rs.getString("locataire_telephone"));

        return reservation;
    }
}