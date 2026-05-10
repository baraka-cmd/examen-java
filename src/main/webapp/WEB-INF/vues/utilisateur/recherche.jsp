<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="java.util.List" %>
<%
    List<Maison> maisons = (List<Maison>) request.getAttribute("maisons");
    int nombreResultats = (Integer) request.getAttribute("nombreResultats");
    boolean rechercheLancee = (Boolean) request.getAttribute("rechercheLancee");
    String messageErreur = (String) request.getAttribute("erreur");
    
    String villeSaisie = (String) request.getAttribute("villeSaisie");
    String dateDebutSaisie = (String) request.getAttribute("dateDebutSaisie");
    String dateFinSaisie = (String) request.getAttribute("dateFinSaisie");
    String capaciteSaisie = (String) request.getAttribute("capaciteSaisie");
    String prixMaxSaisie = (String) request.getAttribute("prixMaxSaisie");
    
    String role = (String) session.getAttribute("role");
    boolean estConnecte = (session.getAttribute("idUtilisateur") != null);
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rechercher une maison - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/recherche-style.css">
</head>
<body>

    <% if (estConnecte) { %>
        <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />
    <% } else { %>
        <header style="background:#fff;padding:15px 30px;border-bottom:1px solid #e2e8f0;display:flex;justify-content:space-between;align-items:center;">
            <a href="${pageContext.request.contextPath}/" style="font-size:20px;font-weight:700;color:#0f172a;text-decoration:none;">
                🏠 Location Maisons
            </a>
            <div style="display:flex;gap:12px;">
                <a href="${pageContext.request.contextPath}/connexion" style="padding:10px 20px;background:#3b82f6;color:#fff;border-radius:10px;text-decoration:none;font-weight:600;">Connexion</a>
            </div>
        </header>
    <% } %>

    <main class="recherche-main">
        <div class="recherche-container">

            
            <section class="recherche-hero">
                <div class="hero-contenu">
                    <h1>Trouvez la maison de vos rêves</h1>
                    <p>Des centaines de maisons disponibles pour vos prochaines vacances.</p>
                </div>
                <div class="hero-formulaire">
                    <form action="${pageContext.request.contextPath}/recherche" method="get" class="formulaire-recherche">
                        <div class="recherche-champs">
                            <div class="recherche-champ">
                                <label><i class="fa-solid fa-location-dot"></i> Destination</label>
                                <input type="text" name="ville" placeholder="Ville, région..." 
                                       value="<%= villeSaisie != null ? villeSaisie : "" %>">
                            </div>
                            <div class="recherche-champ">
                                <label><i class="fa-solid fa-calendar"></i> Arrivée</label>
                                <input type="date" name="dateDebut" 
                                       value="<%= dateDebutSaisie != null ? dateDebutSaisie : "" %>">
                            </div>
                            <div class="recherche-champ">
                                <label><i class="fa-solid fa-calendar"></i> Départ</label>
                                <input type="date" name="dateFin" 
                                       value="<%= dateFinSaisie != null ? dateFinSaisie : "" %>">
                            </div>
                            <div class="recherche-champ">
                                <label><i class="fa-solid fa-users"></i> Voyageurs</label>
                                <input type="number" name="capacite" min="1" placeholder="Nombre" 
                                       value="<%= capaciteSaisie != null ? capaciteSaisie : "" %>">
                            </div>
                            <div class="recherche-champ">
                                <label><i class="fa-solid fa-euro-sign"></i> Prix max/nuit</label>
                                <input type="number" name="prixMax" min="0" placeholder="Budget" 
                                       value="<%= prixMaxSaisie != null ? prixMaxSaisie : "" %>">
                            </div>
                        </div>
                        <button type="submit" class="bouton-rechercher">
                            <i class="fa-solid fa-magnifying-glass"></i> Rechercher
                        </button>
                    </form>
                </div>
            </section>

            
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

            
            <section class="recherche-resultats">
                <div class="resultats-entete">
                    <h2>
                        <% if (rechercheLancee) { %>
                            <i class="fa-solid fa-magnifying-glass"></i> 
                            <%= nombreResultats %> résultat<%= nombreResultats > 1 ? "s" : "" %> trouvé<%= nombreResultats > 1 ? "s" : "" %>
                        <% } else { %>
                            <i class="fa-solid fa-house"></i> 
                            Dernières maisons publiées
                        <% } %>
                    </h2>
                    <% if (rechercheLancee) { %>
                        <a href="${pageContext.request.contextPath}/recherche" class="lien-tout-voir">
                            <i class="fa-solid fa-rotate"></i> Réinitialiser
                        </a>
                    <% } %>
                </div>

                <% if (maisons != null && !maisons.isEmpty()) { %>
                    <div class="grille-resultats">
                        <% for (Maison m : maisons) { %>
                            <a href="${pageContext.request.contextPath}/maison/detail?id=<%= m.getIdMaison() %>" class="carte-resultat">
                                <!-- Photo -->
                                <div class="carte-resultat-image">
                                    <% if (m.getPhotoPrincipale() != null && m.getPhotoPrincipale().getCheminFichier() != null) { %>
                                        <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= m.getPhotoPrincipale().getCheminFichier() %>" 
                                             alt="<%= m.getTitre() %>">
                                    <% } else { %>
                                        <div class="image-placeholder">
                                            <i class="fa-solid fa-house"></i>
                                        </div>
                                    <% } %>
                                    <!-- Prix -->
                                    <span class="badge-prix"><%= String.format("%.0f", m.getPrixBaseNuit()) %> €<small>/nuit</small></span>
                                </div>

                                <!-- Infos -->
                                <div class="carte-resultat-infos">
                                    <h3><%= m.getTitre() %></h3>
                                    <div class="resultat-adresse">
                                        <i class="fa-solid fa-location-dot"></i>
                                        <span><%= m.getVille() %><%= m.getPays() != null ? ", " + m.getPays() : "" %></span>
                                    </div>
                                    <div class="resultat-caracteristiques">
                                        <span><i class="fa-solid fa-users"></i> <%= m.getCapacitePersonnes() %> pers.</span>
                                        <span><i class="fa-solid fa-bed"></i> <%= m.getNombreChambres() %> ch.</span>
                                        <span><i class="fa-solid fa-bath"></i> <%= m.getNombreSallesBain() %> sdb</span>
                                    </div>
                                </div>
                            </a>
                        <% } %>
                    </div>
                <% } else { %>
                   
                    <div class="etat-vide-recherche">
                        <div class="vide-illustration">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </div>
                        <h3>Aucun résultat trouvé</h3>
                        <p>
                            <% if (rechercheLancee) { %>
                                Essayez d'élargir votre recherche avec moins de filtres.
                            <% } else { %>
                                Aucune maison n'est disponible pour le moment. Revenez bientôt !
                            <% } %>
                        </p>
                        <% if (rechercheLancee) { %>
                            <a href="${pageContext.request.contextPath}/recherche" class="bouton-primaire">
                                <i class="fa-solid fa-rotate"></i> Réinitialiser la recherche
                            </a>
                        <% } %>
                    </div>
                <% } %>
            </section>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/recherche-script.js"></script>

</body>
</html>