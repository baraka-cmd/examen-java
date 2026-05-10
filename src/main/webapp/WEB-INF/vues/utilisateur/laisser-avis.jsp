<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.ReservationProprio" %>
<%
    ReservationProprio reservation = (ReservationProprio) request.getAttribute("reservation");
    Integer idReservation = (Integer) request.getAttribute("idReservation");
    String messageErreur = (String) request.getAttribute("erreur");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Donner mon avis - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avis-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="avis-main">
        <div class="avis-container">

            
            <div class="avis-entete">
                <a href="${pageContext.request.contextPath}/mes-reservations" class="retour-lien">
                    <i class="fa-solid fa-arrow-left"></i> Retour à mes réservations
                </a>
                <h1>
                    <i class="fa-solid fa-star"></i> 
                    Donner mon avis
                </h1>
                <p>
                    <% if (reservation != null) { %>
                        Sur votre séjour à <strong><%= reservation.getMaisonTitre() %></strong>
                    <% } else { %>
                        Partagez votre expérience pour aider les futurs locataires.
                    <% } %>
                </p>
            </div>

           
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

           
            <div class="avis-formulaire-carte">
                <form action="${pageContext.request.contextPath}/avis/laisser" method="post" class="avis-formulaire">
                    <input type="hidden" name="id_reservation" value="<%= idReservation %>">

               
                    <div class="critere-avis">
                        <div class="critere-entete">
                            <span class="critere-icone"><i class="fa-solid fa-broom"></i></span>
                            <span class="critere-nom">Propreté</span>
                        </div>
                        <div class="critere-etoiles" data-critere="note_proprete">
                            <input type="hidden" name="note_proprete" id="note_proprete" value="0">
                            <button type="button" class="etoile" data-valeur="1"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="2"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="3"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="4"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="5"><i class="fa-regular fa-star"></i></button>
                        </div>
                    </div>

               
                    <div class="critere-avis">
                        <div class="critere-entete">
                            <span class="critere-icone"><i class="fa-solid fa-comments"></i></span>
                            <span class="critere-nom">Communication</span>
                        </div>
                        <div class="critere-etoiles" data-critere="note_communication">
                            <input type="hidden" name="note_communication" id="note_communication" value="0">
                            <button type="button" class="etoile" data-valeur="1"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="2"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="3"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="4"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="5"><i class="fa-regular fa-star"></i></button>
                        </div>
                    </div>

              
                    <div class="critere-avis">
                        <div class="critere-entete">
                            <span class="critere-icone"><i class="fa-solid fa-location-dot"></i></span>
                            <span class="critere-nom">Emplacement</span>
                        </div>
                        <div class="critere-etoiles" data-critere="note_emplacement">
                            <input type="hidden" name="note_emplacement" id="note_emplacement" value="0">
                            <button type="button" class="etoile" data-valeur="1"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="2"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="3"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="4"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="5"><i class="fa-regular fa-star"></i></button>
                        </div>
                    </div>

                    
                    <div class="critere-avis">
                        <div class="critere-entete">
                            <span class="critere-icone"><i class="fa-solid fa-tags"></i></span>
                            <span class="critere-nom">Rapport qualité/prix</span>
                        </div>
                        <div class="critere-etoiles" data-critere="note_rapport_qualite_prix">
                            <input type="hidden" name="note_rapport_qualite_prix" id="note_rapport_qualite_prix" value="0">
                            <button type="button" class="etoile" data-valeur="1"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="2"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="3"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="4"><i class="fa-regular fa-star"></i></button>
                            <button type="button" class="etoile" data-valeur="5"><i class="fa-regular fa-star"></i></button>
                        </div>
                    </div>
                    
                    

                    
                    <div class="note-globale-section">
                        <span class="globale-label">Note globale :</span>
                        <span class="globale-valeur" id="noteGlobale">-</span>
                        <span class="globale-etoiles" id="etoilesGlobales"></span>
                    </div>

                    
                    <div class="commentaire-section">
                        <label for="commentaire">
                            <i class="fa-solid fa-pen"></i> Votre commentaire
                        </label>
                        <textarea id="commentaire" name="commentaire" rows="4" 
                                  placeholder="Partagez votre expérience : ce que vous avez aimé, des conseils pour les futurs locataires..."
                                  maxlength="1000"></textarea>
                        <span class="compteur-caracteres">0 / 1000</span>
                    </div>

                    
                    <div class="avis-actions">
                        <a href="${pageContext.request.contextPath}/mes-reservations" class="bouton-annuler-avis">
                            <i class="fa-solid fa-xmark"></i> Annuler
                        </a>
                        <button type="submit" class="bouton-publier-avis">
                            <i class="fa-solid fa-paper-plane"></i> Publier mon avis
                        </button>
                    </div>

                </form>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/avis-script.js"></script>

</body>
</html>