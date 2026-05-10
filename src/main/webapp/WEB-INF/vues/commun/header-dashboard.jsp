<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.UtilisateurInscription" %>
<%@ page import="com.examen_java.modele.ProfilInscription" %>
<%@ page import="java.util.List" %>
<%
    
    UtilisateurInscription utilisateur = (UtilisateurInscription) session.getAttribute("utilisateur");
    String role = (String) session.getAttribute("role");
    String nomComplet = (String) session.getAttribute("nomComplet");
    String prenom = (utilisateur != null) ? utilisateur.getPrenom() : "";
    String nom = (utilisateur != null) ? utilisateur.getNom() : "";
    String email = (utilisateur != null) ? utilisateur.getEmail() : "";
    
   
    String initiales = "";
    if (!prenom.isEmpty()) initiales += prenom.charAt(0);
    if (!nom.isEmpty()) initiales += nom.charAt(0);
%>

<header class="header-dashboard">
    <div class="header-container">

        
        <div class="header-logo">
            <a href="${pageContext.request.contextPath}/dashboard" class="logo-lien">
                <div class="logo-icone">
                    <i class="fa-solid fa-house-chimney"></i>
                </div>
                <span class="logo-texte">Location Maisons</span>
            </a>
        </div>

        
        <nav class="header-nav">
            <ul class="nav-liste">

              
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/dashboard" class="nav-lien actif">
                        <i class="fa-solid fa-table-columns"></i>
                        <span>Tableau de bord</span>
                    </a>
                </li>

              
                <% if ("LOCATAIRE".equalsIgnoreCase(role)) { %>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/recherche" class="nav-lien">
                            <i class="fa-solid fa-magnifying-glass-location"></i>
                            <span>Explorer</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/mes-reservations" class="nav-lien">
                            <i class="fa-solid fa-calendar-check"></i>
                            <span>Mes réservations</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/mes-paiements" class="nav-lien">
                            <i class="fa-solid fa-receipt"></i>
                            <span>Mes paiements</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/favoris" class="nav-lien">
                            <i class="fa-solid fa-heart"></i>
                            <span>Mes favoris</span>
                        </a>
                    </li>
                <% } %>

                
                <% if ("PROPRIETAIRE".equalsIgnoreCase(role)) { %>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/mes-maisons" class="nav-lien">
                            <i class="fa-solid fa-house-laptop"></i>
                            <span>Mes annonces</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/maison/ajouter" class="nav-lien">
                            <i class="fa-solid fa-circle-plus"></i>
                            <span>Ajouter une maison</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens" class="nav-lien">
                            <i class="fa-solid fa-book-bookmark"></i>
                            <span>Réservations reçues</span>
                        </a>
                    </li>
                    
                    <li class="nav-item">
                        <a href="${pageContext.request.contextPath}/revenus" class="nav-lien">
                            <i class="fa-solid fa-chart-line"></i>
                            <span>Revenus</span>
                        </a>
                    </li>
                <% } %>

            </ul>
        </nav>

     
        <div class="header-utilisateur">

            
            <%
                // Récupérer le nombre de messages non lus depuis la session
                Integer nombreMessagesNonLus = (Integer) session.getAttribute("nombreMessagesNonLus");
                if (nombreMessagesNonLus == null) nombreMessagesNonLus = 0;
            %>
            <a href="${pageContext.request.contextPath}/messages" 
               class="bouton-messages" 
               title="Messages" 
               aria-label="Messages">
                <i class="fa-solid fa-envelope"></i>
                <% if (nombreMessagesNonLus > 0) { %>
                    <span class="badge badge-message"><%= nombreMessagesNonLus %></span>
                <% } %>
            </a>

           
            <a href="${pageContext.request.contextPath}/notifications" class="bouton-notifications" title="Notifications">
                <i class="fa-solid fa-bell"></i>
            </a>

            
            <div class="menu-profil">
                <button class="bouton-profil" id="boutonProfil" 
                        title="Menu utilisateur" 
                        aria-label="Menu utilisateur"
                        aria-expanded="false"
                        aria-haspopup="true">
                    <div class="avatar">
                        <span class="avatar-initiales"><%= initiales %></span>
                    </div>
                    <span class="profil-nom"><%= nomComplet != null ? nomComplet : "Utilisateur" %></span>
                    <i class="fa-solid fa-chevron-down"></i>
                </button>

                
                <div class="dropdown-menu" id="dropdownProfil">
                    <div class="dropdown-entete">
                        <span class="dropdown-nom"><%= nomComplet != null ? nomComplet : "" %></span>
                        <span class="dropdown-email"><%= email %></span>
                        <span class="dropdown-role">
                            <% if ("LOCATAIRE".equalsIgnoreCase(role)) { %>
                                <i class="fa-solid fa-magnifying-glass-location"></i> Locataire
                            <% } else if ("PROPRIETAIRE".equalsIgnoreCase(role)) { %>
                                <i class="fa-solid fa-house-laptop"></i> Propriétaire
                            <% } %>
                        </span>
                    </div>
                    <div class="dropdown-separateur"></div>
                    <a href="${pageContext.request.contextPath}/mon-profil" class="dropdown-item">
                        <i class="fa-solid fa-user"></i> Mon profil
                    </a>
                    <a href="${pageContext.request.contextPath}/parametres" class="dropdown-item">
                        <i class="fa-solid fa-gear"></i> Paramètres
                    </a>
                    <a href="${pageContext.request.contextPath}/aide" class="dropdown-item">
                        <i class="fa-solid fa-circle-question"></i> Aide
                    </a>
                    <div class="dropdown-separateur"></div>
                    <a href="${pageContext.request.contextPath}/deconnexion" class="dropdown-item dropdown-deconnexion">
                        <i class="fa-solid fa-right-from-bracket"></i> Déconnexion
                    </a>
                </div>
            </div>

        </div>

     
        <button class="bouton-menu-mobile" id="boutonMenuMobile" 
                title="Menu" aria-label="Menu principal" aria-expanded="false">
            <span class="barre-hamburger"></span>
            <span class="barre-hamburger"></span>
            <span class="barre-hamburger"></span>
        </button>

    </div>
</header>


<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header-dashboard.css">


<script src="${pageContext.request.contextPath}/assets/js/header-dashboard.js"></script>