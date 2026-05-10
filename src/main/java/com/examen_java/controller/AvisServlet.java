package com.examen_java.controller;

import com.examen_java.dao.AvisDAO;
import com.examen_java.dao.AvisDAOImpl;
import com.examen_java.dao.ReservationLocataireDAO;
import com.examen_java.dao.ReservationLocataireDAOImpl;
import com.examen_java.modele.Avis;
import com.examen_java.modele.ReservationProprio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class AvisServlet extends HttpServlet {

    private AvisDAO avisDAO;
    private ReservationLocataireDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        this.avisDAO = new AvisDAOImpl();
        this.reservationDAO = new ReservationLocataireDAOImpl();
        System.out.println("[AvisServlet] Initialisée.");
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

       
        if (!avisDAO.peutDonnerAvis(idReservation, idLocataire)) {
            request.setAttribute("erreur", "Vous ne pouvez pas donner votre avis sur cette réservation. "
                    + "Vérifiez que le séjour est terminé et que vous n'avez pas déjà laissé un avis.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/laisser-avis.jsp")
                    .forward(request, response);
            return;
        }

        
        if (avisDAO.existePourReservation(idReservation)) {
            request.setAttribute("erreur", "Vous avez déjà donné votre avis pour cette réservation.");
            request.getRequestDispatcher("/WEB-INF/vues/utilisateur/laisser-avis.jsp")
                    .forward(request, response);
            return;
        }

  
        ReservationProprio reservation = reservationDAO.trouverParLocataire(idLocataire)
                .stream()
                .filter(r -> r.getIdReservation() == idReservation)
                .findFirst()
                .orElse(null);

        request.setAttribute("idReservation", idReservation);
        request.setAttribute("reservation", reservation);

        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/laisser-avis.jsp")
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
        String notePropreteStr = request.getParameter("note_proprete");
        String noteCommunicationStr = request.getParameter("note_communication");
        String noteEmplacementStr = request.getParameter("note_emplacement");
        String noteRapportStr = request.getParameter("note_rapport_qualite_prix");
        String commentaire = request.getParameter("commentaire");

       
        if (idReservationStr == null || idReservationStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

        int idReservation = Integer.parseInt(idReservationStr);

        
        if (!reservationDAO.appartientA(idReservation, idLocataire)) {
            response.sendRedirect(request.getContextPath() + "/mes-reservations");
            return;
        }

      
        if (avisDAO.existePourReservation(idReservation)) {
            request.setAttribute("erreur", "Vous avez déjà donné votre avis pour cette réservation.");
            doGet(request, response);
            return;
        }

        
        int noteProprete = parseNote(notePropreteStr);
        int noteCommunication = parseNote(noteCommunicationStr);
        int noteEmplacement = parseNote(noteEmplacementStr);
        int noteRapport = parseNote(noteRapportStr);

        if (noteProprete == 0 || noteCommunication == 0 || noteEmplacement == 0 || noteRapport == 0) {
            request.setAttribute("erreur", "Veuillez attribuer une note à tous les critères.");
            doGet(request, response);
            return;
        }

       
        ReservationProprio reservation = reservationDAO.trouverParLocataire(idLocataire)
                .stream()
                .filter(r -> r.getIdReservation() == idReservation)
                .findFirst()
                .orElse(null);

        if (reservation == null) {
            request.setAttribute("erreur", "Réservation introuvable.");
            doGet(request, response);
            return;
        }

       
        Avis avis = new Avis();
        avis.setIdReservation(idReservation);
        avis.setIdMaison(reservation.getIdMaison());
        avis.setIdAuteur(idLocataire);
        avis.setNoteProprete(noteProprete);
        avis.setNoteCommunication(noteCommunication);
        avis.setNoteEmplacement(noteEmplacement);
        avis.setNoteRapportQualitePrix(noteRapport);
        avis.setCommentaire(commentaire != null ? commentaire.trim() : null);

        boolean cree = avisDAO.creer(avis);

        if (cree) {
            System.out.println("[AvisServlet] Avis créé - Réservation: " + idReservation 
                    + ", Locataire: " + idLocataire);
            response.sendRedirect(request.getContextPath() + "/mes-reservations?avis=succes");
        } else {
            request.setAttribute("erreur", "Erreur lors de l'enregistrement de votre avis.");
            doGet(request, response);
        }
    }

    
    private int parseNote(String noteStr) {
        if (noteStr == null || noteStr.trim().isEmpty()) return 0;
        try {
            int note = Integer.parseInt(noteStr.trim());
            return (note >= 1 && note <= 5) ? note : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}