<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.ReservationProprio" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    ReservationProprio reservation = (ReservationProprio) request.getAttribute("reservation");
    String reference = (String) request.getAttribute("reference");
    String datePaiement = (String) request.getAttribute("datePaiement");
    
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
    <title>Paiement confirmé - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/paiement-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="paiement-main">
        <div class="paiement-container">

            
            <div class="confirmation-carte-paiement">
                
                <!-- Icône succès -->
                <div class="confirmation-icone-paiement">
                    <i class="fa-solid fa-circle-check"></i>
                </div>
                
                <h1>Paiement confirmé !</h1>
                <p class="confirmation-message-paiement">
                    Votre réservation est maintenant confirmée. Voici votre reçu.
                </p>

           
                <div class="recu-paiement">
                    <div class="recu-entete">
                        <span class="recu-logo">Location Maisons</span>
                        <span class="recu-titre">Reçu de paiement</span>
                    </div>

                    <div class="recu-corps">
                        <div class="recu-ligne">
                            <span class="recu-label">Référence</span>
                            <span class="recu-valeur"><strong><%= reference %></strong></span>
                        </div>
                        <div class="recu-ligne">
                            <span class="recu-label">Date</span>
                            <span class="recu-valeur"><%= datePaiement %></span>
                        </div>
                        <div class="recu-separateur"></div>
                        <div class="recu-ligne">
                            <span class="recu-label">Maison</span>
                            <span class="recu-valeur"><%= reservation.getMaisonTitre() %></span>
                        </div>
                        <div class="recu-ligne">
                            <span class="recu-label">Ville</span>
                            <span class="recu-valeur"><%= reservation.getMaisonVille() %></span>
                        </div>
                        <div class="recu-ligne">
                            <span class="recu-label">Arrivée</span>
                            <span class="recu-valeur"><%= dateCourte.format(reservation.getDateDebut()) %></span>
                        </div>
                        <div class="recu-ligne">
                            <span class="recu-label">Départ</span>
                            <span class="recu-valeur"><%= dateCourte.format(reservation.getDateFin()) %></span>
                        </div>
                        <div class="recu-ligne">
                            <span class="recu-label">Durée</span>
                            <span class="recu-valeur"><%= reservation.getNombreNuits() %> nuit<%= reservation.getNombreNuits() > 1 ? "s" : "" %></span>
                        </div>
                        <div class="recu-ligne">
                            <span class="recu-label">Voyageurs</span>
                            <span class="recu-valeur"><%= reservation.getNombrePersonnes() %> personne<%= reservation.getNombrePersonnes() > 1 ? "s" : "" %></span>
                        </div>
                        <div class="recu-separateur"></div>
                        <div class="recu-total">
                            <span class="total-label">Montant payé</span>
                            <span class="total-valeur"><%= reservation.getMontantFormate() %></span>
                        </div>
                        <div class="recu-statut">
                            <i class="fa-solid fa-circle-check"></i> Paiement réussi
                        </div>
                    </div>
                </div>

                
                <div class="confirmation-actions-paiement">
                    <a href="${pageContext.request.contextPath}/mes-reservations" class="bouton-primaire">
                        <i class="fa-solid fa-calendar-check"></i> Mes réservations
                    </a>
                    <a href="${pageContext.request.contextPath}/dashboard" class="bouton-secondaire">
                        <i class="fa-solid fa-table-columns"></i> Dashboard
                    </a>
                    <button onclick="window.print()" class="bouton-imprimer">
                        <i class="fa-solid fa-print"></i> Imprimer le reçu
                    </button>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />
</body>
</html>