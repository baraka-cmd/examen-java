<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.UtilisateurInscription" %>
<%@ page import="com.examen_java.modele.ProfilInscription" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%
    UtilisateurInscription utilisateur = (UtilisateurInscription) session.getAttribute("utilisateur");
    String role = (String) session.getAttribute("role");
    String nomComplet = (String) session.getAttribute("nomComplet");
    String messageSucces = (String) request.getAttribute("succes");
    String messageErreur = (String) request.getAttribute("erreur");
    String messageErreurMdp = (String) request.getAttribute("erreurMdp");
    
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new java.util.Locale("fr", "FR"));
    
    String initiales = "";
    if (utilisateur != null) {
        if (utilisateur.getPrenom() != null && !utilisateur.getPrenom().isEmpty()) {
            initiales += utilisateur.getPrenom().charAt(0);
        }
        if (utilisateur.getNom() != null && !utilisateur.getNom().isEmpty()) {
            initiales += utilisateur.getNom().charAt(0);
        }
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mon Profil - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profil-style.css">
</head>
<body>

    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    <main class="maison-main">
        <div class="maison-container profil-container">

           
            <div class="profil-entete">
                <a href="${pageContext.request.contextPath}/dashboard" class="retour-lien">
                    <i class="fa-solid fa-arrow-left"></i> Retour au dashboard
                </a>
                <h1>
                    <i class="fa-solid fa-user-gear"></i> 
                    Mon Profil
                </h1>
                <p>Gérez vos informations personnelles et votre mot de passe.</p>
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

            
            <div class="profil-carte-identite">
                <div class="carte-avatar-section">
                    <div class="carte-avatar">
                        <span class="avatar-grand"><%= initiales %></span>
                    </div>
                    <div class="carte-statut">
                        <span class="statut-role">
                            <% if ("PROPRIETAIRE".equalsIgnoreCase(role)) { %>
                                <i class="fa-solid fa-house-laptop"></i> Propriétaire
                            <% } else if ("LOCATAIRE".equalsIgnoreCase(role)) { %>
                                <i class="fa-solid fa-magnifying-glass-location"></i> Locataire
                            <% } %>
                        </span>
                    </div>
                </div>
                <div class="carte-infos">
                    <div class="info-ligne">
                        <span class="info-label"><i class="fa-solid fa-user"></i> Nom complet</span>
                        <span class="info-valeur"><%= nomComplet %></span>
                    </div>
                    <div class="info-ligne">
                        <span class="info-label"><i class="fa-solid fa-envelope"></i> Email</span>
                        <span class="info-valeur"><%= utilisateur != null ? utilisateur.getEmail() : "" %></span>
                    </div>
                    <div class="info-ligne">
                        <span class="info-label"><i class="fa-solid fa-phone"></i> Téléphone</span>
                        <span class="info-valeur">
                            <%= utilisateur != null && utilisateur.getTelephone() != null && !utilisateur.getTelephone().isEmpty() ? utilisateur.getTelephone() : "<em>Non renseigné</em>" %>
                        </span>
                    </div>
                    <div class="info-ligne">
                        <span class="info-label"><i class="fa-solid fa-calendar"></i> Membre depuis</span>
                        <span class="info-valeur">
                            <%= utilisateur != null && utilisateur.getDateInscription() != null ? dateFormat.format(utilisateur.getDateInscription()) : "-" %>
                        </span>
                    </div>
                </div>
            </div>

            
            <div class="profil-section">
                <div class="section-titre">
                    <i class="fa-solid fa-pen-to-square"></i>
                    <h2>Modifier mes informations</h2>
                </div>

                <form action="${pageContext.request.contextPath}/mon-profil" method="post" class="profil-formulaire">
                    <input type="hidden" name="action" value="modifierProfil">

                    <div class="champ-ligne">
                        <div class="champ-groupe">
                            <label for="nom">Nom</label>
                            <input type="text" id="nom" name="nom" 
                                   value="<%= utilisateur != null ? utilisateur.getNom() : "" %>"
                                   required>
                        </div>
                        <div class="champ-groupe">
                            <label for="prenom">Prénom</label>
                            <input type="text" id="prenom" name="prenom" 
                                   value="<%= utilisateur != null ? utilisateur.getPrenom() : "" %>"
                                   required>
                        </div>
                    </div>

                    <div class="champ-groupe">
                        <label for="telephone">Téléphone</label>
                        <div class="champ-icone-wrapper">
                            <i class="fa-solid fa-mobile-screen"></i>
                            <input type="tel" id="telephone" name="telephone" 
                                   value="<%= utilisateur != null && utilisateur.getTelephone() != null ? utilisateur.getTelephone() : "" %>"
                                   placeholder="+33 6 12 34 56 78">
                        </div>
                    </div>

                    <div class="formulaire-actions">
                        <button type="submit" class="bouton-sauvegarder">
                            <i class="fa-solid fa-floppy-disk"></i> Enregistrer les modifications
                        </button>
                    </div>
                </form>
            </div>

            
            <div class="profil-section">
                <div class="section-titre">
                    <i class="fa-solid fa-lock"></i>
                    <h2>Changer mon mot de passe</h2>
                </div>

                <% if (messageErreurMdp != null) { %>
                    <div class="message-erreur" style="margin-bottom: 16px;">
                        <i class="fa-solid fa-circle-exclamation"></i>
                        <span><%= messageErreurMdp %></span>
                    </div>
                <% } %>

                <form action="${pageContext.request.contextPath}/mon-profil" method="post" class="profil-formulaire">
                    <input type="hidden" name="action" value="changerMotDePasse">

                    <div class="champ-groupe">
                        <label for="ancienMotDePasse">Mot de passe actuel</label>
                        <div class="champ-icone-wrapper">
                            <i class="fa-solid fa-key"></i>
                            <input type="password" id="ancienMotDePasse" name="ancienMotDePasse" 
                                   placeholder="Votre mot de passe actuel" required>
                        </div>
                    </div>

                    <div class="champ-ligne">
                        <div class="champ-groupe">
                            <label for="nouveauMotDePasse">Nouveau mot de passe</label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-lock"></i>
                                <input type="password" id="nouveauMotDePasse" name="nouveauMotDePasse" 
                                       placeholder="Minimum 8 caractères" required minlength="8">
                            </div>
                        </div>
                        <div class="champ-groupe">
                            <label for="confirmationMotDePasse">Confirmer</label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-lock"></i>
                                <input type="password" id="confirmationMotDePasse" name="confirmationMotDePasse" 
                                       placeholder="Répétez le nouveau mot de passe" required minlength="8">
                            </div>
                        </div>
                    </div>

                    <div class="formulaire-actions">
                        <button type="submit" class="bouton-mdp">
                            <i class="fa-solid fa-key"></i> Changer le mot de passe
                        </button>
                    </div>
                </form>
            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/profil-script.js"></script>

</body>
</html>