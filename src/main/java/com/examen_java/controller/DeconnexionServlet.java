package com.examen_java.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class DeconnexionServlet extends HttpServlet {

    
    @Override
    public void init() throws ServletException {
        System.out.println("[DeconnexionServlet] Servlet initialisée.");
    }

   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            
            Object email = session.getAttribute("email");
            Object idUtilisateur = session.getAttribute("idUtilisateur");

            
            if (email != null) {
                System.out.println("[DeconnexionServlet] Déconnexion : " + email 
                        + " (ID=" + idUtilisateur + ")");
            }

           
            session.invalidate();
        }

        
        response.sendRedirect(request.getContextPath() + "/connexion?deconnexion=succes");
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    
    @Override
    public void destroy() {
        System.out.println("[DeconnexionServlet] Servlet détruite.");
    }
}