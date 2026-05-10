package com.examen_java.controller;

import com.examen_java.dao.AvisDAO;
import com.examen_java.dao.AvisDAOImpl;
import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;
import com.examen_java.dao.MediaMaisonDAO;
import com.examen_java.dao.MediaMaisonDAOImpl;
import com.examen_java.modele.Avis;
import com.examen_java.modele.Maison;
import com.examen_java.modele.MediaMaison;
import com.examen_java.utils.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AccueilServlet extends HttpServlet {

    private MaisonDAO maisonDAO;
    private MediaMaisonDAO mediaMaisonDAO;
    private AvisDAO avisDAO;

    @Override
    public void init() throws ServletException {
        this.maisonDAO = new MaisonDAOImpl();
        this.mediaMaisonDAO = new MediaMaisonDAOImpl();
        this.avisDAO = new AvisDAOImpl();
        System.out.println("[AccueilServlet] Initialisée.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Maison> dernieresMaisons = maisonDAO.trouverDernieresMaisons(6);
        if (dernieresMaisons != null) {
            for (Maison m : dernieresMaisons) {
                MediaMaison photo = mediaMaisonDAO.trouverPhotoPrincipale(m.getIdMaison());
                m.setPhotoPrincipale(photo);
            }
        }

        
        List<Maison> topMaisons = trouverTopMaisons(3);
        if (topMaisons != null) {
            for (Maison m : topMaisons) {
                MediaMaison photo = mediaMaisonDAO.trouverPhotoPrincipale(m.getIdMaison());
                m.setPhotoPrincipale(photo);
            }
        }

        
        List<String[]> villesPopulaires = trouverVillesPopulaires(6);

        
        List<Avis> avisRecents = trouverAvisRecents(3);

        
        int totalMaisons = compterTotalMaisons();
        int totalVilles = compterTotalVilles();
        int totalAvis = compterTotalAvis();
        double noteGlobale = calculerNoteGlobale();

       
        HttpSession session = request.getSession(false);
        boolean estConnecte = (session != null && session.getAttribute("idUtilisateur") != null);
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        
        request.setAttribute("dernieresMaisons", dernieresMaisons);
        request.setAttribute("topMaisons", topMaisons);
        request.setAttribute("villesPopulaires", villesPopulaires);
        request.setAttribute("avisRecents", avisRecents);
        request.setAttribute("totalMaisons", totalMaisons);
        request.setAttribute("totalVilles", totalVilles);
        request.setAttribute("totalAvis", totalAvis);
        request.setAttribute("noteGlobale", noteGlobale);
        request.setAttribute("estConnecte", estConnecte);
        request.setAttribute("role", role);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

   

    
    private List<Maison> trouverTopMaisons(int limite) {
        List<Maison> top = new ArrayList<>();
        String sql = "SELECT m.*, COALESCE(AVG(a.note_moyenne), 0) AS note_avg, COUNT(a.id_avis) AS nb_avis " +
                     "FROM maisons m " +
                     "LEFT JOIN avis a ON m.id_maison = a.id_maison " +
                     "WHERE m.est_visible = TRUE " +
                     "GROUP BY m.id_maison " +
                     "HAVING COUNT(a.id_avis) > 0 " +
                     "ORDER BY note_avg DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Maison m = new Maison();
                m.setIdMaison(rs.getInt("id_maison"));
                m.setTitre(rs.getString("titre"));
                m.setVille(rs.getString("ville"));
                m.setPays(rs.getString("pays"));
                m.setPrixBaseNuit(rs.getDouble("prix_base_nuit"));
                m.setCapacitePersonnes(rs.getInt("capacite_personnes"));
                m.setNombreChambres(rs.getInt("nombre_chambres"));
                top.add(m);
            }
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur trouverTopMaisons : " + e.getMessage());
        }
        return top;
    }

    
    private List<String[]> trouverVillesPopulaires(int limite) {
        List<String[]> villes = new ArrayList<>();
        String sql = "SELECT ville, COUNT(*) AS nb FROM maisons WHERE est_visible = TRUE " +
                     "GROUP BY ville ORDER BY nb DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                villes.add(new String[]{rs.getString("ville"), String.valueOf(rs.getInt("nb"))});
            }
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur trouverVillesPopulaires : " + e.getMessage());
        }
        return villes;
    }

    
    private List<Avis> trouverAvisRecents(int limite) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT a.*, u.nom AS auteur_nom, u.prenom AS auteur_prenom, m.titre AS maison_titre " +
                     "FROM avis a " +
                     "JOIN utilisateurs u ON a.id_auteur = u.id_utilisateur " +
                     "JOIN maisons m ON a.id_maison = m.id_maison " +
                     "WHERE a.commentaire IS NOT NULL AND a.commentaire != '' " +
                     "ORDER BY a.date_publication DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Avis avis = new Avis();
                avis.setIdAvis(rs.getInt("id_avis"));
                avis.setNoteMoyenne(rs.getDouble("note_moyenne"));
                avis.setCommentaire(rs.getString("commentaire"));
                avis.setAuteurNom(rs.getString("auteur_nom"));
                avis.setAuteurPrenom(rs.getString("auteur_prenom"));
                avis.setMaisonTitre(rs.getString("maison_titre"));
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur trouverAvisRecents : " + e.getMessage());
        }
        return avisList;
    }

    private int compterTotalMaisons() {
        String sql = "SELECT COUNT(*) FROM maisons WHERE est_visible = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur compterTotalMaisons : " + e.getMessage());
        }
        return 0;
    }

    private int compterTotalVilles() {
        String sql = "SELECT COUNT(DISTINCT ville) FROM maisons WHERE est_visible = TRUE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur compterTotalVilles : " + e.getMessage());
        }
        return 0;
    }

    private int compterTotalAvis() {
        String sql = "SELECT COUNT(*) FROM avis";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur compterTotalAvis : " + e.getMessage());
        }
        return 0;
    }

    private double calculerNoteGlobale() {
        String sql = "SELECT COALESCE(AVG(note_moyenne), 0) FROM avis";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return Math.round(rs.getDouble(1) * 10.0) / 10.0;
        } catch (SQLException e) {
            System.err.println("[AccueilServlet] Erreur calculerNoteGlobale : " + e.getMessage());
        }
        return 0.0;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}