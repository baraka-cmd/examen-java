package com.examen_java.controller;

import com.examen_java.dao.RechercheDAO;
import com.examen_java.dao.RechercheDAOImpl;
import com.examen_java.modele.Maison;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RechercheServlet extends HttpServlet {

    private RechercheDAO rechercheDAO;

    @Override
    public void init() throws ServletException {
        this.rechercheDAO = new RechercheDAOImpl();
        System.out.println("[RechercheServlet] Initialisée.");
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        String ville = request.getParameter("ville");
        String dateDebut = request.getParameter("dateDebut");
        String dateFin = request.getParameter("dateFin");
        String capaciteStr = request.getParameter("capacite");
        String prixMaxStr = request.getParameter("prixMax");

        int capacite = 0;
        double prixMax = 0.0;

        try {
            if (capaciteStr != null && !capaciteStr.trim().isEmpty()) {
                capacite = Integer.parseInt(capaciteStr.trim());
            }
        } catch (NumberFormatException e) {
            capacite = 0;
        }

        try {
            if (prixMaxStr != null && !prixMaxStr.trim().isEmpty()) {
                prixMax = Double.parseDouble(prixMaxStr.trim());
            }
        } catch (NumberFormatException e) {
            prixMax = 0.0;
        }

        
        boolean datesValides = true;
        if (dateDebut != null && !dateDebut.trim().isEmpty()
                && dateFin != null && !dateFin.trim().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date debut = sdf.parse(dateDebut.trim());
                Date fin = sdf.parse(dateFin.trim());

                if (debut.after(fin)) {
                    request.setAttribute("erreur", "La date de début doit être avant la date de fin.");
                    datesValides = false;
                } else if (debut.before(new Date())) {
                    request.setAttribute("erreur", "La date de début ne peut pas être dans le passé.");
                    datesValides = false;
                }
            } catch (Exception e) {
                request.setAttribute("erreur", "Format de date invalide.");
                datesValides = false;
            }
        }

        
        boolean rechercheLancee = (ville != null && !ville.trim().isEmpty())
                || (dateDebut != null && !dateDebut.trim().isEmpty())
                || capacite > 0
                || prixMax > 0;

        List<Maison> maisons = null;
        int nombreResultats = 0;

        if (rechercheLancee && datesValides) {
            
            maisons = rechercheDAO.rechercher(ville, dateDebut, dateFin, capacite, prixMax);
            nombreResultats = (maisons != null) ? maisons.size() : 0;
        }

        
        if (!rechercheLancee) {
            maisons = rechercheDAO.trouverDernieresMaisons(9);
            nombreResultats = (maisons != null) ? maisons.size() : 0;
        }

        
        request.setAttribute("maisons", maisons);
        request.setAttribute("nombreResultats", nombreResultats);
        request.setAttribute("rechercheLancee", rechercheLancee);

      
        request.setAttribute("villeSaisie", ville);
        request.setAttribute("dateDebutSaisie", dateDebut);
        request.setAttribute("dateFinSaisie", dateFin);
        request.setAttribute("capaciteSaisie", capaciteStr);
        request.setAttribute("prixMaxSaisie", prixMaxStr);

        
        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/recherche.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}