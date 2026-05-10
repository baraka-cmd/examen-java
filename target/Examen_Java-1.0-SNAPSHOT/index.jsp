<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="com.examen_java.modele.Avis" %>
<%@ page import="java.util.List" %>
<%
    List<Maison> dernieresMaisons = (List<Maison>) request.getAttribute("dernieresMaisons");
    List<Maison> topMaisons = (List<Maison>) request.getAttribute("topMaisons");
    List<Avis> avisRecents = (List<Avis>) request.getAttribute("avisRecents");
    List<String[]> villesPopulaires = (List<String[]>) request.getAttribute("villesPopulaires");
    
    int totalMaisons = (Integer) request.getAttribute("totalMaisons");
    int totalVilles = (Integer) request.getAttribute("totalVilles");
    int totalAvis = (Integer) request.getAttribute("totalAvis");
    double noteGlobale = (Double) request.getAttribute("noteGlobale");
    
    boolean estConnecte = (Boolean) request.getAttribute("estConnecte");
    String role = (String) request.getAttribute("role");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Location Maisons - Trouvez la maison de vos rêves</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/accueil-style.css">
</head>
<body>

    
    <header class="accueil-header">
        <div class="header-container">
            <a href="${pageContext.request.contextPath}/accueil" class="header-logo">
                <div class="logo-icone">
                    <i class="fa-solid fa-house-chimney"></i>
                </div>
                <span class="logo-texte">Location Maisons</span>
            </a>

            <nav class="header-nav-accueil">
                <a href="${pageContext.request.contextPath}/recherche" class="nav-lien-accueil">Explorer</a>
                <% if (estConnecte) { %>
                    <a href="${pageContext.request.contextPath}/dashboard" class="nav-lien-accueil">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/deconnexion" class="bouton-header bouton-outline">Déconnexion</a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/connexion" class="bouton-header bouton-outline">Connexion</a>
                    <a href="${pageContext.request.contextPath}/inscription" class="bouton-header bouton-plein">Inscription</a>
                <% } %>
            </nav>
        </div>
    </header>

    
    <section class="hero">
        <div class="hero-container">
            <div class="hero-contenu">
                <h1>Trouvez la maison de vos rêves</h1>
                <p>Des centaines de maisons vous attendent pour vos prochaines vacances. Réservez en quelques clics et profitez d'un séjour inoubliable.</p>
                <div class="hero-actions">
                    <a href="${pageContext.request.contextPath}/recherche" class="bouton-hero bouton-hero-primaire">
                        <i class="fa-solid fa-magnifying-glass"></i> Explorer les maisons
                    </a>
                    <% if (!estConnecte) { %>
                        <a href="${pageContext.request.contextPath}/inscription" class="bouton-hero bouton-hero-secondaire">
                            <i class="fa-solid fa-user-plus"></i> Créer un compte
                        </a>
                    <% } %>
                </div>
            </div>
            <div class="hero-image">
                <img src="${pageContext.request.contextPath}/assets/images/hero-house.jpg" 
                     alt="Maison de vacances" 
                     onerror="this.style.display='none';this.parentElement.innerHTML='<div style=width:100%;height:100%;display:flex;align-items:center;justify-content:center;background:rgba(255,255,255,0.06);border-radius:24px;color:rgba(255,255,255,0.3);font-size:80px;><i class=fa-solid fa-house-chimney></i></div>';">
            </div>
        </div>
    </section>

    
    <section class="section-chiffres">
        <div class="section-container">
            <div class="chiffres-grille">
                <div class="chiffre-card">
                    <div class="chiffre-icone"><i class="fa-solid fa-house"></i></div>
                    <span class="chiffre-valeur" data-cible="<%= totalMaisons %>">0</span>
                    <span class="chiffre-label">Maisons disponibles</span>
                </div>
                <div class="chiffre-card">
                    <div class="chiffre-icone"><i class="fa-solid fa-city"></i></div>
                    <span class="chiffre-valeur" data-cible="<%= totalVilles %>">0</span>
                    <span class="chiffre-label">Villes</span>
                </div>
                <div class="chiffre-card">
                    <div class="chiffre-icone"><i class="fa-solid fa-star"></i></div>
                    <span class="chiffre-valeur" data-cible="<%= totalAvis %>">0</span>
                    <span class="chiffre-label">Avis vérifiés</span>
                </div>
                <div class="chiffre-card">
                    <div class="chiffre-icone"><i class="fa-solid fa-face-smile"></i></div>
                    <span class="chiffre-valeur" data-cible="<%= (int)(noteGlobale * 10) %>"><%= String.format("%.1f", noteGlobale) %></span>
                    <span class="chiffre-label">/ 5 de satisfaction</span>
                </div>
            </div>
        </div>
    </section>

    
    <section class="section-annonces">
        <div class="section-container">
            <div class="section-entete-accueil">
                <div>
                    <h2>Dernières annonces</h2>
                    <p>Découvrez les maisons récemment ajoutées par nos propriétaires.</p>
                </div>
                <a href="${pageContext.request.contextPath}/recherche" class="bouton-voir-tout">
                    Voir tout <i class="fa-solid fa-arrow-right"></i>
                </a>
            </div>

            <% if (dernieresMaisons != null && !dernieresMaisons.isEmpty()) { %>
                <div class="annonces-grille">
                    <% for (Maison m : dernieresMaisons) { %>
                        <a href="${pageContext.request.contextPath}/maison/detail?id=<%= m.getIdMaison() %>" class="carte-annonce">
                            <div class="carte-annonce-image">
                                <% if (m.getPhotoPrincipale() != null && m.getPhotoPrincipale().getCheminFichier() != null) { %>
                                    <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= m.getPhotoPrincipale().getCheminFichier() %>" alt="<%= m.getTitre() %>">
                                <% } else { %>
                                    <div class="image-placeholder-accueil"><i class="fa-solid fa-house"></i></div>
                                <% } %>
                                <span class="badge-prix-accueil"><%= String.format("%.0f", m.getPrixBaseNuit()) %> €<small>/nuit</small></span>
                            </div>
                            <div class="carte-annonce-infos">
                                <h3><%= m.getTitre() %></h3>
                                <div class="annonce-adresse"><i class="fa-solid fa-location-dot"></i> <%= m.getVille() %>, <%= m.getPays() != null ? m.getPays() : "" %></div>
                                <div class="annonce-carac">
                                    <span><i class="fa-solid fa-users"></i> <%= m.getCapacitePersonnes() %> pers.</span>
                                    <span><i class="fa-solid fa-bed"></i> <%= m.getNombreChambres() %> ch.</span>
                                </div>
                            </div>
                        </a>
                    <% } %>
                </div>
            <% } else { %>
                <div class="etat-vide-accueil">
                    <i class="fa-solid fa-house-circle-exclamation"></i>
                    <p>Aucune annonce publiée pour le moment. Revenez bientôt !</p>
                </div>
            <% } %>
        </div>
    </section>

    
    <% if (topMaisons != null && !topMaisons.isEmpty()) { %>
    <section class="section-top-maisons">
        <div class="section-container">
            <div class="section-entete-accueil">
                <div>
                    <h2><i class="fa-solid fa-trophy" style="color:#f59e0b;"></i> Les mieux notées</h2>
                    <p>Les maisons préférées de nos locataires.</p>
                </div>
                <a href="${pageContext.request.contextPath}/recherche" class="bouton-voir-tout">
                    Voir tout <i class="fa-solid fa-arrow-right"></i>
                </a>
            </div>

            <div class="top-maisons-grille">
                <% for (Maison m : topMaisons) { %>
                    <a href="${pageContext.request.contextPath}/maison/detail?id=<%= m.getIdMaison() %>" class="carte-top-maison">
                        <div class="carte-top-image">
                            <% if (m.getPhotoPrincipale() != null && m.getPhotoPrincipale().getCheminFichier() != null) { %>
                                <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= m.getPhotoPrincipale().getCheminFichier() %>" alt="<%= m.getTitre() %>">
                            <% } else { %>
                                <div class="image-placeholder-accueil"><i class="fa-solid fa-house"></i></div>
                            <% } %>
                            <span class="badge-top">Coup de cœur</span>
                        </div>
                        <div class="carte-top-infos">
                            <h3><%= m.getTitre() %></h3>
                            <div class="top-adresse"><i class="fa-solid fa-location-dot"></i> <%= m.getVille() %></div>
                            <div class="top-prix"><%= String.format("%.0f", m.getPrixBaseNuit()) %> €<small>/nuit</small></div>
                        </div>
                    </a>
                <% } %>
            </div>
        </div>
    </section>
    <% } %>

    
    <% if (villesPopulaires != null && !villesPopulaires.isEmpty()) { %>
    <section class="section-villes">
        <div class="section-container">
            <div class="section-entete-accueil">
                <div>
                    <h2>Villes populaires</h2>
                    <p>Découvrez nos destinations les plus demandées.</p>
                </div>
                <a href="${pageContext.request.contextPath}/recherche" class="bouton-voir-tout">
                    Toutes les villes <i class="fa-solid fa-arrow-right"></i>
                </a>
            </div>

            <div class="villes-grille">
                <% for (String[] ville : villesPopulaires) { %>
                    <a href="${pageContext.request.contextPath}/recherche?ville=<%= ville[0] %>" class="carte-ville">
                        <div class="carte-ville-fond">
                            <i class="fa-solid fa-city"></i>
                        </div>
                        <div class="carte-ville-infos">
                            <span class="ville-nom"><%= ville[0] %></span>
                            <span class="ville-nombre"><%= ville[1] %> maison<%= Integer.parseInt(ville[1]) > 1 ? "s" : "" %></span>
                        </div>
                    </a>
                <% } %>
            </div>
        </div>
    </section>
    <% } %>

    
    <% if (avisRecents != null && !avisRecents.isEmpty()) { %>
    <section class="section-avis">
        <div class="section-container">
            <div class="section-entete-accueil">
                <div>
                    <h2>Ce que disent nos locataires</h2>
                    <p>Des expériences authentiques partagées par notre communauté.</p>
                </div>
            </div>

            <div class="avis-grille">
                <% for (Avis a : avisRecents) { %>
                    <div class="carte-avis">
                        <div class="avis-etoiles">
                            <% for (int i = 1; i <= 5; i++) { %>
                                <%= i <= Math.round(a.getNoteMoyenne()) ? "★" : "☆" %>
                            <% } %>
                        </div>
                        <p class="avis-texte">"<%= a.getCommentaire() != null && a.getCommentaire().length() > 120 ? a.getCommentaire().substring(0, 120) + "..." : a.getCommentaire() %>"</p>
                        <div class="avis-auteur">
                            <span class="avis-auteur-avatar"><%= a.getAuteurPrenom() != null && !a.getAuteurPrenom().isEmpty() ? a.getAuteurPrenom().charAt(0) : "?" %></span>
                            <div>
                                <strong><%= a.getAuteurNomComplet() %></strong>
                                <span>Sur <%= a.getMaisonTitre() %></span>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        </div>
    </section>
    <% } %>

    
    <section class="section-fonctionnement">
        <div class="section-container">
            <div class="section-entete-accueil">
                <div>
                    <h2>Comment ça marche ?</h2>
                    <p>Trois étapes simples pour trouver votre prochain séjour.</p>
                </div>
            </div>

            <div class="etapes-grille">
                <div class="etape-card">
                    <div class="etape-icone"><i class="fa-solid fa-magnifying-glass-location"></i></div>
                    <h3>1. Recherchez</h3>
                    <p>Parcourez les annonces et trouvez la maison idéale selon vos critères.</p>
                </div>
                <div class="etape-card">
                    <div class="etape-icone"><i class="fa-solid fa-calendar-check"></i></div>
                    <h3>2. Réservez</h3>
                    <p>Choisissez vos dates et confirmez votre réservation en quelques clics.</p>
                </div>
                <div class="etape-card">
                    <div class="etape-icone"><i class="fa-solid fa-house-chimney"></i></div>
                    <h3>3. Profitez</h3>
                    <p>Installez-vous et profitez de votre séjour en toute sérénité.</p>
                </div>
            </div>
        </div>
    </section>

    
    <section class="section-confiance">
        <div class="section-container">
            <div class="confiance-grille">
                <div class="confiance-item">
                    <i class="fa-solid fa-shield-halved"></i>
                    <span>Paiement 100% sécurisé</span>
                </div>
                <div class="confiance-item">
                    <i class="fa-solid fa-rotate-left"></i>
                    <span>Annulation flexible</span>
                </div>
                <div class="confiance-item">
                    <i class="fa-solid fa-headset"></i>
                    <span>Support client 24/7</span>
                </div>
                <div class="confiance-item">
                    <i class="fa-solid fa-user-check"></i>
                    <span>Propriétaires vérifiés</span>
                </div>
            </div>
        </div>
    </section>

    
    <section class="section-proprietaires">
        <div class="section-container">
            <div class="proprio-contenu">
                <div class="proprio-texte">
                    <h2>Vous êtes propriétaire ?</h2>
                    <p>Publiez votre maison et commencez à générer des revenus dès aujourd'hui. Gérez vos annonces, vos réservations et vos revenus depuis un tableau de bord simple et intuitif.</p>
                    <ul class="proprio-avantages">
                        <li><i class="fa-solid fa-circle-check"></i> Publication gratuite et illimitée</li>
                        <li><i class="fa-solid fa-circle-check"></i> Gestion complète des réservations</li>
                        <li><i class="fa-solid fa-circle-check"></i> Paiements sécurisés</li>
                        <li><i class="fa-solid fa-circle-check"></i> Support prioritaire 24/7</li>
                    </ul>
                    <a href="${pageContext.request.contextPath}/inscription" class="bouton-hero bouton-hero-primaire">
                        <i class="fa-solid fa-house-laptop"></i> Publier ma maison
                    </a>
                </div>
                <div class="proprio-illustration">
                    <i class="fa-solid fa-house-laptop"></i>
                </div>
            </div>
        </div>
    </section>

   
    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/accueil-script.js"></script>

</body>
</html>