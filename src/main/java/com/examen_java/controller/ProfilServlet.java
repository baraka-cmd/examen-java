package com.examen_java.controller;

import com.examen_java.dao.AuthentificationDAO;
import com.examen_java.dao.AuthentificationDAOImpl;
import com.examen_java.modele.UtilisateurInscription;
import com.examen_java.utils.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ProfilServlet extends HttpServlet {

    private AuthentificationDAO authentificationDAO;

    @Override
    public void init() throws ServletException {
        this.authentificationDAO = new AuthentificationDAOImpl();
        System.out.println("[ProfilServlet] Initialisée.");
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
        UtilisateurInscription utilisateur = authentificationDAO.trouverParId(idUtilisateur);

        if (utilisateur != null) {
            
            session.setAttribute("utilisateur", utilisateur);
            session.setAttribute("nomComplet", utilisateur.getNomComplet());
            session.setAttribute("email", utilisateur.getEmail());
        }

      
        String modifie = request.getParameter("modifie");
        if ("succes".equals(modifie)) {
            request.setAttribute("succes", "Votre profil a été mis à jour avec succès.");
        }

        String mdpModifie = request.getParameter("mdpModifie");
        if ("succes".equals(mdpModifie)) {
            request.setAttribute("succes", "Votre mot de passe a été changé avec succès.");
        }

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/mon-profil.jsp")
                .forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        int idUtilisateur = (Integer) session.getAttribute("idUtilisateur");
        String action = request.getParameter("action");

        if ("modifierProfil".equals(action)) {
            traiterModificationProfil(request, response, session, idUtilisateur);
        } else if ("changerMotDePasse".equals(action)) {
            traiterChangementMotDePasse(request, response, session, idUtilisateur);
        } else {
            response.sendRedirect(request.getContextPath() + "/mon-profil");
        }
    }

    
    private void traiterModificationProfil(HttpServletRequest request, HttpServletResponse response,
                                            HttpSession session, int idUtilisateur)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String telephone = request.getParameter("telephone");

       
        String erreur = null;
        if (nom == null || nom.trim().isEmpty()) {
            erreur = "Le nom est obligatoire.";
        } else if (prenom == null || prenom.trim().isEmpty()) {
            erreur = "Le prénom est obligatoire.";
        }

        if (erreur != null) {
            request.setAttribute("erreur", erreur);
            doGet(request, response);
            return;
        }

        
        UtilisateurInscription utilisateur = authentificationDAO.trouverParId(idUtilisateur);
        if (utilisateur == null) {
            request.setAttribute("erreur", "Utilisateur introuvable.");
            doGet(request, response);
            return;
        }

        
        utilisateur.setNom(nom.trim());
        utilisateur.setPrenom(prenom.trim());
        utilisateur.setTelephone(telephone != null ? telephone.trim() : null);

        boolean modifie = authentificationDAO.mettreAJour(utilisateur);

        if (modifie) {
            
            session.setAttribute("utilisateur", utilisateur);
            session.setAttribute("nomComplet", utilisateur.getNomComplet());

            System.out.println("[ProfilServlet] Profil mis à jour pour " + utilisateur.getEmail());
            response.sendRedirect(request.getContextPath() + "/mon-profil?modifie=succes");
        } else {
            request.setAttribute("erreur", "Erreur lors de la modification du profil.");
            doGet(request, response);
        }
    }

    
    private void traiterChangementMotDePasse(HttpServletRequest request, HttpServletResponse response,
                                              HttpSession session, int idUtilisateur)
            throws ServletException, IOException {

        String ancienMotDePasse = request.getParameter("ancienMotDePasse");
        String nouveauMotDePasse = request.getParameter("nouveauMotDePasse");
        String confirmation = request.getParameter("confirmationMotDePasse");

        
        String erreur = null;
        if (ancienMotDePasse == null || ancienMotDePasse.trim().isEmpty()) {
            erreur = "Veuillez saisir votre mot de passe actuel.";
        } else if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 8) {
            erreur = "Le nouveau mot de passe doit contenir au moins 8 caractères.";
        } else if (!nouveauMotDePasse.equals(confirmation)) {
            erreur = "Les nouveaux mots de passe ne correspondent pas.";
        }

        if (erreur != null) {
            request.setAttribute("erreurMdp", erreur);
            doGet(request, response);
            return;
        }

        
        UtilisateurInscription utilisateur = authentificationDAO.trouverParId(idUtilisateur);
        if (utilisateur == null) {
            request.setAttribute("erreurMdp", "Utilisateur introuvable.");
            doGet(request, response);
            return;
        }

        if (!PasswordUtil.verifier(ancienMotDePasse, utilisateur.getMotDePasseHash())) {
            request.setAttribute("erreurMdp", "Le mot de passe actuel est incorrect.");
            doGet(request, response);
            return;
        }

        
        String nouveauHash = PasswordUtil.hacher(nouveauMotDePasse);
        utilisateur.setMotDePasseHash(nouveauHash);
        boolean modifie = authentificationDAO.mettreAJourMotDePasse(utilisateur);

        if (modifie) {
            System.out.println("[ProfilServlet] Mot de passe changé pour " + utilisateur.getEmail());
            response.sendRedirect(request.getContextPath() + "/mon-profil?mdpModifie=succes");
        } else {
            request.setAttribute("erreurMdp", "Erreur lors du changement de mot de passe.");
            doGet(request, response);
        }
    }
}