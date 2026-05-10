<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.ReservationProprio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<ReservationProprio> reservations = (List<ReservationProprio>) request.getAttribute("reservations");
    int totalReservations = (Integer) request.getAttribute("totalReservations");
    int enAttente = (Integer) request.getAttribute("enAttente");
    int confirmees = (Integer) request.getAttribute("confirmees");
    int terminees = (Integer) request.getAttribute("terminees");
    int annulees = (Integer) request.getAttribute("annulees");
    String filtreActif = (String) request.getAttribute("filtreActif");
    String messageErreur = (String) request.getAttribute("erreur");
    String messageSucces = (String) request.getAttribute("succes");

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("fr", "FR"));
    SimpleDateFormat dateCourte = new SimpleDateFormat("dd/MM/yyyy");
%>

<%
    String paiementSucces = request.getParameter("paiement");
    if ("succes".equals(paiementSucces)) {
        request.setAttribute("succes", "Votre paiement a été effectué avec succès ! La réservation est maintenant confirmée.");
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes réservations - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mes-reservations-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="reservations-main">
        <div class="reservations-container">

            
            <div class="reservations-entete">
                <div class="entete-gauche">
                    <a href="${pageContext.request.contextPath}/dashboard" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour au dashboard
                    </a>
                    <h1>
                        <i class="fa-solid fa-calendar-check"></i> 
                        Mes réservations
                    </h1>
                    <p>
                        <% if (totalReservations > 0) { %>
                            <strong><%= totalReservations %></strong> réservation<%= totalReservations > 1 ? "s" : "" %>
                        <% } else { %>
                            Vous n'avez pas encore de réservation.
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
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

            
            <div class="reservations-stats">
                <a href="${pageContext.request.contextPath}/mes-reservations" 
                   class="stat-item <%= filtreActif == null ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= totalReservations %></span>
                    <span class="stat-label">Toutes</span>
                </a>
                <a href="${pageContext.request.contextPath}/mes-reservations?statut=EN_ATTENTE_PAIEMENT" 
                   class="stat-item stat-attente <%= "EN_ATTENTE_PAIEMENT".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= enAttente %></span>
                    <span class="stat-label">En attente</span>
                </a>
                <a href="${pageContext.request.contextPath}/mes-reservations?statut=CONFIRMEE" 
                   class="stat-item stat-confirmee <%= "CONFIRMEE".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= confirmees %></span>
                    <span class="stat-label">Confirmées</span>
                </a>
                <a href="${pageContext.request.contextPath}/mes-reservations?statut=TERMINEE" 
                   class="stat-item stat-terminee <%= "TERMINEE".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= terminees %></span>
                    <span class="stat-label">Terminées</span>
                </a>
                <a href="${pageContext.request.contextPath}/mes-reservations?statut=ANNULEE" 
                   class="stat-item stat-annulee <%= "ANNULEE".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= annulees %></span>
                    <span class="stat-label">Annulées</span>
                </a>
            </div>

            
            <% if (reservations != null && !reservations.isEmpty()) { %>
                <div class="reservations-liste">

                    <% for (ReservationProprio r : reservations) { %>
                        <div class="carte-reservation-locataire reservation-statut-<%= r.getStatut() != null ? r.getStatut().toUpperCase() : "INCONNU" %>">

                           
                            <div class="reservation-barre">
                                <span class="statut-badge-locataire statut-<%= r.getStatut() != null ? r.getStatut().toUpperCase() : "INCONNU" %>">
                                    <i class="fa-solid <%= r.estEnAttente() ? "fa-clock" : r.estConfirmee() ? "fa-circle-check" : r.estAnnulee() ? "fa-circle-xmark" : r.estTerminee() ? "fa-flag-checkered" : "fa-circle" %>"></i>
                                    <%= r.getStatutLibelle() %>
                                </span>
                                <span class="reservation-ref">#<%= r.getIdReservation() %></span>
                            </div>

                            
                            <div class="reservation-corps-locataire">
                                
                                <div class="reservation-photo">
                                    <% if (r.getPhotoPrincipale() != null && r.getPhotoPrincipale().getCheminFichier() != null) { %>
                                        <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= r.getPhotoPrincipale().getCheminFichier() %>" 
                                             alt="<%= r.getMaisonTitre() %>">
                                    <% } else { %>
                                        <div class="image-placeholder"><i class="fa-solid fa-house"></i></div>
                                    <% } %>
                                </div>

                                
                                <div class="reservation-infos-locataire">
                                    <h3><%= r.getMaisonTitre() %></h3>
                                    <div class="info-ville">
                                        <i class="fa-solid fa-location-dot"></i> <%= r.getMaisonVille() %>
                                    </div>

                                    <div class="info-dates">
                                        <div class="date-bloc">
                                            <span class="date-label">Arrivée</span>
                                            <span class="date-valeur"><%= dateCourte.format(r.getDateDebut()) %></span>
                                        </div>
                                        <div class="date-separateur">
                                            <i class="fa-solid fa-arrow-right"></i>
                                            <span><%= r.getNombreNuits() %> nuit<%= r.getNombreNuits() > 1 ? "s" : "" %></span>
                                        </div>
                                        <div class="date-bloc">
                                            <span class="date-label">Départ</span>
                                            <span class="date-valeur"><%= dateCourte.format(r.getDateFin()) %></span>
                                        </div>
                                    </div>

                                    <div class="info-details">
                                        <span><i class="fa-solid fa-users"></i> <%= r.getNombrePersonnes() %> pers.</span>
                                        <span class="info-montant"><i class="fa-solid fa-euro-sign"></i> <%= r.getMontantFormate() %></span>
                                    </div>
                                </div>

                            
                                <div class="reservation-actions-locataire">
                                    <% if (r.estEnAttente() || r.estConfirmee()) { %>
                                        <a href="${pageContext.request.contextPath}/mes-reservations?action=annuler&id=<%= r.getIdReservation() %>" 
                                           class="bouton-annuler-locataire"
                                           onclick="return confirm('Voulez-vous vraiment annuler cette réservation ?');">
                                            <i class="fa-solid fa-circle-xmark"></i> Annuler
                                        </a>
                                    <% } %>
                                    <% if (r.estTerminee()) { %>
                                        <a href="${pageContext.request.contextPath}/avis/laisser?id_reservation=<%= r.getIdReservation() %>" 
                                            class="bouton-avis">
                                             <i class="fa-solid fa-star"></i> Donner mon avis
                                         </a>
                                    <% } %>
                                    <a href="${pageContext.request.contextPath}/maison/detail?id=<%= r.getIdMaison() %>" 
                                       class="bouton-voir-maison">
                                        <i class="fa-solid fa-eye"></i> Voir l'annonce
                                    </a>
                                    <a href="${pageContext.request.contextPath}/messages/conversation?id_reservation=<%= r.getIdReservation() %>" 
                                        class="bouton-contacter">
                                        <i class="fa-solid fa-envelope"></i> Contacter
                                    </a>
                                    <% if (r.estEnAttente()) { %>
                                        <a href="${pageContext.request.contextPath}/paiement?id_reservation=<%= r.getIdReservation() %>" 
                                           class="bouton-payer-locataire">
                                            <i class="fa-solid fa-credit-card"></i> Payer
                                        </a>
                                    <% } %>
                                    <% if (r.estConfirmee() || r.estTerminee()) { %>
                                        <a href="${pageContext.request.contextPath}/paiement/confirmation?id_reservation=<%= r.getIdReservation() %>" 
                                           class="bouton-voir-recue">
                                            <i class="fa-solid fa-receipt"></i> Voir le reçu
                                        </a>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    <% } %>

                </div>
            <% } else { %>
                
                <div class="etat-vide-reservations">
                    <div class="vide-illustration">
                        <i class="fa-solid fa-calendar-xmark"></i>
                    </div>
                    <h2>
                        <% if (filtreActif != null) { %>
                            Aucune réservation "<%= filtreActif.replace("_", " ").toLowerCase() %>"
                        <% } else { %>
                            Aucune réservation
                        <% } %>
                    </h2>
                    <p>
                        <% if (filtreActif != null) { %>
                            Essayez un autre filtre pour voir vos réservations.
                        <% } else { %>
                            Vous n'avez pas encore effectué de réservation. Explorez les annonces et trouvez la maison idéale !
                        <% } %>
                    </p>
                    <a href="${pageContext.request.contextPath}/recherche" class="bouton-primaire">
                        <i class="fa-solid fa-magnifying-glass"></i> Explorer des maisons
                    </a>
                </div>
            <% } %>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />
    <script src="${pageContext.request.contextPath}/assets/js/mes-reservations-script.js"></script>

</body>
</html>