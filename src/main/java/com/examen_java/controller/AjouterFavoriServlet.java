package com.examen_java.controller;

import com.examen_java.dao.FavoriDAO;
import com.examen_java.dao.FavoriDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AjouterFavoriServlet extends HttpServlet {

    private FavoriDAO favoriDAO;

    @Override
    public void init() throws ServletException {
        this.favoriDAO = new FavoriDAOImpl();
        System.out.println("[AjouterFavoriServlet] Initialisée.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        int idUtilisateur = (Integer) session.getAttribute("idUtilisateur");

        
        String idMaisonStr = request.getParameter("idMaison");
        if (idMaisonStr == null || idMaisonStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        int idMaison;
        try {
            idMaison = Integer.parseInt(idMaisonStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        
        boolean ajoute = favoriDAO.ajouter(idUtilisateur, idMaison);

     
        int nombreFavoris = favoriDAO.compter(idUtilisateur);
        session.setAttribute("nombreFavoris", nombreFavoris);

        if (ajoute) {
            System.out.println("[AjouterFavoriServlet] Favori ajouté - Utilisateur: " 
                    + idUtilisateur + ", Maison: " + idMaison);
            response.sendRedirect(request.getContextPath() 
                    + "/maison/detail?id=" + idMaison + "&favori=ajoute");
        } else {
            response.sendRedirect(request.getContextPath() 
                    + "/maison/detail?id=" + idMaison + "&erreur=favori");
        }
    }
}