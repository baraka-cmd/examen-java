package com.examen_java.controller;

import com.examen_java.dao.FavoriDAO;
import com.examen_java.dao.FavoriDAOImpl;
import com.examen_java.modele.Maison;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class FavorisServlet extends HttpServlet {

    private FavoriDAO favoriDAO;

    @Override
    public void init() throws ServletException {
        this.favoriDAO = new FavoriDAOImpl();
        System.out.println("[FavorisServlet] Initialisée.");
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

        
        List<Maison> favoris = favoriDAO.trouverParUtilisateur(idUtilisateur);
        int nombreFavoris = favoris != null ? favoris.size() : 0;

        
        session.setAttribute("nombreFavoris", nombreFavoris);

       
        String supprime = request.getParameter("supprime");
        if ("1".equals(supprime)) {
            request.setAttribute("succes", "La maison a été retirée de vos favoris.");
        }

        
        request.setAttribute("favoris", favoris);
        request.setAttribute("nombreFavoris", nombreFavoris);

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/favoris.jsp")
                .forward(request, response);
    }
}