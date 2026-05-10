<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Message" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<Message> messages = (List<Message>) request.getAttribute("messages");
    Integer idReservation = (Integer) request.getAttribute("idReservation");
    Integer idUtilisateur = (Integer) request.getAttribute("idUtilisateur");
    String maisonTitre = (String) request.getAttribute("maisonTitre");
    String locataireNom = (String) request.getAttribute("locataireNom");
    String proprietaireNom = (String) request.getAttribute("proprietaireNom");
    String messageErreur = (String) request.getAttribute("erreur");
    
    SimpleDateFormat heureFormat = new SimpleDateFormat("HH:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new java.util.Locale("fr", "FR"));
    
    // Pour regrouper les messages par date
    String dateActuelle = "";
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Conversation - <%= maisonTitre != null ? maisonTitre : "Location Maisons" %></title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/maison-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/messages-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="maison-main">
        <div class="maison-container conversation-container">

           
            <div class="conversation-entete">
                <div class="conversation-entete-gauche">
                    <a href="${pageContext.request.contextPath}/messages" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour aux messages
                    </a>
                    <div class="conversation-infos-entete">
                        <h2><i class="fa-solid fa-house"></i> <%= maisonTitre != null ? maisonTitre : "Conversation" %></h2>
                        <p>
                            <span><i class="fa-solid fa-user"></i> Locataire : <%= locataireNom != null ? locataireNom : "-" %></span>
                            <span class="separateur">•</span>
                            <span><i class="fa-solid fa-user-tie"></i> Propriétaire : <%= proprietaireNom != null ? proprietaireNom : "-" %></span>
                        </p>
                    </div>
                </div>
            </div>

           
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

            
            <div class="fil-discussion" id="filDiscussion">

                <% if (messages != null && !messages.isEmpty()) { 
                    for (Message msg : messages) {
                        boolean estMonMessage = (msg.getIdExpediteur() == idUtilisateur);
                        String dateMsg = dateFormat.format(msg.getDateEnvoi());
                        
                        // Afficher le séparateur de date si la date change
                        if (!dateMsg.equals(dateActuelle)) {
                            dateActuelle = dateMsg;
                %>
                            <div class="separateur-date">
                                <span><%= dateMsg %></span>
                            </div>
                <%      } %>
                        
                        <div class="bulle-message <%= estMonMessage ? "bulle-envoyee" : "bulle-recue" %>">
                            <div class="bulle-avatar">
                                <span class="avatar-initiales-petit">
                                    <%= msg.getPrenomExpediteur() != null && !msg.getPrenomExpediteur().isEmpty() ? msg.getPrenomExpediteur().charAt(0) : "?" %>
                                </span>
                            </div>
                            <div class="bulle-corps">
                                <div class="bulle-haut">
                                    <span class="bulle-nom"><%= estMonMessage ? "Vous" : msg.getPrenomExpediteur() %></span>
                                    <span class="bulle-heure"><%= heureFormat.format(msg.getDateEnvoi()) %></span>
                                </div>
                                <div class="bulle-texte">
                                    <%= msg.getContenu().replace("\n", "<br>") %>
                                </div>
                            </div>
                        </div>

                <%  } 
                } else { %>
                    <div class="etat-vide-messages etat-vide-petit">
                        <i class="fa-solid fa-message"></i>
                        <p>Aucun message dans cette conversation. Envoyez le premier message !</p>
                    </div>
                <% } %>

            </div>

            
            <div class="formulaire-envoi">
                <form action="${pageContext.request.contextPath}/messages/conversation" method="post">
                    <input type="hidden" name="id_reservation" value="<%= idReservation %>">
                    <div class="envoi-container">
                        <textarea id="contenu" name="contenu" rows="2" 
                                  placeholder="Écrivez votre message..." required></textarea>
                        <button type="submit" class="bouton-envoi" title="Envoyer">
                            <i class="fa-solid fa-paper-plane"></i>
                        </button>
                    </div>
                </form>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/messages-script.js"></script>
    
 
    <script>
        var fil = document.getElementById('filDiscussion');
        if (fil) {
            fil.scrollTop = fil.scrollHeight;
        }
    </script>

</body>
</html>