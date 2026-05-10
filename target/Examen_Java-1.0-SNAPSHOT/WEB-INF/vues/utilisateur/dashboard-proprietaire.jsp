<%@page import="com.examen_java.modele.Avis"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.UtilisateurInscription" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="com.examen_java.dao.MaisonDAOImpl" %>
<%@ page import="com.examen_java.dao.MaisonDAO" %>
<%@ page import="com.examen_java.dao.ReservationProprioDAOImpl" %>
<%@ page import="com.examen_java.dao.ReservationProprioDAO" %>
<%@ page import="com.examen_java.dao.RevenuDAOImpl" %>
<%@ page import="com.examen_java.dao.RevenuDAO" %>
<%@ page import="java.util.List" %>
<%
    // Données de session
    UtilisateurInscription utilisateur = (UtilisateurInscription) session.getAttribute("utilisateur");
    String role = (String) session.getAttribute("role");
    String prenom = (utilisateur != null) ? utilisateur.getPrenom() : "Propriétaire";
    int idProprietaire = (utilisateur != null) ? utilisateur.getIdUtilisateur() : 0;

    // Récupération des vraies données depuis la base
    MaisonDAO maisonDAO = new MaisonDAOImpl();
    ReservationProprioDAO reservationDAO = new ReservationProprioDAOImpl();
    RevenuDAO revenuDAO = new RevenuDAOImpl();

    int annoncesActives = maisonDAO.compterActives(idProprietaire);
    int reservationsEnAttente = reservationDAO.compterEnAttente(idProprietaire);
    double revenusMois = revenuDAO.calculerTotalMoisEnCours(idProprietaire);
    
    List<Maison> maisons = maisonDAO.trouverParProprietaire(idProprietaire);
    boolean aAnnonces = (maisons != null && !maisons.isEmpty());
    boolean aReservations = (reservationsEnAttente > 0);
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Propriétaire - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="dashboard-main">
        <div class="dashboard-container">

            
            <section class="section-bienvenue">
                <div class="carte-bienvenue carte-bienvenue-proprietaire">
                    <div class="bienvenue-texte">
                        <span class="bienvenue-salutation">Bonjour, <%= prenom %></span>
                        <h1>Gérez vos annonces et vos réservations</h1>
                        <p>Suivez vos revenus, gérez vos biens et communiquez avec vos locataires en toute simplicité.</p>
                    </div>
                    <div class="bienvenue-illustration">
                        <i class="fa-solid fa-house-laptop"></i>
                    </div>
                </div>
            </section>

            
            <section class="section-statistiques">
                <div class="grille-statistiques">
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-bleu">
                            <i class="fa-solid fa-house-circle-check"></i>
                        </div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= annoncesActives %></span>
                            <span class="statistique-label">Annonces actives</span>
                        </div>
                    </div>
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-orange">
                            <i class="fa-solid fa-clock"></i>
                        </div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= reservationsEnAttente %></span>
                            <span class="statistique-label">Réservations en attente</span>
                        </div>
                    </div>
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-vert">
                            <i class="fa-solid fa-euro-sign"></i>
                        </div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur"><%= String.format("%.0f", revenusMois) %> €</span>
                            <span class="statistique-label">Revenus ce mois</span>
                        </div>
                    </div>
                    <div class="carte-statistique">
                        <div class="statistique-icone bg-rouge">
                            <i class="fa-solid fa-chart-pie"></i>
                        </div>
                        <div class="statistique-infos">
                            <span class="statistique-valeur">--</span>
                            <span class="statistique-label">Taux d'occupation</span>
                        </div>
                    </div>
                </div>
            </section>

            
            <section class="section-actions-rapides">
                <div class="grille-actions">
                    <a href="${pageContext.request.contextPath}/maison/ajouter" class="carte-action">
                        <div class="action-icone bg-bleu"><i class="fa-solid fa-circle-plus"></i></div>
                        <span class="action-texte">Ajouter une maison</span>
                        <span class="action-description">Publier une nouvelle annonce</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/mes-maisons" class="carte-action">
                        <div class="action-icone bg-vert"><i class="fa-solid fa-list-check"></i></div>
                        <span class="action-texte">Voir mes annonces</span>
                        <span class="action-description">Gérer vos biens</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens" class="carte-action">
                        <div class="action-icone bg-orange"><i class="fa-solid fa-book-bookmark"></i></div>
                        <span class="action-texte">Réservations reçues</span>
                        <span class="action-description">Confirmer ou annuler</span>
                    </a>
                    <a href="${pageContext.request.contextPath}/revenus" class="carte-action">
                        <div class="action-icone bg-rouge"><i class="fa-solid fa-chart-line"></i></div>
                        <span class="action-texte">Voir mes revenus</span>
                        <span class="action-description">Suivre vos gains</span>
                    </a>
                </div>
            </section>

         
            <div class="dashboard-colonnes">
                <div class="colonne-gauche">

                  
                    <section class="section-reservations">
                        <div class="section-entete">
                            <h2><i class="fa-solid fa-house-laptop"></i> Mes annonces récentes</h2>
                            <a href="${pageContext.request.contextPath}/mes-maisons" class="lien-tout-voir">Voir tout <i class="fa-solid fa-arrow-right"></i></a>
                        </div>
                        <% if (aAnnonces) { %>
                            <div class="liste-reservations">
                                <% for (Maison m : maisons) { %>
                                    <div class="carte-reservation">
                                        <div class="reservation-image">
                                            <% if (m.getCheminPhotoPrincipale() != null) { %>
                                                <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= m.getCheminPhotoPrincipale() %>" alt="<%= m.getTitre() %>">
                                            <% } else { %>
                                                <div class="image-placeholder"><i class="fa-solid fa-house"></i></div>
                                            <% } %>
                                        </div>
                                        <div class="reservation-infos">
                                            <h3><%= m.getTitre() %></h3>
                                            <div class="reservation-details">
                                                <span class="detail-item"><i class="fa-solid fa-location-dot"></i> <%= m.getVille() %></span>
                                                <span class="detail-item"><i class="fa-solid fa-euro-sign"></i> <%= String.format("%.0f", m.getPrixBaseNuit()) %> €/nuit</span>
                                            </div>
                                            <span class="statut-badge <%= m.isEstVisible() ? "statut-CONFIRMEE" : "statut-ANNULEE" %>">
                                                <%= m.isEstVisible() ? "Visible" : "Masquée" %>
                                            </span>
                                        </div>
                                    </div>
                                <% } %>
                            </div>
                        <% } else { %>
                            <div class="etat-vide">
                                <div class="vide-icone"><i class="fa-solid fa-house-circle-exclamation"></i></div>
                                <h3>Aucune annonce publiée</h3>
                                <p>Vous n'avez pas encore publié d'annonce. Commencez dès maintenant à gagner des revenus.</p>
                                <a href="${pageContext.request.contextPath}/maison/ajouter" class="bouton-primaire">
                                    <i class="fa-solid fa-circle-plus"></i> Ajouter ma première maison
                                </a>
                            </div>
                        <% } %>
                    </section>

                   
                    <section class="section-reservations">
                        <div class="section-entete">
                            <h2><i class="fa-solid fa-book-bookmark"></i> Dernières réservations reçues</h2>
                            <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens" class="lien-tout-voir">Voir tout <i class="fa-solid fa-arrow-right"></i></a>
                        </div>
                        <% if (aReservations) { %>
                            <div class="etat-vide etat-vide-petit">
                                <p><strong><%= reservationsEnAttente %></strong> réservation(s) en attente de confirmation.</p>
                            </div>
                        <% } else { %>
                            <div class="etat-vide etat-vide-petit">
                                <i class="fa-solid fa-calendar-xmark"></i>
                                <p>Aucune réservation reçue pour le moment.</p>
                            </div>
                        <% } %>
                    </section>
                    
                   
                <section class="section-reservations">
                    <div class="section-entete">
                        <h2>
                            <i class="fa-solid fa-star"></i> 
                            Derniers avis reçus
                        </h2>
                    </div>

                    <%
                        List<Avis> derniersAvis = (List<Avis>) request.getAttribute("derniersAvis");
                        if (derniersAvis != null && !derniersAvis.isEmpty()) {
                            for (Avis a : derniersAvis) {
                    %>
                                <div class="carte-reservation" style="margin-bottom:12px;">
                                    <div class="reservation-infos">
                                        <div style="display:flex;align-items:center;gap:8px;margin-bottom:4px;">
                                            <strong><%= a.getAuteurNomComplet() %></strong>
                                            <span style="color:#f59e0b;">
                                                <% for (int i = 1; i <= 5; i++) { %>
                                                    <%= i <= Math.round(a.getNoteMoyenne()) ? "★" : "☆" %>
                                                <% } %>
                                                <%= String.format("%.1f", a.getNoteMoyenne()) %>
                                            </span>
                                        </div>
                                        <span style="font-size:13px;color:#64748b;">
                                            Sur <strong><%= a.getMaisonTitre() %></strong> - <%= a.getDateFormatee() %>
                                        </span>
                                        <% if (a.getCommentaire() != null && !a.getCommentaire().isEmpty()) { %>
                                            <p style="font-size:13px;color:#475569;margin:6px 0 0 0;"><%= a.getCommentaire() %></p>
                                        <% } %>
                                    </div>
                                </div>
                    <%
                            }
                        } else {
                    %>
                            <div class="etat-vide etat-vide-petit">
                                <i class="fa-solid fa-star"></i>
                                <p>Aucun avis reçu pour le moment.</p>
                            </div>
                    <% } %>
                </section>

                </div>

               
                <aside class="colonne-droite">
                    <div class="carte-sidebar">
                        <h3><i class="fa-solid fa-clock"></i> Réservations en attente</h3>
                        <% if (reservationsEnAttente > 0) { %>
                            <p style="font-size:14px;color:#64748b;"><strong><%= reservationsEnAttente %></strong> réservation(s) à confirmer.</p>
                        <% } else { %>
                            <div class="etat-vide etat-vide-petit">
                                <i class="fa-solid fa-circle-check" style="color:#10b981;"></i>
                                <p>Toutes les réservations sont traitées.</p>
                            </div>
                        <% } %>
                    </div>

                    <div class="carte-sidebar">
                        <div class="section-entete">
                            <h3><i class="fa-solid fa-envelope"></i> Messages récents</h3>
                            <a href="${pageContext.request.contextPath}/messages" class="lien-tout-voir-petit">Voir tout</a>
                        </div>
                        <div class="etat-vide etat-vide-petit">
                            <i class="fa-solid fa-envelope-open"></i>
                            <p>Aucun nouveau message.</p>
                        </div>
                    </div>

                    <div class="carte-sidebar">
                        <h3><i class="fa-solid fa-circle-info"></i> Informations</h3>
                        <div style="display:flex;flex-direction:column;gap:12px;font-size:13px;color:#64748b;">
                            <div style="display:flex;align-items:center;gap:10px;">
                                <i class="fa-solid fa-shield-halved" style="color:#3b82f6;font-size:16px;"></i>
                                <span>Annonces vérifiées après 24h</span>
                            </div>
                            <div style="display:flex;align-items:center;gap:10px;">
                                <i class="fa-solid fa-hand-holding-dollar" style="color:#10b981;font-size:16px;"></i>
                                <span>Commission : 5% par réservation</span>
                            </div>
                            <div style="display:flex;align-items:center;gap:10px;">
                                <i class="fa-solid fa-headset" style="color:#f59e0b;font-size:16px;"></i>
                                <span>Support propriétaire prioritaire</span>
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