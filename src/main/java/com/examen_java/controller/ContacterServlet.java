package com.examen_java.controller;

import com.examen_java.dao.MessageDAO;
import com.examen_java.dao.MessageDAOImpl;
import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;
import com.examen_java.modele.Maison;
import com.examen_java.modele.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ContacterServlet extends HttpServlet {

    private MessageDAO messageDAO;
    private MaisonDAO maisonDAO;

    @Override
    public void init() throws ServletException {
        this.messageDAO = new MessageDAOImpl();
        this.maisonDAO = new MaisonDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        int idLocataire = (Integer) session.getAttribute("idUtilisateur");
        String idMaisonStr = request.getParameter("idMaison");

        if (idMaisonStr == null || idMaisonStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        int idMaison = Integer.parseInt(idMaisonStr);
        Maison maison = maisonDAO.trouverParId(idMaison);

        if (maison == null) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        
        response.sendRedirect(request.getContextPath() + "/maison/detail?id=" + idMaison 
                + "&message=Pour contacter le propriétaire, effectuez d'abord une réservation.");
    }
}