package com.examen_java.controller;

import com.examen_java.dao.FavoriDAO;
import com.examen_java.dao.FavoriDAOImpl;
import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;
import com.examen_java.dao.MediaMaisonDAO;
import com.examen_java.dao.MediaMaisonDAOImpl;
import com.examen_java.modele.Maison;
import com.examen_java.modele.MediaMaison;

import com.examen_java.dao.AvisDAO;
import com.examen_java.dao.AvisDAOImpl;
import com.examen_java.modele.Avis;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class DetailMaisonServlet extends HttpServlet {

    private MaisonDAO maisonDAO;
    private MediaMaisonDAO mediaMaisonDAO;

    @Override
    public void init() throws ServletException {
        this.maisonDAO = new MaisonDAOImpl();
        this.mediaMaisonDAO = new MediaMaisonDAOImpl();
        System.out.println("[DetailMaisonServlet] Initialisée.");
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        int idMaison;
        try {
            idMaison = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        
        Maison maison = maisonDAO.trouverParId(idMaison);

        if (maison == null || !maison.isEstVisible()) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        
        List<MediaMaison> medias = mediaMaisonDAO.trouverParMaison(idMaison);
        maison.setMedias(medias);

        
        MediaMaison photoPrincipale = mediaMaisonDAO.trouverPhotoPrincipale(idMaison);
        maison.setPhotoPrincipale(photoPrincipale);

        
        com.examen_java.dao.AvisDAO avisDAO = new com.examen_java.dao.AvisDAOImpl();
        java.util.List<com.examen_java.modele.Avis> avis = avisDAO.trouverParMaison(idMaison);
        request.setAttribute("avis", avis);

        
        double noteMoyenne = 0;
        if (avis != null && !avis.isEmpty()) {
            double somme = 0;
            for (com.examen_java.modele.Avis a : avis) {
                somme += a.getNoteMoyenne();
            }
            noteMoyenne = somme / avis.size();
        }
        request.setAttribute("noteMoyenne", noteMoyenne);
        request.setAttribute("nombreAvis", avis != null ? avis.size() : 0);

       
        HttpSession session = request.getSession(false);
        boolean estFavori = false;
        if (session != null && session.getAttribute("idUtilisateur") != null) {
            int idUtilisateur = (Integer) session.getAttribute("idUtilisateur");
            FavoriDAO favoriDAO = new FavoriDAOImpl();
            estFavori = favoriDAO.estFavori(idUtilisateur, idMaison);
        }
        request.setAttribute("estFavori", estFavori);

    
        request.setAttribute("maison", maison);
        request.setAttribute("medias", medias);
        request.setAttribute("estFavori", estFavori);
        request.setAttribute("nombrePhotos", medias != null ? medias.size() : 0);

      
        String favoriAjoute = request.getParameter("favori");
        if ("ajoute".equals(favoriAjoute)) {
            request.setAttribute("succes", "Maison ajoutée à vos favoris !");
        }

     
        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/detail-maison.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}