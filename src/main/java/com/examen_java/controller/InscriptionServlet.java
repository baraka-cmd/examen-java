package com.examen_java.controller;

import com.examen_java.dao.InscriptionDAO;
import com.examen_java.dao.InscriptionDAOImpl;
import com.examen_java.modele.ProfilInscription;
import com.examen_java.modele.UtilisateurInscription;
import com.examen_java.utils.PasswordUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebServlet("/inscription")
public class InscriptionServlet extends HttpServlet {

    
    private InscriptionDAO inscriptionDAO;

    
    @Override
    public void init() throws ServletException {
        this.inscriptionDAO = new InscriptionDAOImpl();
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                .forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String motDePasse = request.getParameter("motDePasse");
        String confirmation = request.getParameter("confirmation");
        String roleChoisi = request.getParameter("role");

        
        if (nom == null || nom.trim().isEmpty()) {
            request.setAttribute("erreur", "Le nom est obligatoire.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

        
        if (prenom == null || prenom.trim().isEmpty()) {
            request.setAttribute("erreur", "Le prénom est obligatoire.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

       
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            request.setAttribute("erreur", "L'adresse email est invalide.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

        
        if (motDePasse == null || motDePasse.length() < 8) {
            request.setAttribute("erreur", "Le mot de passe doit contenir au moins 8 caractères.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

       
        if (!motDePasse.equals(confirmation)) {
            request.setAttribute("erreur", "Les mots de passe ne correspondent pas.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

       
        if (roleChoisi == null || 
            (!roleChoisi.equalsIgnoreCase("LOCATAIRE") && 
             !roleChoisi.equalsIgnoreCase("PROPRIETAIRE"))) {
            request.setAttribute("erreur", "Veuillez choisir un rôle valide (LOCATAIRE ou PROPRIETAIRE).");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

        
        if (inscriptionDAO.emailExiste(email)) {
            request.setAttribute("erreur", "Cet email est déjà utilisé.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

        
        String motDePasseHash = PasswordUtil.hacher(motDePasse);

       
        UtilisateurInscription utilisateur = new UtilisateurInscription();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setTelephone(telephone);
        utilisateur.setMotDePasseHash(motDePasseHash);

        int idUtilisateur = inscriptionDAO.creerUtilisateur(utilisateur);

        if (idUtilisateur <= 0) {
            request.setAttribute("erreur", "Erreur technique. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/inscription.jsp")
                    .forward(request, response);
            return;
        }

        
        ProfilInscription profil = new ProfilInscription(idUtilisateur, roleChoisi);
        inscriptionDAO.creerProfil(profil);

        
        HttpSession session = request.getSession();
        session.setAttribute("utilisateur", utilisateur);
        session.setAttribute("idUtilisateur", idUtilisateur);
        session.setAttribute("role", roleChoisi.toUpperCase());

        
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}