package com.examen_java.dao;

import com.examen_java.modele.ProfilInscription;
import com.examen_java.modele.UtilisateurInscription;
import com.examen_java.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AuthentificationDAOImpl implements AuthentificationDAO {

    private static final String SQL_TROUVER_PAR_EMAIL =
            "SELECT id_utilisateur, email, mot_de_passe_hash, nom, prenom, " +
            "telephone, date_inscription, email_verifie, est_actif " +
            "FROM utilisateurs WHERE email = ?";

    private static final String SQL_TROUVER_PROFILS =
            "SELECT id_profil, id_utilisateur, role, date_attribution, est_verifie " +
            "FROM profils WHERE id_utilisateur = ?";

    private static final String SQL_MAJ_DERNIERE_CONNEXION =
            "UPDATE utilisateurs SET derniere_connexion = ? WHERE id_utilisateur = ?";

    private static final String SQL_EST_COMPTE_ACTIF =
            "SELECT est_actif FROM utilisateurs WHERE email = ?";

    @Override
    public UtilisateurInscription trouverParEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        UtilisateurInscription utilisateur = null;
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PAR_EMAIL)) {
            requete.setString(1, email.toLowerCase().trim());
            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    utilisateur = new UtilisateurInscription();
                    utilisateur.setIdUtilisateur(resultat.getInt("id_utilisateur"));
                    utilisateur.setEmail(resultat.getString("email"));
                    utilisateur.setMotDePasseHash(resultat.getString("mot_de_passe_hash"));
                    utilisateur.setNom(resultat.getString("nom"));
                    utilisateur.setPrenom(resultat.getString("prenom"));
                    utilisateur.setTelephone(resultat.getString("telephone"));
                    utilisateur.setDateInscription(resultat.getTimestamp("date_inscription"));
                    utilisateur.setEmailVerifie(resultat.getBoolean("email_verifie"));
                    utilisateur.setEstActif(resultat.getBoolean("est_actif"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur trouverParEmail : " + e.getMessage());
        }
        return utilisateur;
    }

    @Override
    public List<ProfilInscription> trouverProfilsParUtilisateur(int idUtilisateur) {
        List<ProfilInscription> profils = new ArrayList<>();
        if (idUtilisateur <= 0) return profils;
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_TROUVER_PROFILS)) {
            requete.setInt(1, idUtilisateur);
            try (ResultSet resultat = requete.executeQuery()) {
                while (resultat.next()) {
                    ProfilInscription profil = new ProfilInscription();
                    profil.setIdProfil(resultat.getInt("id_profil"));
                    profil.setIdUtilisateur(resultat.getInt("id_utilisateur"));
                    profil.setRoleString(resultat.getString("role"));
                    profil.setDateAttribution(resultat.getTimestamp("date_attribution"));
                    profil.setEstVerifie(resultat.getBoolean("est_verifie"));
                    profils.add(profil);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur trouverProfils : " + e.getMessage());
        }
        return profils;
    }

    @Override
    public void mettreAJourDerniereConnexion(int idUtilisateur) {
        if (idUtilisateur <= 0) return;
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_MAJ_DERNIERE_CONNEXION)) {
            requete.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            requete.setInt(2, idUtilisateur);
            requete.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur miseAJourDerniereConnexion : " + e.getMessage());
        }
    }

    @Override
    public boolean estCompteActif(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement requete = connexion.prepareStatement(SQL_EST_COMPTE_ACTIF)) {
            requete.setString(1, email.toLowerCase().trim());
            try (ResultSet resultat = requete.executeQuery()) {
                if (resultat.next()) {
                    return resultat.getBoolean("est_actif");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur estCompteActif : " + e.getMessage());
        }
        return false;
    }
    
    
    private UtilisateurInscription mapperUtilisateur(ResultSet rs) throws SQLException {
    UtilisateurInscription utilisateur = new UtilisateurInscription();

    utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
    utilisateur.setEmail(rs.getString("email"));
    utilisateur.setMotDePasseHash(rs.getString("mot_de_passe_hash"));
    utilisateur.setNom(rs.getString("nom"));
    utilisateur.setPrenom(rs.getString("prenom"));
    utilisateur.setTelephone(rs.getString("telephone"));
    utilisateur.setDateInscription(rs.getTimestamp("date_inscription"));
    utilisateur.setEmailVerifie(rs.getBoolean("email_verifie"));
    utilisateur.setEstActif(rs.getBoolean("est_actif"));

    return utilisateur;
}
    
    @Override
public UtilisateurInscription trouverParId(int idUtilisateur) {
    if (idUtilisateur <= 0) return null;

    String sql = "SELECT * FROM utilisateurs WHERE id_utilisateur = ?";

    try (Connection connexion = DatabaseConnection.getConnection();
         PreparedStatement requete = connexion.prepareStatement(sql)) {

        requete.setInt(1, idUtilisateur);

        try (ResultSet rs = requete.executeQuery()) {
            if (rs.next()) {
                return mapperUtilisateur(rs);
            }
        }

    } catch (SQLException e) {
        System.err.println("[AuthentificationDAO] Erreur trouverParId : " + e.getMessage());
    }

    return null;
}

@Override
public boolean mettreAJour(UtilisateurInscription utilisateur) {
    if (utilisateur == null || utilisateur.getIdUtilisateur() <= 0) return false;

    String sql = "UPDATE utilisateurs SET nom = ?, prenom = ?, telephone = ? WHERE id_utilisateur = ?";

    try (Connection connexion = DatabaseConnection.getConnection();
         PreparedStatement requete = connexion.prepareStatement(sql)) {

        requete.setString(1, utilisateur.getNom());
        requete.setString(2, utilisateur.getPrenom());
        requete.setString(3, utilisateur.getTelephone());
        requete.setInt(4, utilisateur.getIdUtilisateur());

        return requete.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("[AuthentificationDAO] Erreur mettreAJour : " + e.getMessage());
    }

    return false;
}

@Override
public boolean mettreAJourMotDePasse(UtilisateurInscription utilisateur) {
    if (utilisateur == null || utilisateur.getIdUtilisateur() <= 0) return false;

    String sql = "UPDATE utilisateurs SET mot_de_passe_hash = ? WHERE id_utilisateur = ?";

    try (Connection connexion = DatabaseConnection.getConnection();
         PreparedStatement requete = connexion.prepareStatement(sql)) {

        requete.setString(1, utilisateur.getMotDePasseHash());
        requete.setInt(2, utilisateur.getIdUtilisateur());

        return requete.executeUpdate() > 0;

    } catch (SQLException e) {
        System.err.println("[AuthentificationDAO] Erreur mettreAJourMotDePasse : " + e.getMessage());
    }

    return false;
}
}