package com.examen_java.dao;

import com.examen_java.modele.ProfilInscription;
import com.examen_java.modele.UtilisateurInscription;
import com.examen_java.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class InscriptionDAOImpl implements InscriptionDAO {

    
    private static final String SQL_EMAIL_EXISTE =
            "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";

    
    private static final String SQL_CREER_UTILISATEUR =
            "INSERT INTO utilisateurs (email, mot_de_passe_hash, nom, prenom, telephone) " +
            "VALUES (?, ?, ?, ?, ?)";

    
    private static final String SQL_CREER_PROFIL =
            "INSERT INTO profils (id_utilisateur, role, est_verifie) " +
            "VALUES (?, ?, ?)";

   
    @Override
    public boolean emailExiste(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_EMAIL_EXISTE)) {

            stmt.setString(1, email.toLowerCase().trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'email : " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

   
    @Override
    public int creerUtilisateur(UtilisateurInscription utilisateur) {
        int idGenere = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_CREER_UTILISATEUR, Statement.RETURN_GENERATED_KEYS)) {

            
            stmt.setString(1, utilisateur.getEmail().toLowerCase().trim());  
            stmt.setString(2, utilisateur.getMotDePasseHash());              
            stmt.setString(3, utilisateur.getNom());                        
            stmt.setString(4, utilisateur.getPrenom());                     
            stmt.setString(5, utilisateur.getTelephone());           

            int lignesAffectees = stmt.executeUpdate();

            if (lignesAffectees > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGenere = rs.getInt(1);
                    utilisateur.setIdUtilisateur(idGenere);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
        return idGenere;
    }

    
    @Override
    public void creerProfil(ProfilInscription profil) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_CREER_PROFIL)) {

            
            stmt.setInt(1, profil.getIdUtilisateur());     
            stmt.setString(2, profil.getRoleString());      
            stmt.setBoolean(3, profil.isEstVerifie());     

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du profil : " + e.getMessage());
            e.printStackTrace();
        }
    }
}