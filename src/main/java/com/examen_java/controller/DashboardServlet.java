package com.examen_java.controller;

import com.examen_java.modele.UtilisateurInscription;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class DashboardServlet extends HttpServlet {

    private static final String PAGE_CONNEXION = "/connexion";
    private static final String VUE_LOCATAIRE = "/WEB-INF/vues/utilisateur/dashboard-locataire.jsp";
    private static final String VUE_PROPRIETAIRE = "/WEB-INF/vues/utilisateur/dashboard-proprietaire.jsp";

    @Override
    public void init() throws ServletException {
        System.out.println("[DashboardServlet] Initialisée.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + PAGE_CONNEXION);
            return;
        }

        Object idUtilisateur = session.getAttribute("idUtilisateur");
        Object utilisateur = session.getAttribute("utilisateur");
        String role = (String) session.getAttribute("role");

        if (idUtilisateur == null || utilisateur == null || role == null) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + PAGE_CONNEXION);
            return;
        }

        try {
            if ("LOCATAIRE".equalsIgnoreCase(role)) {
                System.out.println("[DashboardServlet] → LOCATAIRE : " + ((UtilisateurInscription) utilisateur).getEmail());
                request.getRequestDispatcher(VUE_LOCATAIRE).forward(request, response);

            } else if ("PROPRIETAIRE".equalsIgnoreCase(role)) {
                int idProprietaire = (Integer) idUtilisateur;

                
                com.examen_java.dao.AvisDAO avisDAO = new com.examen_java.dao.AvisDAOImpl();
                List<com.examen_java.modele.Avis> derniersAvis = avisDAO.trouverParProprietaire(idProprietaire);
                request.setAttribute("derniersAvis", derniersAvis);

                System.out.println("[DashboardServlet] → PROPRIETAIRE : " + ((UtilisateurInscription) utilisateur).getEmail());
                request.getRequestDispatcher(VUE_PROPRIETAIRE).forward(request, response);

            } else {
                System.err.println("[DashboardServlet] Rôle invalide : " + role);
                session.invalidate();
                response.sendRedirect(request.getContextPath() + PAGE_CONNEXION);
            }

        } catch (Exception e) {
            System.err.println("[DashboardServlet] Erreur : " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/erreurs/500.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("[DashboardServlet] Détruite.");
    }
}