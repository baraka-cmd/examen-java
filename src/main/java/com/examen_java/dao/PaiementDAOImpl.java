package com.examen_java.dao;

import com.examen_java.modele.ReservationProprio;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class PaiementDAOImpl implements PaiementDAO {

    

    private static final String SQL_CREER_PAIEMENT =
            "INSERT INTO paiements (id_reservation, montant, methode_paiement, statut, reference_transaction) " +
            "VALUES (?, ?, ?, 'REUSSI', ?)";

    private static final String SQL_CONFIRMER_RESERVATION =
            "UPDATE reservations SET statut = 'CONFIRMEE' WHERE id_reservation = ? AND id_locataire = ?";

    private static final String SQL_PEUT_PAYER =
            "SELECT COUNT(*) FROM reservations " +
            "WHERE id_reservation = ? AND id_locataire = ? AND statut = 'EN_ATTENTE_PAIEMENT'";

    private static final String SQL_PAIEMENT_EXISTE =
            "SELECT COUNT(*) FROM paiements WHERE id_reservation = ?";

  

    @Override
    public boolean effectuerPaiement(int idReservation, int idLocataire, double montant, String methodePaiement) {
        if (idReservation <= 0 || idLocataire <= 0 || montant <= 0) {
            return false;
        }

        if (methodePaiement == null || methodePaiement.trim().isEmpty()) {
            methodePaiement = "CARTE_BANCAIRE";
        }

        // Vérifier que le paiement est possible
        if (!peutPayer(idReservation, idLocataire)) {
            System.err.println("[PaiementDAO] Paiement impossible pour la réservation " + idReservation);
            return false;
        }

        // Vérifier qu'il n'y a pas déjà un paiement
        if (paiementExiste(idReservation)) {
            System.err.println("[PaiementDAO] Un paiement existe déjà pour la réservation " + idReservation);
            return false;
        }

        Connection connexion = null;
        try {
            connexion = DatabaseConnection.getConnection();
            connexion.setAutoCommit(false); // Transaction

            
            String reference = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            PreparedStatement paiementStmt = connexion.prepareStatement(SQL_CREER_PAIEMENT);
            paiementStmt.setInt(1, idReservation);
            paiementStmt.setDouble(2, montant);
            paiementStmt.setString(3, methodePaiement.trim().toUpperCase());
            paiementStmt.setString(4, reference);
            int paiementCree = paiementStmt.executeUpdate();

            
            PreparedStatement reservationStmt = connexion.prepareStatement(SQL_CONFIRMER_RESERVATION);
            reservationStmt.setInt(1, idReservation);
            reservationStmt.setInt(2, idLocataire);
            int reservationMaj = reservationStmt.executeUpdate();

            if (paiementCree > 0 && reservationMaj > 0) {
                connexion.commit();
                System.out.println("[PaiementDAO] Paiement effectué - Réservation: " + idReservation 
                        + ", Réf: " + reference + ", Montant: " + montant + " €");
                return true;
            } else {
                connexion.rollback();
                System.err.println("[PaiementDAO] Échec de la transaction.");
            }

        } catch (SQLException e) {
            System.err.println("[PaiementDAO] Erreur effectuerPaiement : " + e.getMessage());
            try {
                if (connexion != null) connexion.rollback();
            } catch (SQLException ex) {
                System.err.println("[PaiementDAO] Erreur rollback : " + ex.getMessage());
            }
        } finally {
            try {
                if (connexion != null) {
                    connexion.setAutoCommit(true);
                    connexion.close();
                }
            } catch (SQLException e) {
                System.err.println("[PaiementDAO] Erreur fermeture connexion : " + e.getMessage());
            }
        }

        return false;
    }

    @Override
    public boolean peutPayer(int idReservation, int idLocataire) {
        if (idReservation <= 0 || idLocataire <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_PEUT_PAYER)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idLocataire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[PaiementDAO] Erreur peutPayer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean paiementExiste(int idReservation) {
        if (idReservation <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_PAIEMENT_EXISTE)) {

            requete.setInt(1, idReservation);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[PaiementDAO] Erreur paiementExiste : " + e.getMessage());
        }

        return false;
    }
    
    @Override
    public List<ReservationProprio> trouverParLocataire(int idLocataire) {
        
        return null;
    }
}