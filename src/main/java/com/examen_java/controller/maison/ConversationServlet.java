package com.examen_java.controller.maison;

import com.examen_java.dao.MessageDAO;
import com.examen_java.dao.MessageDAOImpl;
import com.examen_java.modele.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class ConversationServlet extends HttpServlet {

    private MessageDAO messageDAO;

    @Override
    public void init() throws ServletException {
        this.messageDAO = new MessageDAOImpl();
        System.out.println("[ConversationServlet] Initialisée.");
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

      
        int messagesNonLus = messageDAO.compterNonLusParProprietaire(idUtilisateur);
        session.setAttribute("nombreMessagesNonLus", messagesNonLus);
        
        String idParam = request.getParameter("id_reservation");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }

        int idReservation;
        try {
            idReservation = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }

        
        if (!messageDAO.reservationAppartientA(idReservation, idUtilisateur)) {
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }

        
        List<Message> messages = messageDAO.trouverParReservation(idReservation);

        
        messageDAO.marquerCommeLu(idReservation, idUtilisateur);

        
        request.setAttribute("messages", messages);
        request.setAttribute("idReservation", idReservation);
        request.setAttribute("idUtilisateur", idUtilisateur);

    
        if (messages != null && !messages.isEmpty()) {
            Message premier = messages.get(0);
            request.setAttribute("maisonTitre", premier.getMaisonTitre());
            request.setAttribute("locataireNom", premier.getLocataireNom() + " " + premier.getLocatairePrenom());
            request.setAttribute("proprietaireNom", premier.getProprietaireNom() + " " + premier.getProprietairePrenom());
        }

      
        request.getRequestDispatcher("/WEB-INF/vues/message/conversation.jsp")
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

        
        String idParam = request.getParameter("id_reservation");
        String contenu = request.getParameter("contenu");

        
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }

        int idReservation;
        try {
            idReservation = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }

        
        if (!messageDAO.reservationAppartientA(idReservation, idUtilisateur)) {
            response.sendRedirect(request.getContextPath() + "/messages");
            return;
        }

        
        if (contenu == null || contenu.trim().isEmpty()) {
            request.setAttribute("erreur", "Veuillez saisir un message.");
            doGet(request, response);
            return;
        }

      
        Message message = new Message();
        message.setIdReservation(idReservation);
        message.setIdExpediteur(idUtilisateur);
        message.setContenu(contenu.trim());

        boolean envoye = messageDAO.envoyer(message);

        if (envoye) {
            System.out.println("[ConversationServlet] Message envoyé par " + idUtilisateur 
                    + " pour réservation " + idReservation);
            response.sendRedirect(request.getContextPath() 
                    + "/messages/conversation?id_reservation=" + idReservation + "&envoye=succes");
        } else {
            request.setAttribute("erreur", "Erreur lors de l'envoi du message.");
            doGet(request, response);
        }
    }
}