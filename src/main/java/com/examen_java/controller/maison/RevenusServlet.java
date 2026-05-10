package com.examen_java.controller.maison;

import com.examen_java.dao.RevenuDAO;
import com.examen_java.dao.RevenuDAOImpl;
import com.examen_java.modele.Revenu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class RevenusServlet extends HttpServlet {

    private RevenuDAO revenuDAO;

    @Override
    public void init() throws ServletException {
        this.revenuDAO = new RevenuDAOImpl();
        System.out.println("[RevenusServlet] Initialisée.");
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

      
        String periode = request.getParameter("periode");
        String dateDebutParam = request.getParameter("debut");
        String dateFinParam = request.getParameter("fin");

        List<Revenu> revenus;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        if (dateDebutParam != null && !dateDebutParam.trim().isEmpty()
                && dateFinParam != null && !dateFinParam.trim().isEmpty()) {
            
            revenus = revenuDAO.trouverParProprietaireEtPeriode(idProprietaire,
                    dateDebutParam.trim(), dateFinParam.trim());
            request.setAttribute("filtreActif", "personnalise");
        } else if ("mois".equals(periode)) {
            
            cal.set(Calendar.DAY_OF_MONTH, 1);
            String debut = sdf.format(cal.getTime());
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String fin = sdf.format(cal.getTime());
            revenus = revenuDAO.trouverParProprietaireEtPeriode(idProprietaire, debut, fin);
            request.setAttribute("filtreActif", "mois");
        } else if ("trimestre".equals(periode)) {
            
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) / 3) * 3);
            String debut = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH, 3);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            String fin = sdf.format(cal.getTime());
            revenus = revenuDAO.trouverParProprietaireEtPeriode(idProprietaire, debut, fin);
            request.setAttribute("filtreActif", "trimestre");
        } else if ("annee".equals(periode)) {
            
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, 0);
            String debut = sdf.format(cal.getTime());
            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DAY_OF_MONTH, 31);
            String fin = sdf.format(cal.getTime());
            revenus = revenuDAO.trouverParProprietaireEtPeriode(idProprietaire, debut, fin);
            request.setAttribute("filtreActif", "annee");
        } else {
            // Tout
            revenus = revenuDAO.trouverParProprietaire(idProprietaire);
            request.setAttribute("filtreActif", "tout");
        }

        
        double totalGeneral = revenuDAO.calculerTotal(idProprietaire);
        double totalMois = revenuDAO.calculerTotalMoisEnCours(idProprietaire);
        int nombreTransactions = revenuDAO.compterTransactions(idProprietaire);
        List<Object[]> revenuParMaison = revenuDAO.calculerRevenuParMaison(idProprietaire);

        
        double totalFiltre = 0.0;
        for (Revenu r : revenus) {
            if (r.estReussi()) {
                totalFiltre += r.getMontant();
            }
        }

        
        request.setAttribute("revenus", revenus);
        request.setAttribute("totalGeneral", totalGeneral);
        request.setAttribute("totalMois", totalMois);
        request.setAttribute("nombreTransactions", nombreTransactions);
        request.setAttribute("revenuParMaison", revenuParMaison);
        request.setAttribute("totalFiltre", totalFiltre);
        request.setAttribute("nombreRevenus", revenus.size());

       
        request.getRequestDispatcher("/WEB-INF/vues/maison/revenus.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}