package com.examen_java.dao;

import com.examen_java.modele.Maison;
import com.examen_java.modele.MediaMaison;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class FavoriDAOImpl implements FavoriDAO {

    

    private static final String SQL_AJOUTER =
            "INSERT INTO favoris (id_utilisateur, id_maison) VALUES (?, ?)";

    private static final String SQL_SUPPRIMER =
            "DELETE FROM favoris WHERE id_utilisateur = ? AND id_maison = ?";

    private static final String SQL_EST_FAVORI =
            "SELECT COUNT(*) FROM favoris WHERE id_utilisateur = ? AND id_maison = ?";

    private static final String SQL_TROUVER_PAR_UTILISATEUR =
            "SELECT m.*, " +
            "(SELECT chemin_fichier FROM medias WHERE id_maison = m.id_maison AND est_image_principale = TRUE LIMIT 1) AS photo_principale " +
            "FROM favoris f " +
            "JOIN maisons m ON f.id_maison = m.id_maison " +
            "WHERE f.id_utilisateur = ? AND m.est_visible = TRUE " +
            "ORDER BY f.date_ajout DESC";

    private static final String SQL_COMPTER =
            "SELECT COUNT(*) FROM favoris WHERE id_utilisateur = ?";

    

    @Override
    public boolean ajouter(int idUtilisateur, int idMaison) {
        if (idUtilisateur <= 0 || idMaison <= 0) {
            return false;
        }

      
        if (estFavori(idUtilisateur, idMaison)) {
            return true; 
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_AJOUTER)) {

            requete.setInt(1, idUtilisateur);
            requete.setInt(2, idMaison);

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[FavoriDAO] Favori ajouté - Utilisateur: " 
                        + idUtilisateur + ", Maison: " + idMaison);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[FavoriDAO] Erreur ajouter : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean supprimer(int idUtilisateur, int idMaison) {
        if (idUtilisateur <= 0 || idMaison <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_SUPPRIMER)) {

            requete.setInt(1, idUtilisateur);
            requete.setInt(2, idMaison);

            int lignes = requete.executeUpdate();

            if (lignes > 0) {
                System.out.println("[FavoriDAO] Favori supprimé - Utilisateur: " 
                        + idUtilisateur + ", Maison: " + idMaison);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[FavoriDAO] Erreur supprimer : " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean estFavori(int idUtilisateur, int idMaison) {
        if (idUtilisateur <= 0 || idMaison <= 0) {
            return false;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_EST_FAVORI)) {

            requete.setInt(1, idUtilisateur);
            requete.setInt(2, idMaison);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[FavoriDAO] Erreur estFavori : " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Maison> trouverParUtilisateur(int idUtilisateur) {
        List<Maison> maisons = new ArrayList<>();

        if (idUtilisateur <= 0) {
            return maisons;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_UTILISATEUR)) {

            requete.setInt(1, idUtilisateur);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    maisons.add(mapperMaison(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[FavoriDAO] Erreur trouverParUtilisateur : " + e.getMessage());
        }

        return maisons;
    }

    @Override
    public int compter(int idUtilisateur) {
        if (idUtilisateur <= 0) {
            return 0;
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_COMPTER)) {

            requete.setInt(1, idUtilisateur);

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[FavoriDAO] Erreur compter : " + e.getMessage());
        }

        return 0;
    }

   

    private Maison mapperMaison(ResultSet rs) throws SQLException {
        Maison maison = new Maison();

        maison.setIdMaison(rs.getInt("id_maison"));
        maison.setIdProprietaire(rs.getInt("id_proprietaire"));
        maison.setTitre(rs.getString("titre"));
        maison.setDescription(rs.getString("description"));
        maison.setPrixBaseNuit(rs.getDouble("prix_base_nuit"));
        maison.setCapacitePersonnes(rs.getInt("capacite_personnes"));
        maison.setNombreChambres(rs.getInt("nombre_chambres"));
        maison.setNombreSallesBain(rs.getInt("nombre_salles_bain"));
        maison.setSuperficieMetresCarres(rs.getInt("superficie_metres_carres"));
        maison.setAdresseLigne1(rs.getString("adresse_ligne1"));
        maison.setAdresseLigne2(rs.getString("adresse_ligne2"));
        maison.setVille(rs.getString("ville"));
        maison.setCodePostal(rs.getString("code_postal"));
        maison.setPays(rs.getString("pays"));
        maison.setEquipements(rs.getString("equipements"));
        maison.setReglementInterieur(rs.getString("reglement_interieur"));
        maison.setEstVisible(rs.getBoolean("est_visible"));

        Timestamp dateCreation = rs.getTimestamp("date_creation");
        if (dateCreation != null) {
            maison.setDateCreation(dateCreation);
        }

        
        String photoPrincipale = rs.getString("photo_principale");
        if (photoPrincipale != null) {
            MediaMaison media = new MediaMaison();
            media.setCheminFichier(photoPrincipale);
            media.setEstImagePrincipale(true);
            maison.setPhotoPrincipale(media);
        }

        return maison;
    }
}