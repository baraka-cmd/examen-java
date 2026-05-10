<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.ReservationProprio" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<ReservationProprio> paiements = (List<ReservationProprio>) request.getAttribute("paiements");
    Double totalPaye = (Double) request.getAttribute("totalPaye");
    Integer nombrePaiements = (Integer) request.getAttribute("nombrePaiements");
    
    SimpleDateFormat dateCourte = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat dateHeure = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mes paiements - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/paiement-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="paiement-main">
        <div class="paiement-container">

            
            <div class="paiements-entete">
                <div class="entete-gauche">
                    <a href="${pageContext.request.contextPath}/dashboard" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour au dashboard
                    </a>
                    <h1>
                        <i class="fa-solid fa-receipt"></i> 
                        Mes paiements
                    </h1>
                    <p>
                        <% if (nombrePaiements != null && nombrePaiements > 0) { %>
                            <strong><%= nombrePaiements %></strong> paiement<%= nombrePaiements > 1 ? "s" : "" %> effectué<%= nombrePaiements > 1 ? "s" : "" %>
                        <% } else { %>
                            Aucun paiement effectué pour le moment.
                        <% } %>
                    </p>
                </div>
                <% if (totalPaye != null && totalPaye > 0) { %>
                    <div class="total-paye-badge">
                        <span class="total-label">Total payé</span>
                        <span class="total-valeur"><%= String.format("%.0f", totalPaye) %> €</span>
                    </div>
                <% } %>
            </div>

            
            <% if (paiements != null && !paiements.isEmpty()) { %>
                <div class="paiements-liste">

                    <% for (ReservationProprio p : paiements) { %>
                        <div class="carte-paiement">
                            <div class="carte-paiement-gauche">
                                <div class="paiement-statut">
                                    <i class="fa-solid fa-circle-check"></i>
                                    <span>Payé</span>
                                </div>
                                <div class="paiement-reference">
                                    <%= p.getMontantFormate() %>
                                </div>
                            </div>
                            
                            <div class="carte-paiement-milieu">
                                <h3><%= p.getMaisonTitre() %></h3>
                                <div class="paiement-adresse">
                                    <i class="fa-solid fa-location-dot"></i> <%= p.getMaisonVille() %>
                                </div>
                                <div class="paiement-dates">
                                    <span class="date-item">
                                        <i class="fa-solid fa-calendar-check"></i>
                                        <%= dateCourte.format(p.getDateDebut()) %> → <%= dateCourte.format(p.getDateFin()) %>
                                    </span>
                                    <span class="duree-item">
                                        <i class="fa-solid fa-moon"></i> <%= p.getNombreNuits() %> nuit<%= p.getNombreNuits() > 1 ? "s" : "" %>
                                    </span>
                                    <span class="personnes-item">
                                        <i class="fa-solid fa-users"></i> <%= p.getNombrePersonnes() %> pers.
                                    </span>
                                </div>
                            </div>

                            <div class="carte-paiement-droite">
                                <a href="${pageContext.request.contextPath}/paiement/confirmation?id_reservation=<%= p.getIdReservation() %>" 
                                   class="bouton-voir-recue">
                                    <i class="fa-solid fa-receipt"></i> Voir le reçu
                                </a>
                                <a href="${pageContext.request.contextPath}/maison/detail?id=<%= p.getIdMaison() %>" 
                                   class="bouton-voir-maison-paiement">
                                    <i class="fa-solid fa-eye"></i> Voir l'annonce
                                </a>
                            </div>
                        </div>
                    <% } %>

                </div>
            <% } else { %>
                
                <div class="etat-vide-paiements">
                    <div class="vide-illustration">
                        <i class="fa-solid fa-receipt"></i>
                    </div>
                    <h2>Aucun paiement</h2>
                    <p>Vous n'avez pas encore effectué de paiement. Lorsque vous réserverez une maison, vos paiements apparaîtront ici.</p>
                    <a href="${pageContext.request.contextPath}/recherche" class="bouton-primaire">
                        <i class="fa-solid fa-magnifying-glass"></i> Explorer des maisons
                    </a>
                </div>
            <% } %>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/paiement-script.js"></script>

</body>
</html>