package com.examen_java.controller;

import com.examen_java.dao.MediaMaisonDAO;
import com.examen_java.dao.MediaMaisonDAOImpl;
import com.examen_java.dao.ReservationLocataireDAO;
import com.examen_java.dao.ReservationLocataireDAOImpl;
import com.examen_java.modele.MediaMaison;
import com.examen_java.modele.ReservationProprio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class MesReservationsServlet extends HttpServlet {

    private ReservationLocataireDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        this.reservationDAO = new ReservationLocataireDAOImpl();
        System.out.println("[MesReservationsServlet] Initialisée.");
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

        
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        if ("annuler".equalsIgnoreCase(action) && idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idReservation = Integer.parseInt(idStr.trim());
                if (reservationDAO.appartientA(idReservation, idLocataire)) {
                    boolean annulee = reservationDAO.annuler(idReservation, idLocataire);
                    if (annulee) {
                        request.setAttribute("succes", "Votre réservation a été annulée avec succès.");
                    } else {
                        request.setAttribute("erreur", "Impossible d'annuler cette réservation.");
                    }
                }
            } catch (NumberFormatException e) {
                request.setAttribute("erreur", "Identifiant de réservation invalide.");
            }
        }

       
        String filtreStatut = request.getParameter("statut");
        List<ReservationProprio> reservations;

        if (filtreStatut != null && !filtreStatut.trim().isEmpty()) {
            reservations = reservationDAO.trouverParLocataireEtStatut(idLocataire, filtreStatut.trim());
            request.setAttribute("filtreActif", filtreStatut.trim());
        } else {
            reservations = reservationDAO.trouverParLocataire(idLocataire);
        }

        
        if (reservations != null) {
            MediaMaisonDAO mediaDAO = new MediaMaisonDAOImpl();
            for (ReservationProprio r : reservations) {
                MediaMaison photo = mediaDAO.trouverPhotoPrincipale(r.getIdMaison());
                r.setPhotoPrincipale(photo);
            }
        }

        
        int total = 0, enAttente = 0, confirmees = 0, terminees = 0, annulees = 0;
        if (reservations != null) {
            total = reservations.size();
            for (ReservationProprio r : reservations) {
                String statut = r.getStatut() != null ? r.getStatut().toUpperCase() : "";
                switch (statut) {
                    case "EN_ATTENTE_PAIEMENT": enAttente++; break;
                    case "CONFIRMEE":           confirmees++; break;
                    case "TERMINEE":            terminees++; break;
                    default: 
                        if (statut.contains("ANNULEE")) annulees++; 
                        break;
                }
            }
        }

     
        request.setAttribute("reservations", reservations);
        request.setAttribute("totalReservations", total);
        request.setAttribute("enAttente", enAttente);
        request.setAttribute("confirmees", confirmees);
        request.setAttribute("terminees", terminees);
        request.setAttribute("annulees", annulees);

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/mes-reservations.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}