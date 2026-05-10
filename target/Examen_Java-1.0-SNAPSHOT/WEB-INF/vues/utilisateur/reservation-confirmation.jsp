<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Boolean successObj = (Boolean) request.getAttribute("success");
    boolean estSucces = (successObj != null) ? successObj : false;
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservation confirmée - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/reservation-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="reservation-main">
        <div class="reservation-container">

            
            <div class="confirmation-carte">
                
                <% if (estSucces) { %>
                    
                    <div class="confirmation-icone succes">
                        <i class="fa-solid fa-circle-check"></i>
                    </div>
                    <h1>Réservation confirmée !</h1>
                    <p class="confirmation-message">
                        Votre réservation a été enregistrée avec succès. 
                        Le propriétaire va maintenant examiner votre demande.
                    </p>
                    <div class="confirmation-etapes">
                        <div class="etape etape-active">
                            <div class="etape-icone"><i class="fa-solid fa-check"></i></div>
                            <span>Réservation</span>
                        </div>
                        <div class="etape-ligne"></div>
                        <div class="etape etape-active">
                            <div class="etape-icone"><i class="fa-solid fa-check"></i></div>
                            <span>Confirmation</span>
                        </div>
                        <div class="etape-ligne"></div>
                        <div class="etape">
                            <div class="etape-icone"><i class="fa-solid fa-clock"></i></div>
                            <span>Paiement</span>
                        </div>
                    </div>
                <% } else { %>
                    
                    <div class="confirmation-icone erreur">
                        <i class="fa-solid fa-circle-xmark"></i>
                    </div>
                    <h1>Erreur lors de la réservation</h1>
                    <p class="confirmation-message">
                        Une erreur est survenue lors de votre réservation. Veuillez réessayer.
                    </p>
                <% } %>

               
                <div class="confirmation-actions">
                    <a href="${pageContext.request.contextPath}/dashboard" class="bouton-primaire">
                        <i class="fa-solid fa-table-columns"></i> Mon dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/recherche" class="bouton-secondaire">
                        <i class="fa-solid fa-magnifying-glass"></i> Continuer à chercher
                    </a>
                </div>

                
                <div class="confirmation-infos">
                    <div class="info-item">
                        <i class="fa-solid fa-envelope"></i>
                        <span>Un email de confirmation vous sera envoyé.</span>
                    </div>
                    <div class="info-item">
                        <i class="fa-solid fa-message"></i>
                        <span>Vous pouvez contacter le propriétaire via la messagerie.</span>
                    </div>
                    <div class="info-item">
                        <i class="fa-solid fa-calendar-check"></i>
                        <span>Suivez l'état de votre réservation dans votre dashboard.</span>
                    </div>
                </div>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

</body>
</html>