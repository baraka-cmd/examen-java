package com.examen_java.controller.maison;

import com.examen_java.dao.MaisonDAO;
import com.examen_java.dao.MaisonDAOImpl;
import com.examen_java.dao.MediaMaisonDAO;
import com.examen_java.dao.MediaMaisonDAOImpl;
import com.examen_java.modele.Maison;
import com.examen_java.modele.MediaMaison;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;


@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class AjouterMaisonServlet extends HttpServlet {

    private static final String DOSSIER_UPLOADS = "assets/images/uploads";

    private MaisonDAO maisonDAO;
    private MediaMaisonDAO mediaMaisonDAO;

    @Override
    public void init() throws ServletException {
        this.maisonDAO = new MaisonDAOImpl();
        this.mediaMaisonDAO = new MediaMaisonDAOImpl();
        System.out.println("[AjouterMaisonServlet] Initialisée.");
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

        request.getRequestDispatcher("/WEB-INF/vues/maison/ajouter-maison.jsp")
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

        int idProprietaire = (Integer) session.getAttribute("idUtilisateur");

        
        String titre = request.getParameter("titre");
        String description = request.getParameter("description");
        String prixStr = request.getParameter("prixBaseNuit");
        String capaciteStr = request.getParameter("capacitePersonnes");
        String chambresStr = request.getParameter("nombreChambres");
        String sallesBainStr = request.getParameter("nombreSallesBain");
        String superficieStr = request.getParameter("superficie");
        String adresseLigne1 = request.getParameter("adresseLigne1");
        String adresseLigne2 = request.getParameter("adresseLigne2");
        String ville = request.getParameter("ville");
        String codePostal = request.getParameter("codePostal");
        String pays = request.getParameter("pays");
        String reglementInterieur = request.getParameter("reglementInterieur");

       
        String[] equipementsArray = request.getParameterValues("equipements");
        String equipements = "[]";
        if (equipementsArray != null && equipementsArray.length > 0) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < equipementsArray.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append("\"").append(equipementsArray[i]).append("\"");
            }
            sb.append("]");
            equipements = sb.toString();
        }

        System.out.println("[AjouterMaisonServlet] Équipements reçus : " + equipements);

        
        String erreur = null;

        if (titre == null || titre.trim().isEmpty()) {
            erreur = "Le titre est obligatoire.";
        } else if (prixStr == null || prixStr.trim().isEmpty()) {
            erreur = "Le prix par nuit est obligatoire.";
        } else if (capaciteStr == null || capaciteStr.trim().isEmpty()) {
            erreur = "La capacité est obligatoire.";
        } else if (ville == null || ville.trim().isEmpty()) {
            erreur = "La ville est obligatoire.";
        } else if (adresseLigne1 == null || adresseLigne1.trim().isEmpty()) {
            erreur = "L'adresse est obligatoire.";
        }

        double prixBaseNuit = 0;
        int capacitePersonnes = 1;
        int nombreChambres = 1;
        int nombreSallesBain = 1;
        int superficie = 0;

        try {
            if (prixStr != null && !prixStr.trim().isEmpty()) {
                prixBaseNuit = Double.parseDouble(prixStr.trim());
                if (prixBaseNuit <= 0) erreur = "Le prix doit être supérieur à 0.";
            }
            if (capaciteStr != null && !capaciteStr.trim().isEmpty()) {
                capacitePersonnes = Integer.parseInt(capaciteStr.trim());
                if (capacitePersonnes <= 0) erreur = "La capacité doit être supérieure à 0.";
            }
            if (chambresStr != null && !chambresStr.trim().isEmpty()) {
                nombreChambres = Integer.parseInt(chambresStr.trim());
            }
            if (sallesBainStr != null && !sallesBainStr.trim().isEmpty()) {
                nombreSallesBain = Integer.parseInt(sallesBainStr.trim());
            }
            if (superficieStr != null && !superficieStr.trim().isEmpty()) {
                superficie = Integer.parseInt(superficieStr.trim());
            }
        } catch (NumberFormatException e) {
            erreur = "Veuillez saisir des nombres valides.";
        }

        if (erreur != null) {
            request.setAttribute("erreur", erreur);
            request.getRequestDispatcher("/WEB-INF/vues/maison/ajouter-maison.jsp")
                    .forward(request, response);
            return;
        }

        
        Maison maison = new Maison();
        maison.setIdProprietaire(idProprietaire);
        maison.setTitre(titre);
        maison.setDescription(description);
        maison.setPrixBaseNuit(prixBaseNuit);
        maison.setCapacitePersonnes(capacitePersonnes);
        maison.setNombreChambres(nombreChambres);
        maison.setNombreSallesBain(nombreSallesBain);
        maison.setSuperficieMetresCarres(superficie);
        maison.setAdresseLigne1(adresseLigne1);
        maison.setAdresseLigne2(adresseLigne2);
        maison.setVille(ville);
        maison.setCodePostal(codePostal);
        maison.setPays(pays != null && !pays.trim().isEmpty() ? pays.trim() : "France");
        maison.setEquipements(equipements);
        maison.setReglementInterieur(reglementInterieur);
        maison.setEstVisible(true);

        int idMaison = maisonDAO.creer(maison);

        if (idMaison <= 0) {
            System.err.println("[AjouterMaisonServlet] ÉCHEC création maison.");
            request.setAttribute("erreur", "Erreur lors de la création de l'annonce. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/vues/maison/ajouter-maison.jsp")
                    .forward(request, response);
            return;
        }

        System.out.println("[AjouterMaisonServlet] Maison créée avec succès - ID=" + idMaison);

        
try {
    String cheminUpload = getServletContext().getRealPath("/") + DOSSIER_UPLOADS;
    File dossierUpload = new File(cheminUpload);
    if (!dossierUpload.exists()) {
        boolean cree = dossierUpload.mkdirs();
        System.out.println("[AjouterMaisonServlet] Dossier uploads créé : " + cree);
    }

    int ordre = 0;
    boolean premierePhoto = true;

    
    for (Part part : request.getParts()) {
        
        if ("photos".equals(part.getName()) && part.getSize() > 0 && part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
            String nomFichier = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            String extension = "";
            int pointIndex = nomFichier.lastIndexOf(".");
            if (pointIndex > 0) {
                extension = nomFichier.substring(pointIndex);
            }
            String nomUnique = UUID.randomUUID().toString() + extension;

           
            part.write(cheminUpload + File.separator + nomUnique);
            System.out.println("[AjouterMaisonServlet] Photo sauvegardée : " + nomUnique);

            
            MediaMaison media = new MediaMaison();
            media.setIdMaison(idMaison);
            media.setCheminFichier(nomUnique);
            media.setEstImagePrincipale(premierePhoto);
            media.setOrdreAffichage(ordre);
            boolean ajoute = mediaMaisonDAO.ajouter(media);
            System.out.println("[AjouterMaisonServlet] Média ajouté : " + ajoute);

            premierePhoto = false;
            ordre++;
        }
    }

    System.out.println("[AjouterMaisonServlet] Total photos uploadées : " + ordre);

} catch (Exception e) {
    System.err.println("[AjouterMaisonServlet] Erreur upload photos : " + e.getMessage());
    e.printStackTrace();
}

        
        response.sendRedirect(request.getContextPath() + "/mes-maisons?ajout=succes");
    }
}