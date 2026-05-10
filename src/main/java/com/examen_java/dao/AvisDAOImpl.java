package com.examen_java.dao;

import com.examen_java.modele.Avis;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class AvisDAOImpl implements AvisDAO {

    

    private static final String SQL_CREER =
            "INSERT INTO avis (id_reservation, id_maison, id_auteur, " +
            "note_proprete, note_communication, note_emplacement, note_rapport_qualite_prix, commentaire) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_EXISTE_POUR_RESERVATION =
            "SELECT COUNT(*) FROM avis WHERE id_reservation = ?";

    private static final String SQL_TROUVER_PAR_MAISON =
            "SELECT a.*, u.nom AS auteur_nom, u.prenom AS auteur_prenom " +
            "FROM avis a " +
            "JOIN utilisateurs u ON a.id_auteur = u.id_utilisateur " +
            "WHERE a.id_maison = ? " +
            "ORDER BY a.date_publication DESC";

    private static final String SQL_TROUVER_PAR_RESERVATION =
            "SELECT a.*, u.nom AS auteur_nom, u.prenom AS auteur_prenom " +
            "FROM avis a " +
            "JOIN utilisateurs u ON a.id_auteur = u.id_utilisateur " +
            "WHERE a.id_reservation = ?";

    private static final String SQL_PEUT_DONNER_AVIS =
            "SELECT COUNT(*) FROM reservations " +
            "WHERE id_reservation = ? AND id_locataire = ? AND statut = 'TERMINEE'";



    @Override
    public boolean creer(Avis avis) {
        if (avis == null || avis.getIdReservation() <= 0 || avis.getIdAuteur() <= 0) {
            return false;
        }

       
        if (existePourReservation(avis.getIdReservation())) {
            System.err.println("[AvisDAO] Avis déjà existant pour la réservation " + avis.getIdReservation());
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CREER, Statement.RETURN_GENERATED_KEYS)) {

            requete.setInt(1, avis.getIdReservation());
            requete.setInt(2, avis.getIdMaison());
            requete.setInt(3, avis.getIdAuteur());
            requete.setInt(4, avis.getNoteProprete());
            requete.setInt(5, avis.getNoteCommunication());
            requete.setInt(6, avis.getNoteEmplacement());
            requete.setInt(7, avis.getNoteRapportQualitePrix());
            requete.setString(8, avis.getCommentaire());

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                ResultSet rs = requete.getGeneratedKeys();
                if (rs.next()) {
                    avis.setIdAvis(rs.getInt(1));
                }
                System.out.println("[AvisDAO] Avis créé pour la réservation " + avis.getIdReservation());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[AvisDAO] Erreur creer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean existePourReservation(int idReservation) {
        if (idReservation <= 0) return false;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_EXISTE_POUR_RESERVATION)) {

            requete.setInt(1, idReservation);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[AvisDAO] Erreur existePourReservation : " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Avis> trouverParMaison(int idMaison) {
        List<Avis> avis = new ArrayList<>();

        if (idMaison <= 0) return avis;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_MAISON)) {

            requete.setInt(1, idMaison);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    avis.add(mapperAvis(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[AvisDAO] Erreur trouverParMaison : " + e.getMessage());
        }

        return avis;
    }

    @Override
    public Avis trouverParReservation(int idReservation) {
        if (idReservation <= 0) return null;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_RESERVATION)) {

            requete.setInt(1, idReservation);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return mapperAvis(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[AvisDAO] Erreur trouverParReservation : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean peutDonnerAvis(int idReservation, int idLocataire) {
        if (idReservation <= 0 || idLocataire <= 0) return false;

        // Vérifier que la réservation est terminée et appartient au locataire
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_PEUT_DONNER_AVIS)) {

            requete.setInt(1, idReservation);
            requete.setInt(2, idLocataire);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Vérifier aussi qu'il n'y a pas déjà un avis
                    return !existePourReservation(idReservation);
                }
            }

        } catch (SQLException e) {
            System.err.println("[AvisDAO] Erreur peutDonnerAvis : " + e.getMessage());
        }

        return false;
    }

   

    private Avis mapperAvis(ResultSet rs) throws SQLException {
        Avis avis = new Avis();

        avis.setIdAvis(rs.getInt("id_avis"));
        avis.setIdReservation(rs.getInt("id_reservation"));
        avis.setIdMaison(rs.getInt("id_maison"));
        avis.setIdAuteur(rs.getInt("id_auteur"));
        avis.setNoteProprete(rs.getInt("note_proprete"));
        avis.setNoteCommunication(rs.getInt("note_communication"));
        avis.setNoteEmplacement(rs.getInt("note_emplacement"));
        avis.setNoteRapportQualitePrix(rs.getInt("note_rapport_qualite_prix"));
        avis.setNoteMoyenne(rs.getDouble("note_moyenne"));
        avis.setCommentaire(rs.getString("commentaire"));

        Timestamp datePublication = rs.getTimestamp("date_publication");
        if (datePublication != null) {
            avis.setDatePublication(datePublication);
        }

      
        try {
            avis.setAuteurNom(rs.getString("auteur_nom"));
            avis.setAuteurPrenom(rs.getString("auteur_prenom"));
        } catch (SQLException e) {
            // Colonnes pas toujours présentes selon la requête
        }

        return avis;
    }
    
    
    private static final String SQL_TROUVER_PAR_PROPRIETAIRE =
        "SELECT a.*, u.nom AS auteur_nom, u.prenom AS auteur_prenom, " +
        "m.titre AS maison_titre " +
        "FROM avis a " +
        "JOIN maisons m ON a.id_maison = m.id_maison " +
        "JOIN utilisateurs u ON a.id_auteur = u.id_utilisateur " +
        "WHERE m.id_proprietaire = ? " +
        "ORDER BY a.date_publication DESC " +
        "LIMIT 5";

    @Override
    public List<Avis> trouverParProprietaire(int idProprietaire) {
        List<Avis> avis = new ArrayList<>();
        if (idProprietaire <= 0) return avis;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_PROPRIETAIRE)) {
            requete.setInt(1, idProprietaire);
            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    Avis a = mapperAvis(resultat);
                    try { a.setMaisonTitre(resultat.getString("maison_titre")); } catch (SQLException e) {}
                    avis.add(a);
                }
            }
        } catch (SQLException e) {
            System.err.println("[AvisDAO] Erreur trouverParProprietaire : " + e.getMessage());
        }
        return avis;
    }
}