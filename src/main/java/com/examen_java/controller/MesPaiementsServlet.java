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
import java.util.ArrayList;
import java.util.List;


public class MesPaiementsServlet extends HttpServlet {

    private PaiementDAO paiementDAO;
    private ReservationLocataireDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        this.paiementDAO = new PaiementDAOImpl();
        this.reservationDAO = new ReservationLocataireDAOImpl();
        System.out.println("[MesPaiementsServlet] Initialisée.");
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

        
        List<ReservationProprio> toutesReservations = reservationDAO.trouverParLocataire(idLocataire);

        
        List<ReservationProprio> paiements = new ArrayList<>();
        double totalPaye = 0;

        if (toutesReservations != null) {
            for (ReservationProprio r : toutesReservations) {
                if (paiementDAO.paiementExiste(r.getIdReservation())) {
                    paiements.add(r);
                    totalPaye += r.getMontantTotal();
                }
            }
        }

        request.setAttribute("paiements", paiements);
        request.setAttribute("totalPaye", totalPaye);
        request.setAttribute("nombrePaiements", paiements.size());

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/mes-paiements.jsp")
                .forward(request, response);
    }
}