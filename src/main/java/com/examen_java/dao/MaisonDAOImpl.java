package com.examen_java.dao;

import com.examen_java.modele.Maison;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class MaisonDAOImpl implements MaisonDAO {

    private static final String SQL_TROUVER_PAR_PROPRIETAIRE =
            "SELECT * FROM maisons WHERE id_proprietaire = ? ORDER BY date_creation DESC";

    private static final String SQL_TROUVER_PAR_ID =
            "SELECT * FROM maisons WHERE id_maison = ?";

    private static final String SQL_CREER =
            "INSERT INTO maisons (id_proprietaire, titre, description, prix_base_nuit, " +
            "capacite_personnes, nombre_chambres, nombre_salles_bain, superficie_metres_carres, " +
            "adresse_ligne1, adresse_ligne2, ville, code_postal, pays, equipements, " +
            "reglement_interieur, est_visible) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_MODIFIER =
            "UPDATE maisons SET titre = ?, description = ?, prix_base_nuit = ?, " +
            "capacite_personnes = ?, nombre_chambres = ?, nombre_salles_bain = ?, " +
            "superficie_metres_carres = ?, adresse_ligne1 = ?, adresse_ligne2 = ?, " +
            "ville = ?, code_postal = ?, pays = ?, equipements = ?, " +
            "reglement_interieur = ?, date_modification = CURRENT_TIMESTAMP " +
            "WHERE id_maison = ? AND id_proprietaire = ?";

    private static final String SQL_DESACTIVER =
            "UPDATE maisons SET est_visible = FALSE WHERE id_maison = ? AND id_proprietaire = ?";

    private static final String SQL_APPARTIENT_A =
            "SELECT COUNT(*) FROM maisons WHERE id_maison = ? AND id_proprietaire = ?";

    private static final String SQL_COMPTER_ACTIVES =
            "SELECT COUNT(*) FROM maisons WHERE id_proprietaire = ? AND est_visible = TRUE";

    @Override
    public List<Maison> trouverParProprietaire(int idProprietaire) {
        List<Maison> maisons = new ArrayList<>();
        if (idProprietaire <= 0) return maisons;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_PROPRIETAIRE)) {
            requete.setInt(1, idProprietaire);
            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    maisons.add(mapperMaison(resultat));
                }
            }
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur trouverParProprietaire : " + e.getMessage());
        }
        return maisons;
    }

    @Override
    public Maison trouverParId(int idMaison) {
        if (idMaison <= 0) return null;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_ID)) {
            requete.setInt(1, idMaison);
            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    return mapperMaison(resultat);
                }
            }
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur trouverParId : " + e.getMessage());
        }
        return null;
    }

    @Override
    public int creer(Maison maison) {
        if (maison == null) return 0;

        int idGenere = 0;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_CREER, Statement.RETURN_GENERATED_KEYS)) {

            requete.setInt(1, maison.getIdProprietaire());
            requete.setString(2, maison.getTitre());
            requete.setString(3, maison.getDescription());
            requete.setDouble(4, maison.getPrixBaseNuit());
            requete.setInt(5, maison.getCapacitePersonnes());
            requete.setInt(6, maison.getNombreChambres());
            requete.setInt(7, maison.getNombreSallesBain());
            requete.setInt(8, maison.getSuperficieMetresCarres());
            requete.setString(9, maison.getAdresseLigne1());
            requete.setString(10, maison.getAdresseLigne2());
            requete.setString(11, maison.getVille());
            requete.setString(12, maison.getCodePostal());
            requete.setString(13, maison.getPays());

            
            String eq = maison.getEquipements();
            if (eq == null || eq.trim().isEmpty()) {
                eq = "[]";
            }
            requete.setString(14, eq);

            requete.setString(15, maison.getReglementInterieur());
            requete.setBoolean(16, maison.isEstVisible());

            int lignesAffectees = requete.executeUpdate();

            if (lignesAffectees > 0) {
                ResultSet rs = requete.getGeneratedKeys();
                if (rs.next()) {
                    idGenere = rs.getInt(1);
                    maison.setIdMaison(idGenere);
                }
            }
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur creer : " + e.getMessage());
        }
        return idGenere;
    }

    @Override
    public boolean modifier(Maison maison) {
        if (maison == null || maison.getIdMaison() <= 0) return false;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_MODIFIER)) {

            requete.setString(1, maison.getTitre());
            requete.setString(2, maison.getDescription());
            requete.setDouble(3, maison.getPrixBaseNuit());
            requete.setInt(4, maison.getCapacitePersonnes());
            requete.setInt(5, maison.getNombreChambres());
            requete.setInt(6, maison.getNombreSallesBain());
            requete.setInt(7, maison.getSuperficieMetresCarres());
            requete.setString(8, maison.getAdresseLigne1());
            requete.setString(9, maison.getAdresseLigne2());
            requete.setString(10, maison.getVille());
            requete.setString(11, maison.getCodePostal());
            requete.setString(12, maison.getPays());

            String eq = maison.getEquipements();
            if (eq == null || eq.trim().isEmpty()) eq = "[]";
            requete.setString(13, eq);

            requete.setString(14, maison.getReglementInterieur());
            requete.setInt(15, maison.getIdMaison());
            requete.setInt(16, maison.getIdProprietaire());

            return requete.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur modifier : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean desactiver(int idMaison, int idProprietaire) {
        if (idMaison <= 0 || idProprietaire <= 0) return false;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_DESACTIVER)) {
            requete.setInt(1, idMaison);
            requete.setInt(2, idProprietaire);
            return requete.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur desactiver : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean appartientA(int idMaison, int idProprietaire) {
        if (idMaison <= 0 || idProprietaire <= 0) return false;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_APPARTIENT_A)) {
            requete.setInt(1, idMaison);
            requete.setInt(2, idProprietaire);
            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur appartientA : " + e.getMessage());
        }
        return false;
    }

    @Override
    public int compterActives(int idProprietaire) {
        if (idProprietaire <= 0) return 0;

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_COMPTER_ACTIVES)) {
            requete.setInt(1, idProprietaire);
            try (ResultSet rs = requete.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[MaisonDAO] Erreur compterActives : " + e.getMessage());
        }
        return 0;
    }
    
    @Override
public List<Maison> trouverDernieresMaisons(int limite) {
    List<Maison> maisons = new ArrayList<>();
    
    if (limite <= 0) limite = 6;
    
    String sql = "SELECT m.*, " +
            "(SELECT chemin_fichier FROM medias WHERE id_maison = m.id_maison AND est_image_principale = TRUE LIMIT 1) AS photo_principale " +
            "FROM maisons m " +
            "WHERE m.est_visible = TRUE " +
            "ORDER BY m.date_creation DESC LIMIT ?";

    try (Connection connexion = DatabaseConnection.getConnection();
         PreparedStatement requete = connexion.prepareStatement(sql)) {

        requete.setInt(1, limite);

        try (ResultSet resultat = requete.executeQuery()) {
            while (resultat.next()) {
                maisons.add(mapperMaison(resultat));
            }
        }

    } catch (SQLException e) {
        System.err.println("[MaisonDAO] Erreur trouverDernieresMaisons : " + e.getMessage());
    }

    return maisons;
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
        if (dateCreation != null) maison.setDateCreation(dateCreation);
        return maison;
    }
}