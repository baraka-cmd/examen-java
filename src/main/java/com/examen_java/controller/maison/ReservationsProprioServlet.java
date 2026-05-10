package com.examen_java.controller.maison;

import com.examen_java.dao.ReservationProprioDAO;
import com.examen_java.dao.ReservationProprioDAOImpl;
import com.examen_java.modele.ReservationProprio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class ReservationsProprioServlet extends HttpServlet {

    
    private ReservationProprioDAO reservationDAO;

    

    @Override
    public void init() throws ServletException {
        this.reservationDAO = new ReservationProprioDAOImpl();
        System.out.println("[ReservationsProprioServlet] Initialisée.");
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
        if (!"PROPRIETAIRE".equalsIgnoreCase(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        int idProprietaire = (Integer) session.getAttribute("idUtilisateur");

        
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        if (action != null && idStr != null && !idStr.trim().isEmpty()) {
            try {
                int idReservation = Integer.parseInt(idStr);

                if ("confirmer".equalsIgnoreCase(action)) {
                    traiterConfirmation(idReservation, idProprietaire, request);
                } else if ("annuler".equalsIgnoreCase(action)) {
                    traiterAnnulation(idReservation, idProprietaire, request);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("erreur", "Identifiant de réservation invalide.");
            }
        }

        
        String filtreStatut = request.getParameter("statut");
        List<ReservationProprio> reservations;

        if (filtreStatut != null && !filtreStatut.trim().isEmpty()) {
            reservations = reservationDAO.trouverParProprietaireEtStatut(idProprietaire, filtreStatut.trim());
            request.setAttribute("filtreActif", filtreStatut.trim());
        } else {
            reservations = reservationDAO.trouverParProprietaire(idProprietaire);
        }

        
        int totalReservations = reservations.size();
        int enAttente = 0;
        int confirmees = 0;
        int annulees = 0;
        int terminees = 0;

        for (ReservationProprio r : reservations) {
            String s = r.getStatut() != null ? r.getStatut().toUpperCase() : "";
            switch (s) {
                case "EN_ATTENTE_PAIEMENT": enAttente++; break;
                case "CONFIRMEE":           confirmees++; break;
                case "ANNULEE_PAR_LOCATAIRE":
                case "ANNULEE_PAR_PROPRIETAIRE":
                case "ANNULEE":            annulees++; break;
                case "TERMINEE":            terminees++; break;
            }
        }

        
        request.setAttribute("reservations", reservations);
        request.setAttribute("totalReservations", totalReservations);
        request.setAttribute("enAttente", enAttente);
        request.setAttribute("confirmees", confirmees);
        request.setAttribute("annulees", annulees);
        request.setAttribute("terminees", terminees);

        
        request.getRequestDispatcher("/WEB-INF/vues/maison/reservations-proprio.jsp")
                .forward(request, response);
    }

    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

   
    
    private void traiterConfirmation(int idReservation, int idProprietaire,
                                      HttpServletRequest request) {
       
        if (!reservationDAO.appartientAProprietaire(idReservation, idProprietaire)) {
            request.setAttribute("erreur", "Action non autorisée.");
            return;
        }

        
        ReservationProprio reservation = reservationDAO.trouverParId(idReservation);
        if (reservation == null) {
            request.setAttribute("erreur", "Réservation introuvable.");
            return;
        }

        if (!reservation.estEnAttente()) {
            request.setAttribute("erreur", "Cette réservation ne peut plus être confirmée (statut : " 
                    + reservation.getStatutLibelle() + ").");
            return;
        }

        boolean confirmee = reservationDAO.confirmer(idReservation);

        if (confirmee) {
            request.setAttribute("succes", "La réservation #" + idReservation 
                    + " a été confirmée avec succès.");
        } else {
            request.setAttribute("erreur", "Erreur lors de la confirmation.");
        }
    }

    
    private void traiterAnnulation(int idReservation, int idProprietaire,
                                    HttpServletRequest request) {
        
        if (!reservationDAO.appartientAProprietaire(idReservation, idProprietaire)) {
            request.setAttribute("erreur", "Action non autorisée.");
            return;
        }

      
        ReservationProprio reservation = reservationDAO.trouverParId(idReservation);
        if (reservation == null) {
            request.setAttribute("erreur", "Réservation introuvable.");
            return;
        }

        if (reservation.estAnnulee()) {
            request.setAttribute("erreur", "Cette réservation est déjà annulée.");
            return;
        }

        if (reservation.estTerminee()) {
            request.setAttribute("erreur", "Impossible d'annuler une réservation terminée.");
            return;
        }

        boolean annulee = reservationDAO.annuler(idReservation);

        if (annulee) {
            request.setAttribute("succes", "La réservation #" + idReservation 
                    + " a été annulée.");
        } else {
            request.setAttribute("erreur", "Erreur lors de l'annulation.");
        }
    }
}