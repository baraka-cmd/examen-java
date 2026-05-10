package com.examen_java.controller.maison;

import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class SupprimerMaisonServlet extends HttpServlet {

    private MaisonDAO maisonDAO;

    @Override
    public void init() throws ServletException {
        this.maisonDAO = new MaisonDAOImpl();
        System.out.println("[SupprimerMaisonServlet] Initialisée.");
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        traiterSuppression(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        traiterSuppression(request, response);
    }

    
    private void traiterSuppression(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        int idProprietaire = (Integer) session.getAttribute("idUtilisateur");

        
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/mes-maisons");
            return;
        }

        int idMaison;
        try {
            idMaison = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/mes-maisons");
            return;
        }

      
        if (!maisonDAO.appartientA(idMaison, idProprietaire)) {
            response.sendRedirect(request.getContextPath() + "/mes-maisons?erreur=non_autorise");
            return;
        }

        
        boolean desactivee = maisonDAO.desactiver(idMaison, idProprietaire);

        if (desactivee) {
            System.out.println("[SupprimerMaisonServlet] Maison " + idMaison 
                    + " désactivée par propriétaire " + idProprietaire);
            response.sendRedirect(request.getContextPath() + "/mes-maisons?suppression=succes");
        } else {
            response.sendRedirect(request.getContextPath() + "/mes-maisons?erreur=echec_suppression");
        }
    }
}