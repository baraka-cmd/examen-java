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


public class RechercheDAOImpl implements RechercheDAO {

   
    private static final String SQL_RECHERCHER =
            "SELECT DISTINCT m.*, " +
            "(SELECT chemin_fichier FROM medias WHERE id_maison = m.id_maison AND est_image_principale = TRUE LIMIT 1) AS photo_principale, " +
            "COALESCE((SELECT AVG(note_moyenne) FROM avis WHERE id_maison = m.id_maison), 0) AS note_moyenne, " +
            "(SELECT COUNT(*) FROM avis WHERE id_maison = m.id_maison) AS nombre_avis " +
            "FROM maisons m " +
            "WHERE m.est_visible = TRUE ";

    private static final String SQL_FILTRE_VILLE =
            "AND m.ville LIKE ? ";

    private static final String SQL_FILTRE_CAPACITE =
            "AND m.capacite_personnes >= ? ";

    private static final String SQL_FILTRE_PRIX =
            "AND m.prix_base_nuit <= ? ";

    private static final String SQL_FILTRE_DISPONIBILITE =
            "AND m.id_maison NOT IN (" +
            "   SELECT r.id_maison FROM reservations r " +
            "   WHERE r.statut IN ('EN_ATTENTE_PAIEMENT', 'CONFIRMEE') " +
            "   AND r.date_debut < ? AND r.date_fin > ?" +
            ") ";

    private static final String SQL_ORDER_BY =
            "ORDER BY m.date_creation DESC";

    private static final String SQL_DERNIERES_MAISONS =
            "SELECT m.*, " +
            "(SELECT chemin_fichier FROM medias WHERE id_maison = m.id_maison AND est_image_principale = TRUE LIMIT 1) AS photo_principale, " +
            "COALESCE((SELECT AVG(note_moyenne) FROM avis WHERE id_maison = m.id_maison), 0) AS note_moyenne, " +
            "(SELECT COUNT(*) FROM avis WHERE id_maison = m.id_maison) AS nombre_avis " +
            "FROM maisons m " +
            "WHERE m.est_visible = TRUE " +
            "ORDER BY m.date_creation DESC LIMIT ?";

    private static final String SQL_COMPTER_RESULTATS =
            "SELECT COUNT(DISTINCT m.id_maison) " +
            "FROM maisons m " +
            "WHERE m.est_visible = TRUE ";

    

    @Override
    public List<Maison> rechercher(String ville, String dateDebut, String dateFin,
                                    int capacite, double prixMax) {
        List<Maison> maisons = new ArrayList<>();

        StringBuilder sql = new StringBuilder(SQL_RECHERCHER);

        
        if (ville != null && !ville.trim().isEmpty()) {
            sql.append(SQL_FILTRE_VILLE);
        }
        if (capacite > 0) {
            sql.append(SQL_FILTRE_CAPACITE);
        }
        if (prixMax > 0) {
            sql.append(SQL_FILTRE_PRIX);
        }
        if (dateDebut != null && !dateDebut.trim().isEmpty()
                && dateFin != null && !dateFin.trim().isEmpty()) {
            sql.append(SQL_FILTRE_DISPONIBILITE);
        }
        sql.append(SQL_ORDER_BY);

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql.toString())) {

            int index = 1;

            if (ville != null && !ville.trim().isEmpty()) {
                requete.setString(index++, "%" + ville.trim() + "%");
            }
            if (capacite > 0) {
                requete.setInt(index++, capacite);
            }
            if (prixMax > 0) {
                requete.setDouble(index++, prixMax);
            }
            if (dateDebut != null && !dateDebut.trim().isEmpty()
                    && dateFin != null && !dateFin.trim().isEmpty()) {
                requete.setString(index++, dateFin.trim());
                requete.setString(index++, dateDebut.trim());
            }

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    maisons.add(mapperMaison(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[RechercheDAO] Erreur rechercher : " + e.getMessage());
        }

        return maisons;
    }

    @Override
    public List<Maison> trouverDernieresMaisons(int limite) {
        List<Maison> maisons = new ArrayList<>();

        if (limite <= 0) limite = 6;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_DERNIERES_MAISONS)) {

            requete.setInt(1, limite);

            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    maisons.add(mapperMaison(resultat));
                }
            }

        } catch (SQLException e) {
            System.err.println("[RechercheDAO] Erreur trouverDernieresMaisons : " + e.getMessage());
        }

        return maisons;
    }

    @Override
    public int compterResultats(String ville, int capacite, double prixMax) {
        StringBuilder sql = new StringBuilder(SQL_COMPTER_RESULTATS);

        if (ville != null && !ville.trim().isEmpty()) {
            sql.append(SQL_FILTRE_VILLE);
        }
        if (capacite > 0) {
            sql.append(SQL_FILTRE_CAPACITE);
        }
        if (prixMax > 0) {
            sql.append(SQL_FILTRE_PRIX);
        }

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(sql.toString())) {

            int index = 1;

            if (ville != null && !ville.trim().isEmpty()) {
                requete.setString(index++, "%" + ville.trim() + "%");
            }
            if (capacite > 0) {
                requete.setInt(index++, capacite);
            }
            if (prixMax > 0) {
                requete.setDouble(index++, prixMax);
            }

            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RechercheDAO] Erreur compterResultats : " + e.getMessage());
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