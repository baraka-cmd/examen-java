<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.ReservationProprio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Récupération des données
    List<ReservationProprio> reservations = (List<ReservationProprio>) request.getAttribute("reservations");
    int totalReservations = (Integer) request.getAttribute("totalReservations");
    int enAttente = (Integer) request.getAttribute("enAttente");
    int confirmees = (Integer) request.getAttribute("confirmees");
    int annulees = (Integer) request.getAttribute("annulees");
    int terminees = (Integer) request.getAttribute("terminees");
    String filtreActif = (String) request.getAttribute("filtreActif");
    String messageErreur = (String) request.getAttribute("erreur");
    String messageSucces = (String) request.getAttribute("succes");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("fr", "FR"));
    SimpleDateFormat dateFormatCourt = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservations reçues - Location Maisons</title>

    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

   
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <!-- Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/maison-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservations-proprio.css">
</head>
<body>

   
    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

   
    <main class="maison-main">
        <div class="maison-container">

           
            <div class="reservations-entete">
                <div class="entete-gauche">
                    <a href="${pageContext.request.contextPath}/mes-maisons" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour à mes annonces
                    </a>
                    <h1>
                        <i class="fa-solid fa-book-bookmark"></i> 
                        Réservations reçues
                    </h1>
                    <p>
                        <% if (totalReservations > 0) { %>
                            <strong><%= totalReservations %></strong> réservation<%= totalReservations > 1 ? "s" : "" %> reçue<%= totalReservations > 1 ? "s" : "" %>
                        <% } else { %>
                            Aucune réservation reçue pour le moment.
                        <% } %>
                    </p>
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
                <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens" 
                   class="stat-item <%= filtreActif == null ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= totalReservations %></span>
                    <span class="stat-label">Toutes</span>
                </a>
                <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?statut=EN_ATTENTE_PAIEMENT" 
                   class="stat-item stat-attente <%= "EN_ATTENTE_PAIEMENT".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= enAttente %></span>
                    <span class="stat-label">En attente</span>
                </a>
                <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?statut=CONFIRMEE" 
                   class="stat-item stat-confirmee <%= "CONFIRMEE".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= confirmees %></span>
                    <span class="stat-label">Confirmées</span>
                </a>
                <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?statut=TERMINEE" 
                   class="stat-item stat-terminee <%= "TERMINEE".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= terminees %></span>
                    <span class="stat-label">Terminées</span>
                </a>
                <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?statut=ANNULEE_PAR_LOCATAIRE" 
                   class="stat-item stat-annulee <%= "ANNULEE_PAR_LOCATAIRE".equals(filtreActif) ? "stat-actif" : "" %>">
                    <span class="stat-valeur"><%= annulees %></span>
                    <span class="stat-label">Annulées</span>
                </a>
            </div>

            
            <% if (reservations != null && !reservations.isEmpty()) { %>
                <div class="reservations-liste">

                    <% for (ReservationProprio r : reservations) { %>
                        <div class="carte-reservation-proprio reservation-statut-<%= r.getStatut() != null ? r.getStatut().toUpperCase() : "INCONNU" %>">

                            
                            <div class="reservation-statut-barre">
                                <span class="statut-badge-proprio statut-<%= r.getStatut() != null ? r.getStatut().toUpperCase() : "INCONNU" %>">
                                    <i class="fa-solid <%= r.estEnAttente() ? "fa-clock" : r.estConfirmee() ? "fa-circle-check" : r.estAnnulee() ? "fa-circle-xmark" : r.estTerminee() ? "fa-flag-checkered" : "fa-circle" %>"></i>
                                    <%= r.getStatutLibelle() %>
                                </span>
                                <span class="reservation-id">#<%= r.getIdReservation() %></span>
                            </div>

                            <div class="reservation-corps">
                                
                                <div class="reservation-colonne">
                                    <div class="colonne-titre">
                                        <i class="fa-solid fa-user"></i> Locataire
                                    </div>
                                    <div class="colonne-contenu">
                                        <strong><%= r.getLocataireNomComplet() %></strong>
                                        <span class="info-secondaire">
                                            <i class="fa-solid fa-envelope"></i> <%= r.getLocataireEmail() %>
                                        </span>
                                        <% if (r.getLocataireTelephone() != null && !r.getLocataireTelephone().isEmpty()) { %>
                                            <span class="info-secondaire">
                                                <i class="fa-solid fa-phone"></i> <%= r.getLocataireTelephone() %>
                                            </span>
                                        <% } %>
                                    </div>
                                </div>

                                
                                <div class="reservation-colonne">
                                    <div class="colonne-titre">
                                        <i class="fa-solid fa-house"></i> Maison
                                    </div>
                                    <div class="colonne-contenu">
                                        <strong><%= r.getMaisonTitre() %></strong>
                                        <span class="info-secondaire">
                                            <i class="fa-solid fa-location-dot"></i> <%= r.getMaisonVille() %>
                                        </span>
                                    </div>
                                </div>

                                
                                <div class="reservation-colonne">
                                    <div class="colonne-titre">
                                        <i class="fa-solid fa-calendar"></i> Séjour
                                    </div>
                                    <div class="colonne-contenu">
                                        <span class="info-dates">
                                            <i class="fa-solid fa-calendar-day"></i> <%= dateFormatCourt.format(r.getDateDebut()) %>
                                            <i class="fa-solid fa-arrow-right"></i>
                                            <%= dateFormatCourt.format(r.getDateFin()) %>
                                        </span>
                                        <span class="info-secondaire">
                                            <i class="fa-solid fa-moon"></i> <%= r.getNombreNuits() %> nuit<%= r.getNombreNuits() > 1 ? "s" : "" %>
                                            &nbsp;|&nbsp;
                                            <i class="fa-solid fa-users"></i> <%= r.getNombrePersonnes() %> pers.
                                        </span>
                                    </div>
                                </div>

                             
                                <div class="reservation-colonne reservation-montant">
                                    <div class="colonne-titre">
                                        <i class="fa-solid fa-euro-sign"></i> Montant
                                    </div>
                                    <div class="colonne-contenu">
                                        <span class="montant-valeur"><%= r.getMontantFormate() %></span>
                                    </div>
                                </div>

                                
                                <div class="reservation-colonne reservation-actions">
                                    <% if (r.peutEtreConfirmee()) { %>
                                        <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?action=confirmer&id=<%= r.getIdReservation() %>" 
                                           class="bouton-action-proprio bouton-confirmer"
                                           onclick="return confirm('Confirmer cette réservation ?');">
                                            <i class="fa-solid fa-circle-check"></i> Confirmer
                                        </a>
                                    <% } %>
                                    <% if (r.peutEtreAnnulee()) { %>
                                        <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens?action=annuler&id=<%= r.getIdReservation() %>" 
                                           class="bouton-action-proprio bouton-annuler"
                                           onclick="return confirm('Voulez-vous vraiment annuler cette réservation ?');">
                                            <i class="fa-solid fa-circle-xmark"></i> Annuler
                                        </a>
                                    <% } %>
                                    <% if (r.getMessageLocataire() != null && !r.getMessageLocataire().isEmpty()) { %>
                                        <button type="button" 
                                                class="bouton-action-proprio bouton-message"
                                                title="<%= r.getMessageLocataire() %>"
                                                onclick="alert('Message du locataire :\n\n<%= r.getMessageLocataire().replace("'", "\\'") %>');">
                                            <i class="fa-solid fa-message"></i> Message
                                        </button>
                                    <% } %>
                                </div>
                            </div>
                        </div>
                    <% } %>

                </div>
            <% } else { %>

                
                <div class="etat-vide-reservations">
                    <div class="vide-illustration">
                        <i class="fa-solid fa-book-bookmark"></i>
                    </div>
                    <h2>
                        <% if (filtreActif != null) { %>
                            Aucune réservation "<%= filtreActif.replace("_", " ").toLowerCase() %>"
                        <% } else { %>
                            Aucune réservation reçue
                        <% } %>
                    </h2>
                    <p>
                        <% if (filtreActif != null) { %>
                            Aucune réservation ne correspond à ce filtre. Essayez un autre statut.
                        <% } else { %>
                            Vous n'avez pas encore reçu de réservation. Lorsqu'un locataire réservera l'une de vos maisons, elle apparaîtra ici.
                        <% } %>
                    </p>
                    <% if (filtreActif != null) { %>
                        <a href="${pageContext.request.contextPath}/reservations-sur-mes-biens" class="bouton-ajouter">
                            <i class="fa-solid fa-list"></i> Voir toutes les réservations
                        </a>
                    <% } else { %>
                        <a href="${pageContext.request.contextPath}/mes-maisons" class="bouton-ajouter">
                            <i class="fa-solid fa-house-laptop"></i> Voir mes annonces
                        </a>
                    <% } %>
                </div>

            <% } %>

        </div>
    </main>

    
    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

   
    <script src="${pageContext.request.contextPath}/assets/js/reservations-proprio.js"></script>

</body>
</html>