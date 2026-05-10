<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="com.examen_java.modele.MediaMaison" %>
<%@ page import="com.examen_java.modele.Avis" %>
<%@ page import="java.util.List" %>
<%
    Maison maison = (Maison) request.getAttribute("maison");
    List<MediaMaison> medias = (List<MediaMaison>) request.getAttribute("medias");
    int nombrePhotos = (Integer) request.getAttribute("nombrePhotos");
    boolean estFavori = (Boolean) request.getAttribute("estFavori");
    String messageSucces = (String) request.getAttribute("succes");
    
    List<Avis> avisList = (List<Avis>) request.getAttribute("avis");
    Double noteMoyenne = (Double) request.getAttribute("noteMoyenne");
    Integer nombreAvis = (Integer) request.getAttribute("nombreAvis");
    
    boolean estConnecte = (session.getAttribute("idUtilisateur") != null);
    String role = (String) session.getAttribute("role");
    boolean estLocataire = "LOCATAIRE".equalsIgnoreCase(role);
    
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
    <title><%= maison.getTitre() %> - Location Maisons</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/dashboard-style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/detail-maison-style.css">
</head>
<body>

    <% if (estConnecte) { %>
        <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />
    <% } else { %>
        <header style="background:#fff;padding:15px 30px;border-bottom:1px solid #e2e8f0;display:flex;justify-content:space-between;align-items:center;">
            <a href="${pageContext.request.contextPath}/" style="font-size:20px;font-weight:700;color:#0f172a;text-decoration:none;">
                🏠 Location Maisons
            </a>
            <div style="display:flex;gap:12px;">
                <a href="${pageContext.request.contextPath}/connexion" style="padding:10px 20px;background:#3b82f6;color:#fff;border-radius:10px;text-decoration:none;font-weight:600;">Connexion</a>
            </div>
        </header>
    <% } %>

    <main class="detail-main">
        <div class="detail-container">

            
            <div class="fil-ariane">
                <a href="${pageContext.request.contextPath}/recherche"><i class="fa-solid fa-arrow-left"></i> Retour aux résultats</a>
            </div>

            
            <% if (messageSucces != null) { %>
                <div class="message-succes">
                    <i class="fa-solid fa-circle-check"></i>
                    <span><%= messageSucces %></span>
                </div>
            <% } %>

            
            <div class="detail-galerie">
                <div class="galerie-principale">
                    <% if (medias != null && !medias.isEmpty()) { %>
                        <img id="photoPrincipale" 
                             src="${pageContext.request.contextPath}/assets/images/uploads/<%= medias.get(0).getCheminFichier() %>" 
                             alt="<%= maison.getTitre() %>">
                    <% } else { %>
                        <div class="image-placeholder-detail"><i class="fa-solid fa-house"></i></div>
                    <% } %>
                </div>
                
                <% if (medias != null && medias.size() > 1) { %>
                    <div class="galerie-miniatures">
                        <% for (int i = 0; i < Math.min(medias.size(), 5); i++) { 
                            MediaMaison media = medias.get(i); %>
                            <div class="miniature <%= i == 0 ? "miniature-active" : "" %>" 
                                 onclick="changerPhoto('<%= media.getCheminFichier() %>', this)">
                                <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= media.getCheminFichier() %>" 
                                     alt="Photo <%= i + 1 %>">
                            </div>
                        <% } %>
                        <% if (medias.size() > 5) { %>
                            <div class="miniature miniature-plus" onclick="alert('<%= medias.size() - 5 %> photo(s) supplémentaire(s) disponible(s).')">
                                <span>+<%= medias.size() - 5 %></span>
                            </div>
                        <% } %>
                    </div>
                <% } %>
            </div>

          
            <div class="detail-contenu">

           
                <div class="detail-colonne-gauche">

                    <div class="detail-section">
                        <h1><%= maison.getTitre() %></h1>
                        <div class="detail-adresse">
                            <i class="fa-solid fa-location-dot"></i>
                            <span><%= maison.getAdresseComplete() %></span>
                        </div>
                    </div>

                    <div class="detail-section">
                        <h2><i class="fa-solid fa-list-check"></i> Caractéristiques</h2>
                        <div class="caracteristiques-grille">
                            <div class="carac-item-detail">
                                <i class="fa-solid fa-users"></i>
                                <span><strong><%= maison.getCapacitePersonnes() %></strong> voyageurs</span>
                            </div>
                            <div class="carac-item-detail">
                                <i class="fa-solid fa-bed"></i>
                                <span><strong><%= maison.getNombreChambres() %></strong> chambres</span>
                            </div>
                            <div class="carac-item-detail">
                                <i class="fa-solid fa-bath"></i>
                                <span><strong><%= maison.getNombreSallesBain() %></strong> salles de bain</span>
                            </div>
                            <% if (maison.getSuperficieMetresCarres() > 0) { %>
                                <div class="carac-item-detail">
                                    <i class="fa-solid fa-maximize"></i>
                                    <span><strong><%= maison.getSuperficieMetresCarres() %> m²</strong></span>
                                </div>
                            <% } %>
                        </div>
                    </div>

                    <div class="detail-section">
                        <h2><i class="fa-solid fa-align-left"></i> Description</h2>
                        <p class="detail-description"><%= maison.getDescription() != null ? maison.getDescription().replace("\n", "<br>") : "Aucune description fournie." %></p>
                    </div>

                    <div class="detail-section">
                        <h2><i class="fa-solid fa-list-check"></i> Équipements</h2>
                        <div class="equipements-grille">
                            <% 
                                String equipements = maison.getEquipements();
                                if (equipements != null && !equipements.isEmpty() && !"[]".equals(equipements)) {
                                    equipements = equipements.replace("[", "").replace("]", "").replace("\"", "");
                                    String[] listeEquipements = equipements.split(",");
                                    for (String eq : listeEquipements) {
                                        eq = eq.trim();
                            %>
                                        <div class="equipement-item-detail">
                                            <i class="fa-solid fa-circle-check"></i>
                                            <span><%= eq %></span>
                                        </div>
                            <%      }
                                } else { %>
                                    <p class="aucun-equipement">Aucun équipement listé.</p>
                            <%  } %>
                        </div>
                    </div>

                    <% if (maison.getReglementInterieur() != null && !maison.getReglementInterieur().isEmpty()) { %>
                        <div class="detail-section">
                            <h2><i class="fa-solid fa-file-lines"></i> Règlement intérieur</h2>
                            <p class="detail-description"><%= maison.getReglementInterieur().replace("\n", "<br>") %></p>
                        </div>
                    <% } %>

                   
                    <div class="detail-section">
                        <h2>
                            <i class="fa-solid fa-star"></i> 
                            Avis des locataires
                            <% if (nombreAvis != null && nombreAvis > 0) { %>
                                <span class="avis-resume">
                                    <span class="avis-etoiles-resume">
                                        <% for (int i = 1; i <= 5; i++) { %>
                                            <%= i <= Math.round(noteMoyenne) ? "★" : "☆" %>
                                        <% } %>
                                    </span>
                                    <span class="avis-note-resume"><%= String.format("%.1f", noteMoyenne) %></span>
                                    <span class="avis-nombre">(<%= nombreAvis %> avis)</span>
                                </span>
                            <% } %>
                        </h2>

                        <% if (avisList != null && !avisList.isEmpty()) { 
                            for (Avis a : avisList) { %>
                                <div class="avis-item">
                                    <div class="avis-item-entete">
                                        <div class="avis-auteur">
                                            <span class="avis-avatar">
                                                <%= a.getAuteurPrenom() != null && !a.getAuteurPrenom().isEmpty() ? a.getAuteurPrenom().charAt(0) : "?" %>
                                            </span>
                                            <strong><%= a.getAuteurNomComplet() %></strong>
                                        </div>
                                        <div class="avis-item-note">
                                            <span class="avis-etoiles">
                                                <% for (int i = 1; i <= 5; i++) { %>
                                                    <%= i <= Math.round(a.getNoteMoyenne()) ? "★" : "☆" %>
                                                <% } %>
                                            </span>
                                            <span class="avis-date"><%= a.getDateFormatee() %></span>
                                        </div>
                                    </div>
                                    <div class="avis-notes-detail">
                                        <span>Propreté : <strong><%= a.getNoteProprete() %>/5</strong></span>
                                        <span>Communication : <strong><%= a.getNoteCommunication() %>/5</strong></span>
                                        <span>Emplacement : <strong><%= a.getNoteEmplacement() %>/5</strong></span>
                                        <span>Qualité/Prix : <strong><%= a.getNoteRapportQualitePrix() %>/5</strong></span>
                                    </div>
                                    <% if (a.getCommentaire() != null && !a.getCommentaire().isEmpty()) { %>
                                        <p class="avis-commentaire"><%= a.getCommentaire() %></p>
                                    <% } %>
                                </div>
                        <%  } 
                        } else { %>
                            <div class="etat-vide etat-vide-petit">
                                <p>Aucun avis pour cette maison. Soyez le premier à donner votre avis !</p>
                            </div>
                        <% } %>
                    </div>

                </div>

              
                <aside class="detail-colonne-droite">

                    <div class="carte-reservation-detail">
                        <div class="prix-section">
                            <span class="prix-valeur-detail"><%= String.format("%.0f", maison.getPrixBaseNuit()) %> €</span>
                            <span class="prix-periode-detail">/ nuit</span>
                        </div>

                        <form action="${pageContext.request.contextPath}/reservation" method="get" class="formulaire-reservation-detail">
                            <input type="hidden" name="idMaison" value="<%= maison.getIdMaison() %>">
                            <div class="champ-reservation">
                                <label><i class="fa-solid fa-calendar-day"></i> Arrivée</label>
                                <input type="date" name="dateDebut" required>
                            </div>
                            <div class="champ-reservation">
                                <label><i class="fa-solid fa-calendar-day"></i> Départ</label>
                                <input type="date" name="dateFin" required>
                            </div>
                            <div class="champ-reservation">
                                <label><i class="fa-solid fa-users"></i> Voyageurs</label>
                                <select name="nombrePersonnes">
                                    <% for (int i = 1; i <= maison.getCapacitePersonnes(); i++) { %>
                                        <option value="<%= i %>"><%= i %> voyageur<%= i > 1 ? "s" : "" %></option>
                                    <% } %>
                                </select>
                            </div>
                            <button type="submit" class="bouton-reserver">
                                <i class="fa-solid fa-bookmark"></i> Réserver
                            </button>
                        </form>

                        <div class="actions-detail">
                            <% if (estLocataire) { %>
                                <% if (estFavori) { %>
                                    <a href="${pageContext.request.contextPath}/favoris/supprimer?idMaison=<%= maison.getIdMaison() %>&redirect=detail" 
                                       class="bouton-favori-detail favori-actif" title="Retirer des favoris">
                                        <i class="fa-solid fa-heart"></i>
                                    </a>
                                <% } else { %>
                                    <a href="${pageContext.request.contextPath}/favoris/ajouter?idMaison=<%= maison.getIdMaison() %>" 
                                       class="bouton-favori-detail" title="Ajouter aux favoris">
                                        <i class="fa-regular fa-heart"></i>
                                    </a>
                                <% } %>
                            <% } %>
                            <a href="#" class="bouton-partager-detail" title="Partager">
                                <i class="fa-solid fa-share-nodes"></i>
                            </a>
                        </div>
                    </div>

                    <div class="carte-sidebar">
                        <h3><i class="fa-solid fa-user-tie"></i> Propriétaire</h3>
                        <p style="font-size:14px;color:#64748b;margin:0;">Géré par un propriétaire vérifié.</p>
                        <% if (estLocataire) { %>
                            <a href="${pageContext.request.contextPath}/messages" class="bouton-contacter-detail">
                                <i class="fa-solid fa-envelope"></i> Contacter le propriétaire
                            </a>
                        <% } %>
                    </div>

                </aside>

            </div>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/detail-maison-script.js"></script>

</body>
</html>