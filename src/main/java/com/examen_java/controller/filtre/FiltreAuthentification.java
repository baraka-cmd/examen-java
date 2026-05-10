package com.examen_java.controller.filtre;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class FiltreAuthentification implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("[FiltreAuthentification] Initialisé.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());

        
        boolean estPagePublique = path.startsWith("/accueil")
                || path.startsWith("/inscription")
                || path.startsWith("/connexion")
                || path.startsWith("/assets/")
                || path.equals("/")
                || path.startsWith("/erreurs/")
                || path.equals("/index.jsp");

        
        boolean estConnecte = (session != null && session.getAttribute("idUtilisateur") != null);

        if (estPagePublique) {
            
            chain.doFilter(request, response);
        } else if (estConnecte) {
            
            chain.doFilter(request, response);
        } else {
            
            System.out.println("[FiltreAuthentification] Accès refusé → " + path);
            res.sendRedirect(req.getContextPath() + "/connexion");
        }
    }

    @Override
    public void destroy() {
        System.out.println("[FiltreAuthentification] Détruit.");
    }
}