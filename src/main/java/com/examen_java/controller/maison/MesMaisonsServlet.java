package com.examen_java.controller.maison;

import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;
import com.examen_java.dao.MediaMaisonDAO;
import com.examen_java.dao.MediaMaisonDAOImpl;
import com.examen_java.modele.Maison;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MesMaisonsServlet extends HttpServlet {

    private MaisonDAO maisonDAO;
    private MediaMaisonDAO mediaMaisonDAO;

    @Override
    public void init() throws ServletException {
        this.maisonDAO = new MaisonDAOImpl();
        this.mediaMaisonDAO = new MediaMaisonDAOImpl();
        System.out.println("[MesMaisonsServlet] Initialisée.");
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

        
        List<Maison> toutesLesMaisons = maisonDAO.trouverParProprietaire(idProprietaire);

        
        String filtre = request.getParameter("filtre");
        List<Maison> maisons = new ArrayList<>();

        if ("actives".equals(filtre)) {
            
            for (Maison m : toutesLesMaisons) {
                if (m.isEstVisible()) {
                    maisons.add(m);
                }
            }
            request.setAttribute("filtreActif", "actives");
        } else if ("masquees".equals(filtre)) {
            
            for (Maison m : toutesLesMaisons) {
                if (!m.isEstVisible()) {
                    maisons.add(m);
                }
            }
            request.setAttribute("filtreActif", "masquees");
        }  else {
            
            
            
            for (Maison m : toutesLesMaisons) {
                if (m.isEstVisible()) {
                    maisons.add(m);
                }
            }
            request.setAttribute("filtreActif", "actives");
        }

        
        for (Maison maison : maisons) {
            maison.setPhotoPrincipale(mediaMaisonDAO.trouverPhotoPrincipale(maison.getIdMaison()));
        }

        request.setAttribute("maisons", maisons);
        request.setAttribute("nombreMaisons", maisons.size());
        request.setAttribute("nombreTotal", toutesLesMaisons.size());

        request.getRequestDispatcher("/WEB-INF/vues/maison/mes-maisons.jsp")
                .forward(request, response);
    }
}