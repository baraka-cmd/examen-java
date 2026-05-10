package com.examen_java.controller;

import com.examen_java.dao.PaiementDAO;
import com.examen_java.dao.PaiementDAOImpl;
import com.examen_java.dao.ReservationLocataireDAO;
import com.examen_java.dao.ReservationLocataireDAOImpl;
import com.examen_java.modele.ReservationProprio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class PaiementServlet extends HttpServlet {

    private PaiementDAO paiementDAO;
    private ReservationLocataireDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        this.paiementDAO = new PaiementDAOImpl();
        this.reservationDAO = new ReservationLocataireDAOImpl();
        System.out.println("[PaiementServlet] Initialisée.");
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

        
        String idReservationStr = request.getParameter("id_reservation");
        if (idReservationStr == null || idReservationStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

        int idReservation;
        try {
            idReservation = Integer.parseInt(idReservationStr.trim());
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

        
        if (!reservationDAO.appartientA(idReservation, idLocataire)) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

        
        if (!paiementDAO.peutPayer(idReservation, idLocataire)) {
            request.setAttribute("erreur", "Cette réservation ne peut pas être payée. "
                    + "Vérifiez qu'elle est en attente de paiement.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/paiement.jsp")
                    .forward(request, response);
            return;
        }

       
        if (paiementDAO.paiementExiste(idReservation)) {
            request.setAttribute("erreur", "Un paiement a déjà été effectué pour cette réservation.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/paiement.jsp")
                    .forward(request, response);
            return;
        }

        
        ReservationProprio reservation = null;
        for (ReservationProprio r : reservationDAO.trouverParLocataire(idLocataire)) {
            if (r.getIdReservation() == idReservation) {
                reservation = r;
                break;
            }
        }

        if (reservation == null) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

        
        request.setAttribute("reservation", reservation);
        request.setAttribute("idReservation", idReservation);

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/paiement.jsp")
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

        int idLocataire = (Integer) session.getAttribute("idUtilisateur");

   
        String idReservationStr = request.getParameter("id_reservation");
        String montantStr = request.getParameter("montant");
        String methodePaiement = request.getParameter("methode_paiement");

        if (idReservationStr == null || idReservationStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

        int idReservation = Integer.parseInt(idReservationStr);
        double montant = Double.parseDouble(montantStr != null ? montantStr : "0");

        if (methodePaiement == null || methodePaiement.trim().isEmpty()) {
            methodePaiement = "CARTE_BANCAIRE";
        }

        
        if (!paiementDAO.peutPayer(idReservation, idLocataire)) {
            request.setAttribute("erreur", "Ce paiement ne peut plus être effectué.");
            doGet(request, response);
            return;
        }

        
        boolean succes = paiementDAO.effectuerPaiement(idReservation, idLocataire, montant, methodePaiement);

        if (succes) {
            System.out.println("[PaiementServlet] Paiement réussi - Réservation: " + idReservation 
                    + ", Locataire: " + idLocataire + ", Montant: " + montant + " €");
            response.sendRedirect(request.getContextPath() + "/paiement/confirmation?id_reservation=" + idReservation);
        } else {
            request.setAttribute("erreur", "Erreur lors du paiement. Veuillez réessayer.");
            doGet(request, response);
        }
    }
}