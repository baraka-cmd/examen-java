<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Message" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    List<Message> conversations = (List<Message>) request.getAttribute("conversations");
    int messagesNonLus = (Integer) request.getAttribute("messagesNonLus");
    int nombreConversations = (Integer) request.getAttribute("nombreConversations");
    String messageErreur = (String) request.getAttribute("erreur");
    String messageSucces = (String) request.getAttribute("succes");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy 'à' HH:mm", new java.util.Locale("fr", "FR"));
    SimpleDateFormat dateCourte = new SimpleDateFormat("dd/MM");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Messages - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/maison-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/messages-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="maison-main">
        <div class="maison-container">

            
            <div class="messages-entete">
                <div class="entete-gauche">
                    <a href="${pageContext.request.contextPath}/dashboard" class="retour-lien">
                        <i class="fa-solid fa-arrow-left"></i> Retour au dashboard
                    </a>
                    <h1>
                        <i class="fa-solid fa-envelope"></i> 
                        Messages
                    </h1>
                    <p>
                        <% if (nombreConversations > 0) { %>
                            <strong><%= nombreConversations %></strong> conversation<%= nombreConversations > 1 ? "s" : "" %>
                            <% if (messagesNonLus > 0) { %>
                                &nbsp;•&nbsp; 
                                <span class="non-lus-indicateur"><%= messagesNonLus %></span> non lu<%= messagesNonLus > 1 ? "s" : "" %>
                            <% } %>
                        <% } else { %>
                            Aucune conversation pour le moment.
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

            
            <% if (conversations != null && !conversations.isEmpty()) { %>
                <div class="conversations-liste">

                    <% for (Message conv : conversations) { %>
                        <a href="${pageContext.request.contextPath}/messages/conversation?id_reservation=<%= conv.getIdReservation() %>" 
                           class="conversation-item <%= !conv.isEstLu() && conv.getIdExpediteur() != (Integer) session.getAttribute("idUtilisateur") ? "conversation-non-lue" : "" %>">

                          
                            <div class="conversation-avatar">
                                <span class="avatar-initiales-message">
                                    <%= conv.getPrenomExpediteur() != null && !conv.getPrenomExpediteur().isEmpty() ? conv.getPrenomExpediteur().charAt(0) : "?" %><%= conv.getNomExpediteur() != null && !conv.getNomExpediteur().isEmpty() ? conv.getNomExpediteur().charAt(0) : "?" %>
                                </span>
                            </div>

                            
                            <div class="conversation-contenu">
                                <div class="conversation-haut">
                                    <span class="conversation-nom"><%= conv.getNomCompletExpediteur() %></span>
                                    <span class="conversation-date"><%= dateCourte.format(conv.getDateEnvoi()) %></span>
                                </div>
                                <div class="conversation-bas">
                                    <span class="conversation-sujet">
                                        <i class="fa-solid fa-house"></i> <%= conv.getMaisonTitre() %>
                                    </span>
                                </div>
                                <div class="conversation-apercu">
                                    <% if (!conv.isEstLu() && conv.getIdExpediteur() != (Integer) session.getAttribute("idUtilisateur")) { %>
                                        <span class="badge-non-lu">●</span>
                                    <% } %>
                                    <span><%= conv.getApercu() %></span>
                                </div>
                            </div>

                            
                            <div class="conversation-fleche">
                                <i class="fa-solid fa-chevron-right"></i>
                            </div>

                        </a>
                    <% } %>

                </div>
            <% } else { %>

               
                <div class="etat-vide-messages">
                    <div class="vide-illustration">
                        <i class="fa-solid fa-envelope-open"></i>
                    </div>
                    <h2>Aucune conversation</h2>
                    <p>Vous n'avez pas encore échangé de messages. Les conversations apparaîtront ici lorsqu'un locataire vous enverra un message ou que vous contacterez un locataire.</p>
                </div>

            <% } %>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/messages-script.js"></script>

</body>
</html>