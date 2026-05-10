<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String messageErreur = (String) request.getAttribute("erreur");
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter une maison - Location Maisons</title>

 
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/maison-style.css">
</head>
<body>

   
    <jsp:include page="/WEB-INF/vues/commun/header-dashboard.jsp" />

    
    <main class="maison-main">
        <div class="maison-container">

            
            <div class="formulaire-entete">
                <a href="${pageContext.request.contextPath}/mes-maisons" class="retour-lien">
                    <i class="fa-solid fa-arrow-left"></i> Retour à mes annonces
                </a>
                <h1>
                    <i class="fa-solid fa-circle-plus"></i> 
                    Ajouter une nouvelle maison
                </h1>
                <p>Remplissez les informations ci-dessous pour publier votre annonce.</p>
            </div>

            
            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

           
            <form action="${pageContext.request.contextPath}/maison/ajouter" 
                  method="post" 
                  enctype="multipart/form-data" 
                  class="formulaire-maison" 
                  novalidate>

               
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-circle-info"></i>
                        <h2>Informations générales</h2>
                    </div>

                    <div class="champ-groupe">
                        <label for="titre">
                            Titre de l'annonce <span class="obligatoire">*</span>
                        </label>
                        <input type="text" 
                               id="titre" 
                               name="titre" 
                               placeholder="Ex : Charmante maison avec piscine à Aix-en-Provence" 
                               value="<%= request.getParameter("titre") != null ? request.getParameter("titre") : "" %>"
                               required 
                               maxlength="255">
                    </div>

                    <div class="champ-groupe">
                        <label for="description">Description</label>
                        <textarea id="description" 
                                  name="description" 
                                  rows="5" 
                                  placeholder="Décrivez votre maison, son environnement, ses atouts..."><%= request.getParameter("description") != null ? request.getParameter("description") : "" %></textarea>
                    </div>

                    <div class="champ-ligne">
                        <div class="champ-groupe champ-moitie">
                            <label for="prixBaseNuit">
                                Prix par nuit (€) <span class="obligatoire">*</span>
                            </label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-euro-sign"></i>
                                <input type="number" 
                                       id="prixBaseNuit" 
                                       name="prixBaseNuit" 
                                       placeholder="150" 
                                       value="<%= request.getParameter("prixBaseNuit") != null ? request.getParameter("prixBaseNuit") : "" %>"
                                       required 
                                       min="1" 
                                       step="0.01">
                            </div>
                        </div>
                        <div class="champ-groupe champ-moitie">
                            <label for="capacitePersonnes">
                                Capacité (personnes) <span class="obligatoire">*</span>
                            </label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-users"></i>
                                <input type="number" 
                                       id="capacitePersonnes" 
                                       name="capacitePersonnes" 
                                       placeholder="6" 
                                       value="<%= request.getParameter("capacitePersonnes") != null ? request.getParameter("capacitePersonnes") : "" %>"
                                       required 
                                       min="1">
                            </div>
                        </div>
                    </div>
                </div>

              
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-house"></i>
                        <h2>Caractéristiques</h2>
                    </div>

                    <div class="champ-ligne">
                        <div class="champ-groupe champ-moitie">
                            <label for="nombreChambres">Nombre de chambres</label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-bed"></i>
                                <input type="number" 
                                       id="nombreChambres" 
                                       name="nombreChambres" 
                                       placeholder="3" 
                                       value="<%= request.getParameter("nombreChambres") != null ? request.getParameter("nombreChambres") : "1" %>"
                                       min="0">
                            </div>
                        </div>
                        <div class="champ-groupe champ-moitie">
                            <label for="nombreSallesBain">Salles de bain</label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-bath"></i>
                                <input type="number" 
                                       id="nombreSallesBain" 
                                       name="nombreSallesBain" 
                                       placeholder="2" 
                                       value="<%= request.getParameter("nombreSallesBain") != null ? request.getParameter("nombreSallesBain") : "1" %>"
                                       min="0">
                            </div>
                        </div>
                    </div>

                    <div class="champ-groupe">
                        <label for="superficie">Superficie (m²)</label>
                        <div class="champ-icone-wrapper">
                            <i class="fa-solid fa-maximize"></i>
                            <input type="number" 
                                   id="superficie" 
                                   name="superficie" 
                                   placeholder="120" 
                                   value="<%= request.getParameter("superficie") != null ? request.getParameter("superficie") : "" %>"
                                   min="0">
                        </div>
                    </div>
                </div>

            
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-location-dot"></i>
                        <h2>Adresse</h2>
                    </div>

                    <div class="champ-groupe">
                        <label for="adresseLigne1">
                            Adresse <span class="obligatoire">*</span>
                        </label>
                        <input type="text" 
                               id="adresseLigne1" 
                               name="adresseLigne1" 
                               placeholder="15 Rue des Oliviers" 
                               value="<%= request.getParameter("adresseLigne1") != null ? request.getParameter("adresseLigne1") : "" %>"
                               required>
                    </div>

                    <div class="champ-groupe">
                        <label for="adresseLigne2">Complément d'adresse</label>
                        <input type="text" 
                               id="adresseLigne2" 
                               name="adresseLigne2" 
                               placeholder="Appartement 3, Bâtiment B" 
                               value="<%= request.getParameter("adresseLigne2") != null ? request.getParameter("adresseLigne2") : "" %>">
                    </div>

                    <div class="champ-ligne">
                        <div class="champ-groupe champ-moitie">
                            <label for="ville">
                                Ville <span class="obligatoire">*</span>
                            </label>
                            <input type="text" 
                                   id="ville" 
                                   name="ville" 
                                   placeholder="Aix-en-Provence" 
                                   value="<%= request.getParameter("ville") != null ? request.getParameter("ville") : "" %>"
                                   required>
                        </div>
                        <div class="champ-groupe champ-moitie">
                            <label for="codePostal">Code postal</label>
                            <input type="text" 
                                   id="codePostal" 
                                   name="codePostal" 
                                   placeholder="13100" 
                                   value="<%= request.getParameter("codePostal") != null ? request.getParameter("codePostal") : "" %>">
                        </div>
                    </div>

                    <div class="champ-groupe">
                        <label for="pays">Pays</label>
                        <input type="text" 
                               id="pays" 
                               name="pays" 
                               placeholder="France" 
                               value="<%= request.getParameter("pays") != null ? request.getParameter("pays") : "France" %>">
                    </div>
                </div>

              
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-list-check"></i>
                        <h2>Équipements</h2>
                    </div>

                    <div class="grille-equipements">
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Wi-Fi">
                            <i class="fa-solid fa-wifi"></i> Wi-Fi
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Cuisine">
                            <i class="fa-solid fa-kitchen-set"></i> Cuisine
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Climatisation">
                            <i class="fa-solid fa-fan"></i> Climatisation
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Parking gratuit">
                            <i class="fa-solid fa-square-parking"></i> Parking gratuit
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Piscine">
                            <i class="fa-solid fa-water-ladder"></i> Piscine
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Lave-linge">
                            <i class="fa-solid fa-shirt"></i> Lave-linge
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Sèche-cheveux">
                            <i class="fa-solid fa-wind"></i> Sèche-cheveux
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Télévision">
                            <i class="fa-solid fa-tv"></i> Télévision
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Animaux acceptés">
                            <i class="fa-solid fa-paw"></i> Animaux acceptés
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Jardin">
                            <i class="fa-solid fa-tree"></i> Jardin
                        </label>
                    </div>
                </div>

                
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-images"></i>
                        <h2>Photos</h2>
                        <span class="section-info">Maximum 5 photos. La première sera la photo principale.</span>
                    </div>

                    <div class="zone-upload" id="zoneUpload">
                        <input type="file" 
                               id="photos" 
                               accept="image/*" 
                               multiple 
                               class="input-file">
                        <label for="photos" class="upload-label" id="uploadLabel">
                            <i class="fa-solid fa-cloud-arrow-up"></i>
                            <span class="upload-texte">Cliquez pour choisir des photos</span>
                            <span class="upload-info">JPG, PNG ou WebP - Max 10 Mo par photo</span>
                        </label>
                    </div>

                    <div class="preview-photos" id="previewPhotos"></div>

                    <p class="compteur-photos" id="compteurPhotos" style="display:none;">
                        <span id="nbPhotos">0</span>/5 photos sélectionnées
                    </p>

                   
                    <div id="photosHiddenInputs"></div>
                </div>
                
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-file-lines"></i>
                        <h2>Règlement intérieur</h2>
                        <span class="section-info">Optionnel</span>
                    </div>

                    <div class="champ-groupe">
                        <textarea id="reglementInterieur" 
                                  name="reglementInterieur" 
                                  rows="4" 
                                  placeholder="Heure d'arrivée, heure de départ, règles de la maison..."><%= request.getParameter("reglementInterieur") != null ? request.getParameter("reglementInterieur") : "" %></textarea>
                    </div>
                </div>

                
                <div class="formulaire-actions">
                    <a href="${pageContext.request.contextPath}/mes-maisons" class="bouton-annuler">
                        <i class="fa-solid fa-xmark"></i> Annuler
                    </a>
                    <button type="submit" class="bouton-publier">
                        <i class="fa-solid fa-circle-plus"></i> Publier l'annonce
                    </button>
                </div>

            </form>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />
    
    <script src="${pageContext.request.contextPath}/assets/js/maison-script.js"></script>

</body>
</html>