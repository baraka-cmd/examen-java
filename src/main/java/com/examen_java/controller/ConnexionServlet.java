package com.examen_java.controller;

import com.examen_java.dao.AuthentificationDAO;
import com.examen_java.dao.AuthentificationDAOImpl;
import com.examen_java.dao.MessageDAO;
import com.examen_java.dao.MessageDAOImpl;
import com.examen_java.modele.ProfilInscription;
import com.examen_java.modele.UtilisateurInscription;
import com.examen_java.utils.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class ConnexionServlet extends HttpServlet {

    
    private AuthentificationDAO authentificationDAO;

    
    @Override
    public void init() throws ServletException {
        this.authentificationDAO = new AuthentificationDAOImpl();
        System.out.println("[ConnexionServlet] Servlet initialisée.");
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        
        if (session != null && session.getAttribute("idUtilisateur") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        
        String inscriptionReussie = request.getParameter("inscription");
        if ("reussie".equals(inscriptionReussie)) {
            request.setAttribute("succes", "Votre compte a été créé avec succès. Connectez-vous.");
        }

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                .forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");

        
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("erreur", "Veuillez saisir votre adresse email.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                    .forward(request, response);
            return;
        }

        
        if (motDePasse == null || motDePasse.trim().isEmpty()) {
            request.setAttribute("erreur", "Veuillez saisir votre mot de passe.");
            request.setAttribute("emailSaisi", email.trim());
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                    .forward(request, response);
            return;
        }

        
        UtilisateurInscription utilisateur = authentificationDAO.trouverParEmail(email.trim());

        // Utilisateur introuvable
        if (utilisateur == null) {
            request.setAttribute("erreur", "Email ou mot de passe incorrect.");
            request.setAttribute("emailSaisi", email.trim());
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                    .forward(request, response);
            return;
        }

        
        if (!utilisateur.isEstActif()) {
            request.setAttribute("erreur", 
                    "Votre compte a été désactivé. Contactez le support.");
            request.setAttribute("emailSaisi", email.trim());
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                    .forward(request, response);
            return;
        }

        
        boolean motDePasseValide = PasswordUtil.verifier(
                motDePasse, 
                utilisateur.getMotDePasseHash()
        );

        if (!motDePasseValide) {
            request.setAttribute("erreur", "Email ou mot de passe incorrect.");
            request.setAttribute("emailSaisi", email.trim());
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                    .forward(request, response);
            return;
        }

        
        List<ProfilInscription> profils = authentificationDAO
                .trouverProfilsParUtilisateur(utilisateur.getIdUtilisateur());

        if (profils.isEmpty()) {
            request.setAttribute("erreur", 
                    "Aucun rôle attribué à ce compte. Contactez le support.");
            request.setAttribute("emailSaisi", email.trim());
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/connexion.jsp")
                    .forward(request, response);
            return;
        }

        
        HttpSession session = request.getSession();
        session.setAttribute("utilisateur", utilisateur);
        session.setAttribute("idUtilisateur", utilisateur.getIdUtilisateur());
        session.setAttribute("email", utilisateur.getEmail());
        session.setAttribute("nomComplet", utilisateur.getNomComplet());

        
        String rolePrincipal = profils.get(0).getRoleString();
        session.setAttribute("role", rolePrincipal);

       
        session.setAttribute("profils", profils);

        
        session.setAttribute("aPlusieursRoles", profils.size() > 1);

        
        
        authentificationDAO.mettreAJourDerniereConnexion(utilisateur.getIdUtilisateur());

        
        
        MessageDAO messageDAO = new MessageDAOImpl();
        int messagesNonLus = 0;

        if ("PROPRIETAIRE".equalsIgnoreCase(rolePrincipal)) {
            messagesNonLus = messageDAO.compterNonLusParProprietaire(utilisateur.getIdUtilisateur());
        }
        

        session.setAttribute("nombreMessagesNonLus", messagesNonLus);
        
        System.out.println("[ConnexionServlet] Connexion réussie : " 
                + utilisateur.getEmail() + " (rôle : " + rolePrincipal + ")");
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    
    @Override
    public void destroy() {
        System.out.println("[ConnexionServlet] Servlet détruite.");
    }
}