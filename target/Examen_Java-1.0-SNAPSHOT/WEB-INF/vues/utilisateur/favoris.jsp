<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="java.util.List" %>
<%
    List<Maison> favoris = (List<Maison>) request.getAttribute("favoris");
    int nombreFavoris = (Integer) request.getAttribute("nombreFavoris");
    String messageSucces = (String) request.getAttribute("succes");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes favoris - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/favoris-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="favoris-main">
        <div class="favoris-container">

            
            <div class="favoris-entete">
                <div class="entete-gauche">
                    <a href="${pageContext.request.contextPath}/dashboard" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour au dashboard
                    </a>
                    <h1>
                        <i class="fa-solid fa-heart"></i> 
                        Mes favoris
                    </h1>
                    <p>
                        <% if (nombreFavoris > 0) { %>
                            <strong><%= nombreFavoris %></strong> maison<%= nombreFavoris > 1 ? "s" : "" %> sauvegardée<%= nombreFavoris > 1 ? "s" : "" %>
                        <% } else { %>
                            Vous n'avez pas encore de favoris.
                        <% } %>
                    </p>
                </div>
                <div class="entete-droite">
                    <a href="${pageContext.request.contextPath}/recherche" class="bouton-primaire">
                        <i class="fa-solid fa-magnifying-glass"></i> Explorer des maisons
                    </a>
                </div>
            </div>

            
            <% if (messageSucces != null) { %>
                <div class="message-succes">
                    <i class="fa-solid fa-circle-check"></i>
                    <span><%= messageSucces %></span>
                </div>
            <% } %>

            
            <% if (favoris != null && !favoris.isEmpty()) { %>
                <div class="favoris-grille">

                    <% for (Maison m : favoris) { %>
                        <div class="carte-favori">
                            
                            <a href="${pageContext.request.contextPath}/favoris/supprimer?idMaison=<%= m.getIdMaison() %>&redirect=favoris" 
                               class="bouton-retirer-favori" 
                               title="Retirer des favoris"
                               onclick="return confirm('Retirer cette maison de vos favoris ?');">
                                <i class="fa-solid fa-xmark"></i>
                            </a>

                            
                            <a href="${pageContext.request.contextPath}/maison/detail?id=<%= m.getIdMaison() %>" class="carte-favori-image">
                                <% if (m.getPhotoPrincipale() != null && m.getPhotoPrincipale().getCheminFichier() != null) { %>
                                    <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= m.getPhotoPrincipale().getCheminFichier() %>" 
                                         alt="<%= m.getTitre() %>">
                                <% } else { %>
                                    <div class="image-placeholder">
                                        <i class="fa-solid fa-house"></i>
                                    </div>
                                <% } %>
                                
                                <span class="badge-prix-favori"><%= String.format("%.0f", m.getPrixBaseNuit()) %> €<small>/nuit</small></span>
                            </a>

                            
                            <div class="carte-favori-infos">
                                <a href="${pageContext.request.contextPath}/maison/detail?id=<%= m.getIdMaison() %>" class="favori-titre">
                                    <%= m.getTitre() %>
                                </a>
                                <div class="favori-adresse">
                                    <i class="fa-solid fa-location-dot"></i>
                                    <span><%= m.getVille() %>, <%= m.getPays() != null ? m.getPays() : "" %></span>
                                </div>
                                <div class="favori-caracteristiques">
                                    <span><i class="fa-solid fa-users"></i> <%= m.getCapacitePersonnes() %> pers.</span>
                                    <span><i class="fa-solid fa-bed"></i> <%= m.getNombreChambres() %> ch.</span>
                                    <span><i class="fa-solid fa-bath"></i> <%= m.getNombreSallesBain() %> sdb</span>
                                </div>
                                <div class="favori-actions">
                                    <a href="${pageContext.request.contextPath}/maison/detail?id=<%= m.getIdMaison() %>" class="bouton-voir">
                                        <i class="fa-solid fa-eye"></i> Voir l'annonce
                                    </a>
                                </div>
                            </div>
                        </div>
                    <% } %>

                </div>
            <% } else { %>
                
                <div class="etat-vide-favoris">
                    <div class="vide-illustration">
                        <i class="fa-solid fa-heart-crack"></i>
                    </div>
                    <h2>Aucun favori</h2>
                    <p>Vous n'avez pas encore sauvegardé de maison. Cliquez sur le cœur <i class="fa-solid fa-heart" style="color:#ef4444;"></i> depuis une fiche détail pour ajouter une maison à vos favoris.</p>
                    <a href="${pageContext.request.contextPath}/recherche" class="bouton-primaire">
                        <i class="fa-solid fa-magnifying-glass"></i> Découvrir des maisons
                    </a>
                </div>
            <% } %>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/favoris-script.js"></script>

</body>
</html>