<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="com.examen_java.modele.MediaMaison" %>
<%@ page import="java.util.List" %>
<%
    List<Maison> maisons = (List<Maison>) request.getAttribute("maisons");
    int nombreMaisons = (maisons != null) ? maisons.size() : 0;
    String message = null;
    
    if ("succes".equals(request.getParameter("ajout"))) {
        message = "Votre annonce a été publiée avec succès !";
    } else if ("succes".equals(request.getParameter("modification"))) {
        message = "Votre annonce a été modifiée avec succès !";
    } else if ("succes".equals(request.getParameter("suppression"))) {
        message = "Votre annonce a été désactivée.";
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes annonces - Location Maisons</title>

   
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

   
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/maison-style.css">
</head>
<body>

   
    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

 
    <main class="maison-main">
        <div class="maison-container">

         
            <div class="maison-entete">
                <div class="entete-gauche">
                    <h1>
                        <i class="fa-solid fa-house-laptop"></i> 
                        Mes annonces
                    </h1>
                    <p>
                        <% if (nombreMaisons > 0) { %>
                            Vous avez <strong><%= nombreMaisons %></strong> annonce<%= nombreMaisons > 1 ? "s" : "" %> publiée<%= nombreMaisons > 1 ? "s" : "" %>.
                        <% } else { %>
                            Vous n'avez pas encore publié d'annonce.
                        <% } %>
                    </p>
                </div>
                <div class="entete-droite">
                    <a href="${pageContext.request.contextPath}/maison/ajouter" class="bouton-ajouter">
                        <i class="fa-solid fa-circle-plus"></i>
                        Ajouter une maison
                    </a>
                </div>
            </div>
                        
           
            <%
                String filtreActif = (String) request.getAttribute("filtreActif");
                if (filtreActif == null) filtreActif = "actives";
            %>
            <div style="display:flex;gap:8px;margin-bottom:20px;flex-wrap:wrap;">
                <a href="${pageContext.request.contextPath}/mes-maisons?filtre=actives" 
                   style="display:inline-flex;align-items:center;gap:7px;padding:10px 20px;background:<%= "actives".equals(filtreActif) ? "#eff6ff" : "#ffffff" %>;border:2px solid <%= "actives".equals(filtreActif) ? "#3b82f6" : "#e2e8f0" %>;border-radius:10px;font-size:13.5px;font-weight:<%= "actives".equals(filtreActif) ? "600" : "500" %>;color:<%= "actives".equals(filtreActif) ? "#2563eb" : "#64748b" %>;text-decoration:none;transition:all 0.3s ease;">
                    <i class="fa-solid fa-eye"></i> Actives
                </a>
                <a href="${pageContext.request.contextPath}/mes-maisons?filtre=masquees" 
                   style="display:inline-flex;align-items:center;gap:7px;padding:10px 20px;background:<%= "masquees".equals(filtreActif) ? "#eff6ff" : "#ffffff" %>;border:2px solid <%= "masquees".equals(filtreActif) ? "#3b82f6" : "#e2e8f0" %>;border-radius:10px;font-size:13.5px;font-weight:<%= "masquees".equals(filtreActif) ? "600" : "500" %>;color:<%= "masquees".equals(filtreActif) ? "#2563eb" : "#64748b" %>;text-decoration:none;transition:all 0.3s ease;">
                    <i class="fa-solid fa-eye-slash"></i> Masquées
                </a>
                <a href="${pageContext.request.contextPath}/mes-maisons?filtre=tous" 
                   style="display:inline-flex;align-items:center;gap:7px;padding:10px 20px;background:<%= "tous".equals(filtreActif) ? "#eff6ff" : "#ffffff" %>;border:2px solid <%= "tous".equals(filtreActif) ? "#3b82f6" : "#e2e8f0" %>;border-radius:10px;font-size:13.5px;font-weight:<%= "tous".equals(filtreActif) ? "600" : "500" %>;color:<%= "tous".equals(filtreActif) ? "#2563eb" : "#64748b" %>;text-decoration:none;transition:all 0.3s ease;">
                    <i class="fa-solid fa-list"></i> Toutes
                </a>
            </div>
        

           
            <% if (message != null) { %>
                <div class="message-succes">
                    <i class="fa-solid fa-circle-check"></i>
                    <span><%= message %></span>
                </div>
            <% } %>

          
            <% if (request.getAttribute("erreur") != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= request.getAttribute("erreur") %></span>
                </div>
            <% } %>

            
            <% if (maisons != null && !maisons.isEmpty()) { %>
                <div class="grille-maisons">

                    <% for (Maison maison : maisons) { %>
                        <div class="carte-maison">
                            
                          
                            <div class="carte-maison-image">
                                <% 
                                    String cheminPhoto = maison.getCheminPhotoPrincipale();
                                    if (cheminPhoto != null && !cheminPhoto.isEmpty()) { 
                                %>
                                    <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= cheminPhoto %>" 
                                         alt="<%= maison.getTitre() %>">
                                <% } else { %>
                                    <div class="image-placeholder">
                                        <i class="fa-solid fa-house"></i>
                                    </div>
                                <% } %>
                                
                                
                                <span class="badge-visibilite <%= maison.isEstVisible() ? "badge-visible" : "badge-cache" %>">
                                    <i class="fa-solid <%= maison.isEstVisible() ? "fa-eye" : "fa-eye-slash" %>"></i>
                                    <%= maison.isEstVisible() ? "Visible" : "Masquée" %>
                                </span>
                            </div>

                       
                            <div class="carte-maison-contenu">
                                <h3 class="maison-titre"><%= maison.getTitre() %></h3>
                                
                                <div class="maison-adresse">
                                    <i class="fa-solid fa-location-dot"></i>
                                    <span><%= maison.getVille() %><%= maison.getCodePostal() != null ? ", " + maison.getCodePostal() : "" %></span>
                                </div>

                                <div class="maison-caracteristiques">
                                    <span class="carac-item">
                                        <i class="fa-solid fa-bed"></i> <%= maison.getNombreChambres() %> ch.
                                    </span>
                                    <span class="carac-item">
                                        <i class="fa-solid fa-bath"></i> <%= maison.getNombreSallesBain() %> sdb
                                    </span>
                                    <span class="carac-item">
                                        <i class="fa-solid fa-users"></i> <%= maison.getCapacitePersonnes() %> pers.
                                    </span>
                                    <% if (maison.getSuperficieMetresCarres() > 0) { %>
                                        <span class="carac-item">
                                            <i class="fa-solid fa-maximize"></i> <%= maison.getSuperficieMetresCarres() %> m²
                                        </span>
                                    <% } %>
                                </div>

                                <div class="maison-prix">
                                    <span class="prix-valeur"><%= String.format("%.0f", maison.getPrixBaseNuit()) %> €</span>
                                    <span class="prix-periode">/ nuit</span>
                                </div>

                                
                                <div class="carte-maison-actions">
                                    <a href="${pageContext.request.contextPath}/maison/modifier?id=<%= maison.getIdMaison() %>" 
                                       class="bouton-action bouton-modifier" 
                                       title="Modifier">
                                        <i class="fa-solid fa-pen-to-square"></i> Modifier
                                    </a>
                                    <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?maison=<%= maison.getIdMaison() %>" 
                                       class="bouton-action bouton-reservations" 
                                       title="Voir les réservations">
                                        <i class="fa-solid fa-book-bookmark"></i> Réservations
                                    </a>
                                    <a href="${pageContext.request.contextPath}/maison/supprimer?id=<%= maison.getIdMaison() %>" 
                                       class="bouton-action bouton-desactiver" 
                                       title="<%= maison.isEstVisible() ? "Désactiver" : "Réactiver" %>"
                                       onclick="return confirm('Voulez-vous vraiment <%= maison.isEstVisible() ? "désactiver" : "réactiver" %> cette annonce ?');">
                                        <i class="fa-solid <%= maison.isEstVisible() ? "fa-eye-slash" : "fa-eye" %>"></i> 
                                        <%= maison.isEstVisible() ? "Désactiver" : "Réactiver" %>
                                    </a>
                                </div>
                            </div>
                        </div>
                    <% } %>

                </div>
            <% } else { %>

               
                <div class="etat-vide-maison">
                    <div class="vide-illustration">
                        <i class="fa-solid fa-house-circle-exclamation"></i>
                    </div>
                    <h2>Vous n'avez pas encore d'annonce</h2>
                    <p>Publiez votre première maison et commencez à recevoir des réservations dès aujourd'hui.</p>
                    <div class="vide-avantages">
                        <div class="avantage-item">
                            <i class="fa-solid fa-check-circle"></i>
                            <span>Publication gratuite et illimitée</span>
                        </div>
                        <div class="avantage-item">
                            <i class="fa-solid fa-check-circle"></i>
                            <span>Gagnez des revenus dès la première réservation</span>
                        </div>
                        <div class="avantage-item">
                            <i class="fa-solid fa-check-circle"></i>
                            <span>Gérez tout depuis votre tableau de bord</span>
                        </div>
                    </div>
                    <a href="${pageContext.request.contextPath}/maison/ajouter" class="bouton-ajouter-grand">
                        <i class="fa-solid fa-circle-plus"></i> Publier ma première annonce
                    </a>
                </div>

            <% } %>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />
   
    <script src="${pageContext.request.contextPath}/assets/js/maison-script.js"></script>

</body>
</html>