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


public class MessagesServlet extends HttpServlet {

    private MessageDAO messageDAO;

    @Override
    public void init() throws ServletException {
        this.messageDAO = new MessageDAOImpl();
        System.out.println("[MessagesServlet] Initialisée.");
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
        String role = (String) session.getAttribute("role");

        List<Message> conversations = null;
        int messagesNonLus = 0;

        if ("PROPRIETAIRE".equalsIgnoreCase(role)) {
            conversations = messageDAO.trouverConversationsParProprietaire(idUtilisateur);
            messagesNonLus = messageDAO.compterNonLusParProprietaire(idUtilisateur);
        } else if ("LOCATAIRE".equalsIgnoreCase(role)) {
            conversations = messageDAO.trouverConversationsParLocataire(idUtilisateur);
            messagesNonLus = messageDAO.compterNonLusParLocataire(idUtilisateur);
        }

        
        session.setAttribute("nombreMessagesNonLus", messagesNonLus);

        request.setAttribute("conversations", conversations);
        request.setAttribute("messagesNonLus", messagesNonLus);
        request.setAttribute("nombreConversations", conversations != null ? conversations.size() : 0);

        String messageEnvoye = request.getParameter("envoye");
        if ("succes".equals(messageEnvoye)) {
            request.setAttribute("succes", "Votre message a été envoyé avec succès.");
        }

        request.getRequestDispatcher("/WEB-INF/vues/message/messages.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}