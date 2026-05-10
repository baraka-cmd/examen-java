package com.examen_java.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ReservationConfirmationServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("[ReservationConfirmationServlet] Initialisée.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("idUtilisateur") == null) {
            response.sendRedirect(request.getContextPath() + "/connexion");
            return;
        }

        
        String successParam = request.getParameter("success");
        boolean estSucces = "1".equals(successParam);

        
        request.setAttribute("success", estSucces);

        
        request.getRequestDispatcher("/WEB-INF/vues/utilisateur/reservation-confirmation.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("[ReservationConfirmationServlet] Détruite.");
    }
}