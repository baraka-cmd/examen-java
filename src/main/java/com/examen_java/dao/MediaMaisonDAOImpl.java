package com.examen_java.dao;

import com.examen_java.modele.MediaMaison;
import com.examen_java.utils.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MediaMaisonDAOImpl implements MediaMaisonDAO {

    

    private static final String SQL_TROUVER_PAR_MAISON =
            "SELECT * FROM medias WHERE id_maison = ? ORDER BY ordre_affichage ASC";

    private static final String SQL_TROUVER_PHOTO_PRINCIPALE =
            "SELECT * FROM medias WHERE id_maison = ? AND est_image_principale = TRUE LIMIT 1";

    private static final String SQL_AJOUTER =
            "INSERT INTO medias (id_maison, chemin_fichier, legende, est_image_principale, ordre_affichage) " +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SUPPRIMER =
            "DELETE FROM medias WHERE id_media = ?";

    private static final String SQL_SUPPRIMER_PAR_MAISON =
            "DELETE FROM medias WHERE id_maison = ?";

    private static final String SQL_RESET_PRINCIPALE =
            "UPDATE medias SET est_image_principale = FALSE WHERE id_maison = ?";

    private static final String SQL_DEFINIR_PRINCIPALE =
            "UPDATE medias SET est_image_principale = TRUE WHERE id_media = ? AND id_maison = ?";

    

    @Override
    public List<MediaMaison> trouverParMaison(int idMaison) {
        List<MediaMaison> medias = new ArrayList<>();

        if (idMaison <= 0) {
            return medias;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_MAISON)) {

            requete.setInt(1, idMaison);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    medias.add(mapperMedia(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[MediaDAO] Erreur trouverParMaison : " + e.getMessage());
        }

        return medias;
    }

    @Override
    public MediaMaison trouverPhotoPrincipale(int idMaison) {
        if (idMaison <= 0) {
            return null;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PHOTO_PRINCIPALE)) {

            requete.setInt(1, idMaison);

            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    return mapperMedia(resultat);
                }
            }

        } catch (SQLException e) {
            System.err.println("[MediaDAO] Erreur trouverPhotoPrincipale : " + e.getMessage());
        }

        return null;
    }

    @Override
    public boolean ajouter(MediaMaison media) {
        if (media == null || media.getIdMaison() <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_AJOUTER)) {

            requete.setInt(1, media.getIdMaison());
            requete.setString(2, media.getCheminFichier());
            requete.setString(3, media.getLegende());
            requete.setBoolean(4, media.isEstImagePrincipale());
            requete.setInt(5, media.getOrdreAffichage());

            return requete.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MediaDAO] Erreur ajouter : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean supprimer(int idMedia) {
        if (idMedia <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_SUPPRIMER)) {

            requete.setInt(1, idMedia);
            return requete.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MediaDAO] Erreur supprimer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean supprimerParMaison(int idMaison) {
        if (idMaison <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_SUPPRIMER_PAR_MAISON)) {

            requete.setInt(1, idMaison);
            return requete.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[MediaDAO] Erreur supprimerParMaison : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean definirCommePrincipale(int idMedia, int idMaison) {
        if (idMedia <= 0 || idMaison <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection()) {
            // Désactiver l'auto-commit pour la transaction
            connexion.setAutoCommit(false);

            try {
                // Étape 1 : Réinitialiser toutes les photos de la maison
                PreparedStatement reset = connexion.prepareStatement(SQL_RESET_PRINCIPALE);
                reset.setInt(1, idMaison);
                reset.executeUpdate();

                // Étape 2 : Définir la nouvelle photo principale
                PreparedStatement definir = connexion.prepareStatement(SQL_DEFINIR_PRINCIPALE);
                definir.setInt(1, idMedia);
                definir.setInt(2, idMaison);
                int lignes = definir.executeUpdate();

                // Valider la transaction
                connexion.commit();

                return lignes > 0;

            } catch (SQLException e) {
                // Annuler en cas d'erreur
                connexion.rollback();
                System.err.println("[MediaDAO] Erreur definirCommePrincipale : " + e.getMessage());
            } finally {
                connexion.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("[MediaDAO] Erreur connexion definirCommePrincipale : " + e.getMessage());
        }

        return false;
    }

    

    private MediaMaison mapperMedia(ResultSet rs) throws SQLException {
        MediaMaison media = new MediaMaison();

        media.setIdMedia(rs.getInt("id_media"));
        media.setIdMaison(rs.getInt("id_maison"));
        media.setCheminFichier(rs.getString("chemin_fichier"));
        media.setLegende(rs.getString("legende"));
        media.setEstImagePrincipale(rs.getBoolean("est_image_principale"));
        media.setOrdreAffichage(rs.getInt("ordre_affichage"));

        return media;
    }
}