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


public class ReservationProprioDAOImpl implements ReservationProprioDAO {

    

    private static final String SQL_TROUVER_PAR_PROPRIETAIRE =
            "SELECT r.id_reservation, r.id_maison, r.id_locataire, " +
            "r.date_debut, r.date_fin, r.nombre_personnes, r.montant_total, " +
            "r.statut, r.date_reservation, r.message_locataire, " +
            "m.titre AS maison_titre, m.ville AS maison_ville, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, u.email AS locataire_email, " +
            "u.telephone AS locataire_telephone " +
            "FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE m.id_proprietaire = ? " +
            "ORDER BY r.date_reservation DESC";

    private static final String SQL_TROUVER_PAR_PROPRIETAIRE_ET_STATUT =
            "SELECT r.id_reservation, r.id_maison, r.id_locataire, " +
            "r.date_debut, r.date_fin, r.nombre_personnes, r.montant_total, " +
            "r.statut, r.date_reservation, r.message_locataire, " +
            "m.titre AS maison_titre, m.ville AS maison_ville, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, u.email AS locataire_email, " +
            "u.telephone AS locataire_telephone " +
            "FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE m.id_proprietaire = ? AND r.statut = ? " +
            "ORDER BY r.date_reservation DESC";

    private static final String SQL_TROUVER_PAR_ID =
            "SELECT r.id_reservation, r.id_maison, r.id_locataire, " +
            "r.date_debut, r.date_fin, r.nombre_personnes, r.montant_total, " +
            "r.statut, r.date_reservation, r.message_locataire, " +
            "m.titre AS maison_titre, m.ville AS maison_ville, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, u.email AS locataire_email, " +
            "u.telephone AS locataire_telephone " +
            "FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE r.id_reservation = ?";

    private static final String SQL_APPARTIENT_A_PROPRIETAIRE =
            "SELECT COUNT(*) FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE r.id_reservation = ? AND m.id_proprietaire = ?";

    private static final String SQL_CONFIRMER =
            "UPDATE reservations SET statut = 'CONFIRMEE' " +
            "WHERE id_reservation = ? AND statut = 'EN_ATTENTE_PAIEMENT'";

    private static final String SQL_ANNULER =
            "UPDATE reservations SET statut = 'ANNULEE_PAR_PROPRIETAIRE' " +
            "WHERE id_reservation = ?";

    private static final String SQL_COMPTER_EN_ATTENTE =
            "SELECT COUNT(*) FROM reservations r " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE m.id_proprietaire = ? AND r.statut = 'EN_ATTENTE_PAIEMENT'";

    

    @Override
    public List<ReservationProprio> trouverParProprietaire(int idProprietaire) {
        List<ReservationProprio> reservations = new ArrayList<>();

        if (idProprietaire <= 0) {
            return reservations;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_PROPRIETAIRE)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    reservations.add(mapperReservation(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur trouverParProprietaire : " + e.getMessage());
        }

        return reservations;
    }

    @Override
    public List<ReservationProprio> trouverParProprietaireEtStatut(int idProprietaire, String statut) {
        List<ReservationProprio> reservations = new ArrayList<>();

        if (idProprietaire <= 0 || statut == null || statut.trim().isEmpty()) {
            return reservations;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_PROPRIETAIRE_ET_STATUT)) {

            requete.setInt(1, idProprietaire);
            requete.setString(2, statut.trim().toUpperCase());

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    reservations.add(mapperReservation(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur trouverParProprietaireEtStatut : " + e.getMessage());
        }

        return reservations;
    }

    @Override
    public ReservationProprio trouverParId(int idReservation) {
        if (idReservation <= 0) {
            return null;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_ID)) {

            requete.setInt(1, idReservation);

            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    return mapperReservation(resultat);
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur trouverParId : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean appartientAProprietaire(int idReservation, int idProprietaire) {
        if (idReservation <= 0 || idProprietaire <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_APPARTIENT_A_PROPRIETAIRE)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idProprietaire);

            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    return resultat.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur appartientAProprietaire : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean confirmer(int idReservation) {
        if (idReservation <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CONFIRMER)) {

            requete.setInt(1, idReservation);
            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[ReservationProprioDAO] Réservation " + idReservation + " confirmée.");
            }

            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur confirmer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean annuler(int idReservation) {
        if (idReservation <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_ANNULER)) {

            requete.setInt(1, idReservation);
            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[ReservationProprioDAO] Réservation " + idReservation + " annulée.");
            }

            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur annuler : " + e.getMessage());
        }

        return false;
    }

    @Override
    public int compterEnAttente(int idProprietaire) {
        if (idProprietaire <= 0) {
            return 0;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_COMPTER_EN_ATTENTE)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    return resultat.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[ReservationProprioDAO] Erreur compterEnAttente : " + e.getMessage());
        }

        return 0;
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