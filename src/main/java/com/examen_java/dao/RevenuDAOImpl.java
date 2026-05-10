package com.examen_java.dao;

import com.examen_java.modele.Revenu;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class RevenuDAOImpl implements RevenuDAO {

  

    private static final String SQL_TROUVER_PAR_PROPRIETAIRE =
            "SELECT p.id_paiement, p.id_reservation, p.montant, p.methode_paiement, " +
            "p.statut, p.reference_transaction, p.date_paiement, " +
            "m.id_maison, m.titre AS maison_titre, m.id_proprietaire, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, " +
            "r.date_debut, r.date_fin " +
            "FROM paiements p " +
            "JOIN reservations r ON p.id_reservation = r.id_reservation " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE m.id_proprietaire = ? " +
            "ORDER BY p.date_paiement DESC";

    private static final String SQL_TROUVER_PAR_PROPRIETAIRE_ET_PERIODE =
            "SELECT p.id_paiement, p.id_reservation, p.montant, p.methode_paiement, " +
            "p.statut, p.reference_transaction, p.date_paiement, " +
            "m.id_maison, m.titre AS maison_titre, m.id_proprietaire, " +
            "u.nom AS locataire_nom, u.prenom AS locataire_prenom, " +
            "r.date_debut, r.date_fin " +
            "FROM paiements p " +
            "JOIN reservations r ON p.id_reservation = r.id_reservation " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "JOIN utilisateurs u ON r.id_locataire = u.id_utilisateur " +
            "WHERE m.id_proprietaire = ? " +
            "AND p.date_paiement BETWEEN ? AND ? " +
            "ORDER BY p.date_paiement DESC";

    private static final String SQL_CALCULER_TOTAL =
            "SELECT COALESCE(SUM(p.montant), 0) " +
            "FROM paiements p " +
            "JOIN reservations r ON p.id_reservation = r.id_reservation " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE m.id_proprietaire = ? AND p.statut = 'REUSSI'";

    private static final String SQL_CALCULER_TOTAL_MOIS =
            "SELECT COALESCE(SUM(p.montant), 0) " +
            "FROM paiements p " +
            "JOIN reservations r ON p.id_reservation = r.id_reservation " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE m.id_proprietaire = ? AND p.statut = 'REUSSI' " +
            "AND MONTH(p.date_paiement) = MONTH(CURRENT_DATE()) " +
            "AND YEAR(p.date_paiement) = YEAR(CURRENT_DATE())";

    private static final String SQL_COMPTER_TRANSACTIONS =
            "SELECT COUNT(*) " +
            "FROM paiements p " +
            "JOIN reservations r ON p.id_reservation = r.id_reservation " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE m.id_proprietaire = ? AND p.statut = 'REUSSI'";

    private static final String SQL_REVENU_PAR_MAISON =
            "SELECT m.id_maison, m.titre, COALESCE(SUM(p.montant), 0) AS total " +
            "FROM paiements p " +
            "JOIN reservations r ON p.id_reservation = r.id_reservation " +
            "JOIN maisons m ON r.id_maison = m.id_maison " +
            "WHERE m.id_proprietaire = ? AND p.statut = 'REUSSI' " +
            "GROUP BY m.id_maison, m.titre " +
            "ORDER BY total DESC";

   

    @Override
    public List<Revenu> trouverParProprietaire(int idProprietaire) {
        List<Revenu> revenus = new ArrayList<>();

        if (idProprietaire <= 0) {
            return revenus;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_PROPRIETAIRE)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    revenus.add(mapperRevenu(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[RevenuDAO] Erreur trouverParProprietaire : " + e.getMessage());
        }

        return revenus;
    }

    @Override
    public List<Revenu> trouverParProprietaireEtPeriode(int idProprietaire, String dateDebut, String dateFin) {
        List<Revenu> revenus = new ArrayList<>();

        if (idProprietaire <= 0 || dateDebut == null || dateFin == null) {
            return revenus;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_PROPRIETAIRE_ET_PERIODE)) {

            requete.setInt(1, idProprietaire);
            requete.setString(2, dateDebut);
            requete.setString(3, dateFin);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    revenus.add(mapperRevenu(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[RevenuDAO] Erreur trouverParProprietaireEtPeriode : " + e.getMessage());
        }

        return revenus;
    }

    @Override
    public double calculerTotal(int idProprietaire) {
        if (idProprietaire <= 0) {
            return 0.0;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CALCULER_TOTAL)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RevenuDAO] Erreur calculerTotal : " + e.getMessage());
        }

        return 0.0;
    }

    @Override
    public double calculerTotalMoisEnCours(int idProprietaire) {
        if (idProprietaire <= 0) {
            return 0.0;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CALCULER_TOTAL_MOIS)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RevenuDAO] Erreur calculerTotalMoisEnCours : " + e.getMessage());
        }

        return 0.0;
    }

    @Override
    public int compterTransactions(int idProprietaire) {
        if (idProprietaire <= 0) {
            return 0;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_COMPTER_TRANSACTIONS)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RevenuDAO] Erreur compterTransactions : " + e.getMessage());
        }

        return 0;
    }

    @Override
    public List<Object[]> calculerRevenuParMaison(int idProprietaire) {
        List<Object[]> resultats = new ArrayList<>();

        if (idProprietaire <= 0) {
            return resultats;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_REVENU_PAR_MAISON)) {

            requete.setInt(1, idProprietaire);

            try (ResultSet rs = requete.executeQuery()) {
                while (rs.next()) {
                    Object[] ligne = new Object[3];
                    ligne[0] = rs.getInt("id_maison");
                    ligne[1] = rs.getString("titre");
                    ligne[2] = rs.getDouble("total");
                    resultats.add(ligne);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RevenuDAO] Erreur calculerRevenuParMaison : " + e.getMessage());
        }

        return resultats;
    }

    

    private Revenu mapperRevenu(ResultSet rs) throws SQLException {
        Revenu revenu = new Revenu();

      
        revenu.setIdPaiement(rs.getInt("id_paiement"));
        revenu.setIdReservation(rs.getInt("id_reservation"));
        revenu.setMontant(rs.getDouble("montant"));
        revenu.setMethodePaiement(rs.getString("methode_paiement"));
        revenu.setStatut(rs.getString("statut"));
        revenu.setReferenceTransaction(rs.getString("reference_transaction"));

        Timestamp datePaiement = rs.getTimestamp("date_paiement");
        if (datePaiement != null) {
            revenu.setDatePaiement(datePaiement);
        }

        
        revenu.setIdMaison(rs.getInt("id_maison"));
        revenu.setMaisonTitre(rs.getString("maison_titre"));
        revenu.setIdProprietaire(rs.getInt("id_proprietaire"));

      
        revenu.setLocataireNom(rs.getString("locataire_nom"));
        revenu.setLocatairePrenom(rs.getString("locataire_prenom"));

        
        revenu.setDateDebut(rs.getDate("date_debut"));
        revenu.setDateFin(rs.getDate("date_fin"));

        return revenu;
    }
}