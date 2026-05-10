<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%
    Maison maison = (Maison) request.getAttribute("maison");
    String dateDebut = (String) request.getAttribute("dateDebut");
    String dateFin = (String) request.getAttribute("dateFin");
    Integer nombrePersonnes = (Integer) request.getAttribute("nombrePersonnes");
    Long nombreNuits = (Long) request.getAttribute("nombreNuits");
    Double prixTotal = (Double) request.getAttribute("prixTotal");
    Integer idMaison = (Integer) request.getAttribute("idMaison");
    String messageErreur = (String) request.getAttribute("erreur");
    
    String role = (String) session.getAttribute("role");
    
    if (maison == null) {
        response.sendRedirect(request.getContextPath() + "/recherche");
        return;
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmer la réservation - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="reservation-main">
        <div class="reservation-container">

           
            <div class="fil-ariane">
                <a href="${pageContext.request.contextPath}/maison/detail?id=<%= maison.getIdMaison() %>">
                    <i class="fa-solid fa-arrow-left"></i> Retour à l'annonce
                </a>
            </div>

            
 
            <div class="reservation-entete">
                <h1><i class="fa-solid fa-bookmark"></i> Confirmer votre réservation</h1>
                <p>Vérifiez les détails avant de confirmer.</p>
            </div>

            
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

           
            <div class="reservation-contenu">

                <!-- COLONNE GAUCHE : Récapitulatif -->
                <div class="reservation-recap">
                    
                    <!-- Maison -->
                    <div class="recap-section">
                        <div class="recap-maison">
                            <div class="recap-maison-image">
                                <% if (maison.getPhotoPrincipale() != null && maison.getPhotoPrincipale().getCheminFichier() != null) { %>
                                    <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= maison.getPhotoPrincipale().getCheminFichier() %>" 
                                         alt="<%= maison.getTitre() %>">
                                <% } else { %>
                                    <div class="image-placeholder"><i class="fa-solid fa-house"></i></div>
                                <% } %>
                            </div>
                            <div class="recap-maison-infos">
                                <h2><%= maison.getTitre() %></h2>
                                <div class="recap-adresse">
                                    <i class="fa-solid fa-location-dot"></i>
                                    <span><%= maison.getVille() %>, <%= maison.getPays() != null ? maison.getPays() : "" %></span>
                                </div>
                                <div class="recap-carac">
                                    <span><i class="fa-solid fa-users"></i> <%= maison.getCapacitePersonnes() %> pers. max</span>
                                    <span><i class="fa-solid fa-bed"></i> <%= maison.getNombreChambres() %> ch.</span>
                                </div>
                            </div>
                        </div>
                    </div>

                   
                    <div class="recap-section">
                        <h3><i class="fa-solid fa-calendar-check"></i> Détails du séjour</h3>
                        <div class="recap-details">
                            <div class="recap-ligne">
                                <span class="recap-label">Arrivée</span>
                                <span class="recap-valeur"><%= dateDebut %></span>
                            </div>
                            <div class="recap-ligne">
                                <span class="recap-label">Départ</span>
                                <span class="recap-valeur"><%= dateFin %></span>
                            </div>
                            <div class="recap-ligne">
                                <span class="recap-label">Durée</span>
                                <span class="recap-valeur"><%= nombreNuits %> nuit<%= nombreNuits > 1 ? "s" : "" %></span>
                            </div>
                            <div class="recap-ligne">
                                <span class="recap-label">Voyageurs</span>
                                <span class="recap-valeur"><%= nombrePersonnes %> personne<%= nombrePersonnes > 1 ? "s" : "" %></span>
                            </div>
                        </div>
                    </div>
                </div>

              
                <div class="reservation-prix-colonne">
                    <div class="carte-prix">
                        <h3><i class="fa-solid fa-receipt"></i> Résumé du prix</h3>
                        
                        <div class="prix-details">
                            <div class="prix-ligne">
                                <span><%= String.format("%.0f", maison.getPrixBaseNuit()) %> € × <%= nombreNuits %> nuit<%= nombreNuits > 1 ? "s" : "" %></span>
                                <span><%= String.format("%.0f", prixTotal) %> €</span>
                            </div>
                            <div class="prix-ligne">
                                <span>Frais de service</span>
                                <span class="gratuit">Inclus</span>
                            </div>
                            <div class="prix-ligne">
                                <span>Frais de ménage</span>
                                <span class="gratuit">Inclus</span>
                            </div>
                            <div class="prix-total">
                                <span>Total</span>
                                <span class="total-valeur"><%= String.format("%.0f", prixTotal) %> €</span>
                            </div>
                        </div>

                        <form action="${pageContext.request.contextPath}/reservation" method="post" class="formulaire-confirmation">
                            <input type="hidden" name="idMaison" value="<%= idMaison %>">
                            <input type="hidden" name="dateDebut" value="<%= dateDebut %>">
                            <input type="hidden" name="dateFin" value="<%= dateFin %>">
                            <input type="hidden" name="nombrePersonnes" value="<%= nombrePersonnes %>">
                            <input type="hidden" name="prixTotal" value="<%= String.format("%.0f", prixTotal) %>">

                            <button type="submit" class="bouton-confirmer-reservation">
                                <i class="fa-solid fa-check-circle"></i> Confirmer la réservation
                            </button>
                        </form>

                        <p class="info-paiement">
                            <i class="fa-solid fa-lock"></i> 
                            Vous serez redirigé vers le paiement après confirmation.
                        </p>
                    </div>

                    
                    <div class="carte-conditions">
                        <h4><i class="fa-solid fa-circle-info"></i> Conditions d'annulation</h4>
                        <ul>
                            <li>Annulation gratuite jusqu'à 48h avant l'arrivée.</li>
                            <li>Remboursement intégral en cas d'annulation par le propriétaire.</li>
                        </ul>
                    </div>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/reservation-script.js"></script>

</body>
</html>