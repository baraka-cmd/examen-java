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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class PaiementConfirmationServlet extends HttpServlet {

    private ReservationLocataireDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        this.reservationDAO = new ReservationLocataireDAOImpl();
        System.out.println("[PaiementConfirmationServlet] Initialisée.");
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

        
        String reference = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String datePaiement = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm").format(new Date());

        
        request.setAttribute("reservation", reservation);
        request.setAttribute("reference", reference);
        request.setAttribute("datePaiement", datePaiement);

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/paiement-confirmation.jsp")
                .forward(request, response);
    }
}