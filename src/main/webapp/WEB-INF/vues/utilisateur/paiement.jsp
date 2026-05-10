<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.ReservationProprio" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    ReservationProprio reservation = (ReservationProprio) request.getAttribute("reservation");
    Integer idReservation = (Integer) request.getAttribute("idReservation");
    String messageErreur = (String) request.getAttribute("erreur");
    
    SimpleDateFormat dateCourte = new SimpleDateFormat("dd/MM/yyyy");
    
    if (reservation == null) {
        response.sendRedirect(request.getContextPath() + "/mes-reservations");
        return;
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Paiement - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/paiement-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="paiement-main">
        <div class="paiement-container">

            
            <div class="fil-ariane">
                <a href="${pageContext.request.contextPath}/mes-reservations">
                    <i class="fa-solid fa-arrow-left"></i> Retour à mes réservations
                </a>
            </div>

            
            <div class="paiement-entete">
                <h1><i class="fa-solid fa-credit-card"></i> Paiement</h1>
                <p>Finalisez votre réservation en effectuant le paiement.</p>
            </div>

           
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

            
            <div class="paiement-contenu">

                
                <div class="paiement-recap">
                    <div class="recap-carte">
                        <h3><i class="fa-solid fa-receipt"></i> Récapitulatif de la réservation</h3>
                        
                        <div class="recap-ligne">
                            <span class="recap-label">Maison</span>
                            <span class="recap-valeur"><%= reservation.getMaisonTitre() %></span>
                        </div>
                        <div class="recap-ligne">
                            <span class="recap-label">Ville</span>
                            <span class="recap-valeur"><%= reservation.getMaisonVille() %></span>
                        </div>
                        <div class="recap-ligne">
                            <span class="recap-label">Arrivée</span>
                            <span class="recap-valeur"><%= dateCourte.format(reservation.getDateDebut()) %></span>
                        </div>
                        <div class="recap-ligne">
                            <span class="recap-label">Départ</span>
                            <span class="recap-valeur"><%= dateCourte.format(reservation.getDateFin()) %></span>
                        </div>
                        <div class="recap-ligne">
                            <span class="recap-label">Durée</span>
                            <span class="recap-valeur"><%= reservation.getNombreNuits() %> nuit<%= reservation.getNombreNuits() > 1 ? "s" : "" %></span>
                        </div>
                        <div class="recap-ligne">
                            <span class="recap-label">Voyageurs</span>
                            <span class="recap-valeur"><%= reservation.getNombrePersonnes() %> personne<%= reservation.getNombrePersonnes() > 1 ? "s" : "" %></span>
                        </div>
                        <div class="recap-total">
                            <span class="total-label">Montant total</span>
                            <span class="total-valeur"><%= reservation.getMontantFormate() %></span>
                        </div>
                    </div>
                </div>

              
                <div class="paiement-formulaire">
                    <div class="formulaire-carte">
                        <h3><i class="fa-solid fa-lock"></i> Choisissez votre méthode de paiement</h3>

                        <form action="${pageContext.request.contextPath}/paiement" method="post" class="formulaire-paiement">
                            <input type="hidden" name="id_reservation" value="<%= idReservation %>">
                            <input type="hidden" name="montant" value="<%= String.format("%.0f", reservation.getMontantTotal()) %>">

                            <!-- Méthodes de paiement -->
                            <div class="methodes-paiement">
                                <label class="methode-item active" id="methodeCarte">
                                    <input type="radio" name="methode_paiement" value="CARTE_BANCAIRE" checked>
                                    <span class="methode-icone"><i class="fa-regular fa-credit-card"></i></span>
                                    <span class="methode-nom">Carte bancaire</span>
                                    <span class="methode-check"><i class="fa-solid fa-circle-check"></i></span>
                                </label>

                                <label class="methode-item" id="methodeVirement">
                                    <input type="radio" name="methode_paiement" value="VIREMENT">
                                    <span class="methode-icone"><i class="fa-solid fa-building-columns"></i></span>
                                    <span class="methode-nom">Virement bancaire</span>
                                    <span class="methode-check"><i class="fa-solid fa-circle-check"></i></span>
                                </label>
                            </div>

                            <!-- Détails carte (simulé) -->
                            <div class="details-carte" id="detailsCarte">
                                <div class="champ-paiement">
                                    <label>Numéro de carte</label>
                                    <div class="champ-icone-paiement">
                                        <i class="fa-regular fa-credit-card"></i>
                                        <input type="text" placeholder="1234 5678 9012 3456" maxlength="19">
                                    </div>
                                </div>
                                <div class="champ-ligne-paiement">
                                    <div class="champ-paiement">
                                        <label>Expiration</label>
                                        <input type="text" placeholder="MM/AA" maxlength="5">
                                    </div>
                                    <div class="champ-paiement">
                                        <label>CVV</label>
                                        <div class="champ-icone-paiement">
                                            <i class="fa-solid fa-lock"></i>
                                            <input type="text" placeholder="123" maxlength="3">
                                        </div>
                                    </div>
                                </div>
                                <div class="champ-paiement">
                                    <label>Titulaire de la carte</label>
                                    <input type="text" placeholder="Nom Prénom">
                                </div>
                            </div>

                            <!-- Sécurité -->
                            <div class="paiement-securite">
                                <div class="securite-item">
                                    <i class="fa-solid fa-shield-halved"></i>
                                    <span>Paiement 100% sécurisé</span>
                                </div>
                                <div class="securite-item">
                                    <i class="fa-solid fa-lock"></i>
                                    <span>Données cryptées SSL</span>
                                </div>
                            </div>

                            <!-- Bouton -->
                            <button type="submit" class="bouton-payer">
                                <i class="fa-solid fa-lock"></i> Payer <%= reservation.getMontantFormate() %>
                            </button>
                        </form>

                        <p class="info-simulation">
                            <i class="fa-solid fa-circle-info"></i> 
                            Ceci est une simulation de paiement. Aucun montant réel ne sera débité.
                        </p>
                    </div>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/paiement-script.js"></script>

</body>
</html>