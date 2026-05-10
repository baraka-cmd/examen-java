<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.UtilisateurInscription" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="com.examen_java.dao.ReservationLocataireDAOImpl" %>
<%@ page import="com.examen_java.dao.ReservationLocataireDAO" %>
<%@ page import="com.examen_java.dao.FavoriDAOImpl" %>
<%@ page import="com.examen_java.dao.FavoriDAO" %>
<%@ page import="com.examen_java.dao.MessageDAOImpl" %>
<%@ page import="com.examen_java.dao.MessageDAO" %>
<%@ page import="java.util.List" %>
<%
    // Données de session
    UtilisateurInscription utilisateur = (UtilisateurInscription) session.getAttribute("utilisateur");
    String role = (String) session.getAttribute("role");
    String prenom = (utilisateur != null) ? utilisateur.getPrenom() : "Utilisateur";
    int idLocataire = (utilisateur != null) ? utilisateur.getIdUtilisateur() : 0;

    // Récupération des vraies données
    ReservationLocataireDAO reservationDAO = new ReservationLocataireDAOImpl();
    FavoriDAO favoriDAO = new FavoriDAOImpl();
    MessageDAO messageDAO = new MessageDAOImpl();

    int reservationsAVenir = 0;
    int sejoursPasses = 0;
    int favoris = favoriDAO.compter(idLocataire);
    int messagesNonLus = messageDAO.compterNonLusParLocataire(idLocataire);
    
    // Compter les réservations à venir et passées
    if (idLocataire > 0) {
        reservationsAVenir = reservationDAO.trouverParLocataireEtStatut(idLocataire, "EN_ATTENTE_PAIEMENT").size()
                + reservationDAO.trouverParLocataireEtStatut(idLocataire, "CONFIRMEE").size();
        sejoursPasses = reservationDAO.trouverParLocataireEtStatut(idLocataire, "TERMINEE").size();
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Locataire - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="dashboard-main">
        <div class="dashboard-container">

            
            <section class="section-bienvenue">
                <div class="carte-bienvenue">
                    <div class="bienvenue-texte">
                        <span class="bienvenue-salutation">Bonjour, <%= prenom %></span>
                        <h1>Prêt à trouver votre prochaine maison ?</h1>
                        <p>Explorez les annonces, gérez vos réservations et trouvez la maison idéale pour votre séjour.</p>
                    </div>
                    <div class="bienvenue-illustration">
                        <i class="fa-solid fa-magnifying-glass-location"></i>
                    </div>
                </div>
            </section>

            
            <section class="section-statistiques">
                <div class="grille-statistiques">
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-bleu"><i class="fa-solid fa-calendar-check"></i></div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= reservationsAVenir %></span>
                            <span class="statistique-label">Réservations à venir</span>
                        </div>
                    </div>
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-vert"><i class="fa-solid fa-clock-rotate-left"></i></div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= sejoursPasses %></span>
                            <span class="statistique-label">Séjours terminés</span>
                        </div>
                    </div>
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-rouge"><i class="fa-solid fa-heart"></i></div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= favoris %></span>
                            <span class="statistique-label">Annonces favorites</span>
                        </div>
                    </div>
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-orange"><i class="fa-solid fa-envelope"></i></div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= messagesNonLus %></span>
                            <span class="statistique-label">Messages non lus</span>
                        </div>
                    </div>
                </div>
            </section>

            
            <div class="dashboard-colonnes">
                <div class="colonne-gauche">

                    
                    <section class="section-reservations">
                        <div class="section-entete">
                            <h2><i class="fa-solid fa-calendar-check"></i> Mes réservations à venir</h2>
                            <a href="${pageContext.request.contextPath}/mes-reservations" class="lien-tout-voir">Voir tout <i class="fa-solid fa-arrow-right"></i></a>
                        </div>
                        <% if (reservationsAVenir > 0) { %>
                            <div class="etat-vide etat-vide-petit">
                                <p>Vous avez <strong><%= reservationsAVenir %></strong> réservation(s) à venir. <a href="${pageContext.request.contextPath}/mes-reservations">Voir mes réservations</a>.</p>
                            </div>
                        <% } else { %>
                            <div class="etat-vide">
                                <div class="vide-icone"><i class="fa-solid fa-calendar-xmark"></i></div>
                                <h3>Aucune réservation à venir</h3>
                                <p>Commencez par explorer les annonces disponibles.</p>
                                <a href="${pageContext.request.contextPath}/recherche" class="bouton-primaire">
                                    <i class="fa-solid fa-magnifying-glass"></i> Explorer les maisons
                                </a>
                            </div>
                        <% } %>
                    </section>

                    
                    <section class="section-reservations">
                        <div class="section-entete">
                            <h2><i class="fa-solid fa-clock-rotate-left"></i> Séjours passés</h2>
                            <a href="${pageContext.request.contextPath}/mes-reservations?statut=TERMINEE" class="lien-tout-voir">Voir tout <i class="fa-solid fa-arrow-right"></i></a>
                        </div>
                        <% if (sejoursPasses > 0) { %>
                            <div class="etat-vide etat-vide-petit">
                                <p>Vous avez <strong><%= sejoursPasses %></strong> séjour(s) terminé(s).</p>
                            </div>
                        <% } else { %>
                            <div class="etat-vide etat-vide-petit">
                                <p>Aucun séjour passé pour le moment.</p>
                            </div>
                        <% } %>
                    </section>

                </div>

        
                <aside class="colonne-droite">
                    <div class="carte-sidebar">
                        <h3><i class="fa-solid fa-magnifying-glass"></i> Recherche rapide</h3>
                        <form action="${pageContext.request.contextPath}/recherche" method="get" class="formulaire-recherche-rapide">
                            <div class="champ-sidebar">
                                <label>Destination</label>
                                <div class="champ-icone-sidebar">
                                    <i class="fa-solid fa-location-dot"></i>
                                    <input type="text" name="ville" placeholder="Ville, région...">
                                </div>
                            </div>
                            <div class="champ-sidebar">
                                <label>Voyageurs</label>
                                <div class="champ-icone-sidebar">
                                    <i class="fa-solid fa-users"></i>
                                    <input type="number" name="capacite" min="1" placeholder="Nombre de personnes">
                                </div>
                            </div>
                            <button type="submit" class="bouton-primaire bouton-plein">
                                <i class="fa-solid fa-magnifying-glass"></i> Rechercher
                            </button>
                        </form>
                    </div>

                    <div class="carte-sidebar">
                        <div class="section-entete">
                            <h3><i class="fa-solid fa-heart"></i> Mes favoris</h3>
                            <% if (favoris > 0) { %>
                                <a href="${pageContext.request.contextPath}/favoris" class="lien-tout-voir-petit">Tout voir</a>
                            <% } %>
                        </div>
                        <% if (favoris > 0) { %>
                            <p style="font-size:14px;color:#64748b;">Vous avez <strong><%= favoris %></strong> maison(s) en favoris.</p>
                        <% } else { %>
                            <div class="etat-vide etat-vide-petit">
                                <i class="fa-solid fa-heart-crack"></i>
                                <p>Aucun favori pour le moment.</p>
                            </div>
                        <% } %>
                    </div>

                    <div class="carte-sidebar">
                        <h3><i class="fa-solid fa-circle-info"></i> Informations</h3>
                        <div style="display:flex;flex-direction:column;gap:12px;font-size:13px;color:#64748b;">
                            <div style="display:flex;align-items:center;gap:10px;">
                                <i class="fa-solid fa-shield-halved" style="color:#3b82f6;font-size:16px;"></i>
                                <span>Paiement 100% sécurisé</span>
                            </div>
                            <div style="display:flex;align-items:center;gap:10px;">
                                <i class="fa-solid fa-headset" style="color:#10b981;font-size:16px;"></i>
                                <span>Assistance 24h/24 et 7j/7</span>
                            </div>
                            <div style="display:flex;align-items:center;gap:10px;">
                                <i class="fa-solid fa-rotate-left" style="color:#f59e0b;font-size:16px;"></i>
                                <span>Annulation flexible</span>
                            </div>
                        </div>
                    </div>
                </aside>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />
    <script src="${pageContext.request.contextPath}/assets/js/dashboard-script.js"></script>

</body>
</html>