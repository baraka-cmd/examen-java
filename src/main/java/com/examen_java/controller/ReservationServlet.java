package com.examen_java.controller;

import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;
import com.examen_java.dao.ReservationLocataireDAO;
import com.examen_java.dao.ReservationLocataireDAOImpl;
import com.examen_java.modele.Maison;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ReservationServlet extends HttpServlet {

    private MaisonDAO maisonDAO;
    private ReservationLocataireDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        this.maisonDAO = new MaisonDAOImpl();
        this.reservationDAO = new ReservationLocataireDAOImpl();
        System.out.println("[ReservationServlet] Initialisée.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (!"LOCATAIRE".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String idMaisonStr = request.getParameter("idMaison");
        String dateDebutStr = request.getParameter("dateDebut");
        String dateFinStr = request.getParameter("dateFin");
        String nombrePersonnesStr = request.getParameter("nombrePersonnes");

        if (idMaisonStr == null || idMaisonStr.trim().isEmpty()
                || dateDebutStr == null || dateDebutStr.trim().isEmpty()
                || dateFinStr == null || dateFinStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        int idMaison;
        int nombrePersonnes = 1;

        try {
            idMaison = Integer.parseInt(idMaisonStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        try {
            if (nombrePersonnesStr != null && !nombrePersonnesStr.trim().isEmpty()) {
                nombrePersonnes = Integer.parseInt(nombrePersonnesStr.trim());
            }
        } catch (NumberFormatException e) {
            nombrePersonnes = 1;
        }

        // Validation des dates (sans les heures)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateDebut = null;
        Date dateFin = null;

        try {
            dateDebut = sdf.parse(dateDebutStr.trim());
            dateFin = sdf.parse(dateFinStr.trim());
        } catch (ParseException e) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

       
        Calendar calAujourdhui = Calendar.getInstance();
        calAujourdhui.set(Calendar.HOUR_OF_DAY, 0);
        calAujourdhui.set(Calendar.MINUTE, 0);
        calAujourdhui.set(Calendar.SECOND, 0);
        calAujourdhui.set(Calendar.MILLISECOND, 0);
        Date aujourdhui = calAujourdhui.getTime();

        if (dateDebut.before(aujourdhui)) {
            request.setAttribute("erreur", "La date d'arrivée ne peut pas être dans le passé.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation.jsp").forward(request, response);
            return;
        }

        if (!dateFin.after(dateDebut)) {
            request.setAttribute("erreur", "La date de départ doit être après la date d'arrivée.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation.jsp").forward(request, response);
            return;
        }

        
        Maison maison = maisonDAO.trouverParId(idMaison);
        
        if (maison == null || !maison.isEstVisible()) {
            request.setAttribute("erreur", "Cette maison n'est plus disponible.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation.jsp").forward(request, response);
            return;
        }
        
        

        
        if (nombrePersonnes > maison.getCapacitePersonnes()) {
            request.setAttribute("erreur", "Le nombre de voyageurs dépasse la capacité (" 
                    + maison.getCapacitePersonnes() + " max).");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation.jsp").forward(request, response);
            return;
        }

       
        if (!reservationDAO.estDisponible(idMaison, dateDebutStr, dateFinStr)) {
            request.setAttribute("erreur", "Cette maison n'est plus disponible pour ces dates.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation.jsp").forward(request, response);
            return;
        }

        
        long diffMilliseconds = dateFin.getTime() - dateDebut.getTime();
        long nombreNuits = TimeUnit.DAYS.convert(diffMilliseconds, TimeUnit.MILLISECONDS);
        if (nombreNuits <= 0) nombreNuits = 1;
        double prixTotal = nombreNuits * maison.getPrixBaseNuit();

        // Attributs pour la JSP
        request.setAttribute("maison", maison);
        request.setAttribute("dateDebut", dateDebutStr);
        request.setAttribute("dateFin", dateFinStr);
        request.setAttribute("nombrePersonnes", nombrePersonnes);
        request.setAttribute("nombreNuits", nombreNuits);
        request.setAttribute("prixTotal", prixTotal);
        request.setAttribute("idMaison", idMaison);

        System.out.println("[ReservationServlet] Page récapitulative affichée - Maison: " + idMaison);

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        int idLocataire = (Integer) session.getAttribute("idUtilisateur");

        String idMaisonStr = request.getParameter("idMaison");
        String dateDebutStr = request.getParameter("dateDebut");
        String dateFinStr = request.getParameter("dateFin");
        String nombrePersonnesStr = request.getParameter("nombrePersonnes");
        String prixTotalStr = request.getParameter("prixTotal");

        if (idMaisonStr == null || dateDebutStr == null || dateFinStr == null) {
            response.sendRedirect(request.getContextPath() + "/recherche");
            return;
        }

        int idMaison = Integer.parseInt(idMaisonStr);
        int nombrePersonnes = Integer.parseInt(nombrePersonnesStr != null ? nombrePersonnesStr : "1");
        double prixTotal = Double.parseDouble(prixTotalStr != null ? prixTotalStr : "0");

        if (!reservationDAO.estDisponible(idMaison, dateDebutStr, dateFinStr)) {
            request.setAttribute("erreur", "Cette maison vient d'être réservée pour ces dates.");
            doGet(request, response);
            return;
        }

        boolean cree = reservationDAO.creer(idMaison, idLocataire, dateDebutStr, dateFinStr,
                nombrePersonnes, prixTotal);

        if (cree) {
            System.out.println("[ReservationServlet] Réservation créée - Maison: " + idMaison 
                    + ", Locataire: " + idLocataire);
            response.sendRedirect(request.getContextPath() + "/reservation-confirmation?success=1");
        } else {
            request.setAttribute("erreur", "Erreur lors de la création de la réservation.");
            doGet(request, response);
        }
    }
}