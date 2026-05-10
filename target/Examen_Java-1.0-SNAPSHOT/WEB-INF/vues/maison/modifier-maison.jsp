<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.examen_java.modele.Maison" %>
<%@ page import="com.examen_java.modele.MediaMaison" %>
<%@ page import="java.util.List" %>
<%
    Maison maison = (Maison) request.getAttribute("maison");
    List<MediaMaison> medias = (List<MediaMaison>) request.getAttribute("medias");
    String messageErreur = (String) request.getAttribute("erreur");
    
    if (maison == null) {
        response.sendRedirect(request.getContextPath() + "/mes-maisons");
        return;
    }
%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier - <%= maison.getTitre() %> - Location Maisons</title>

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
                    <i class="fa-solid fa-pen-to-square"></i> 
                    Modifier : <%= maison.getTitre() %>
                </h1>
                <p>Modifiez les informations de votre annonce.</p>
            </div>

            <% if (messageErreur != null) { %>
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span><%= messageErreur %></span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/maison/modifier" 
                  method="post" 
                  enctype="multipart/form-data" 
                  class="formulaire-maison" 
                  novalidate>

                <input type="hidden" name="idMaison" value="<%= maison.getIdMaison() %>">

                
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-circle-info"></i>
                        <h2>Informations générales</h2>
                    </div>

                    <div class="champ-groupe">
                        <label for="titre">Titre de l'annonce <span class="obligatoire">*</span></label>
                        <input type="text" id="titre" name="titre" 
                               value="<%= maison.getTitre() != null ? maison.getTitre() : "" %>"
                               required maxlength="255">
                    </div>

                    <div class="champ-groupe">
                        <label for="description">Description</label>
                        <textarea id="description" name="description" rows="5" 
                                  placeholder="Décrivez votre maison..."><%= maison.getDescription() != null ? maison.getDescription() : "" %></textarea>
                    </div>

                    <div class="champ-ligne">
                        <div class="champ-groupe champ-moitie">
                            <label for="prixBaseNuit">Prix par nuit (€) <span class="obligatoire">*</span></label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-euro-sign"></i>
                                <input type="number" id="prixBaseNuit" name="prixBaseNuit" 
                                       value="<%= (int) maison.getPrixBaseNuit() %>"
                                       required min="1" step="0.01">
                            </div>
                        </div>
                        <div class="champ-groupe champ-moitie">
                            <label for="capacitePersonnes">Capacité (personnes) <span class="obligatoire">*</span></label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-users"></i>
                                <input type="number" id="capacitePersonnes" name="capacitePersonnes" 
                                       value="<%= maison.getCapacitePersonnes() %>"
                                       required min="1">
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
                            <label for="nombreChambres">Chambres</label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-bed"></i>
                                <input type="number" id="nombreChambres" name="nombreChambres" 
                                       value="<%= maison.getNombreChambres() %>" min="0">
                            </div>
                        </div>
                        <div class="champ-groupe champ-moitie">
                            <label for="nombreSallesBain">Salles de bain</label>
                            <div class="champ-icone-wrapper">
                                <i class="fa-solid fa-bath"></i>
                                <input type="number" id="nombreSallesBain" name="nombreSallesBain" 
                                       value="<%= maison.getNombreSallesBain() %>" min="0">
                            </div>
                        </div>
                    </div>

                    <div class="champ-groupe">
                        <label for="superficie">Superficie (m²)</label>
                        <div class="champ-icone-wrapper">
                            <i class="fa-solid fa-maximize"></i>
                            <input type="number" id="superficie" name="superficie" 
                                   value="<%= maison.getSuperficieMetresCarres() > 0 ? maison.getSuperficieMetresCarres() : "" %>"
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
                        <label for="adresseLigne1">Adresse <span class="obligatoire">*</span></label>
                        <input type="text" id="adresseLigne1" name="adresseLigne1" 
                               value="<%= maison.getAdresseLigne1() != null ? maison.getAdresseLigne1() : "" %>" required>
                    </div>

                    <div class="champ-groupe">
                        <label for="adresseLigne2">Complément d'adresse</label>
                        <input type="text" id="adresseLigne2" name="adresseLigne2" 
                               value="<%= maison.getAdresseLigne2() != null ? maison.getAdresseLigne2() : "" %>">
                    </div>

                    <div class="champ-ligne">
                        <div class="champ-groupe champ-moitie">
                            <label for="ville">Ville <span class="obligatoire">*</span></label>
                            <input type="text" id="ville" name="ville" 
                                   value="<%= maison.getVille() != null ? maison.getVille() : "" %>" required>
                        </div>
                        <div class="champ-groupe champ-moitie">
                            <label for="codePostal">Code postal</label>
                            <input type="text" id="codePostal" name="codePostal" 
                                   value="<%= maison.getCodePostal() != null ? maison.getCodePostal() : "" %>">
                        </div>
                    </div>

                    <div class="champ-groupe">
                        <label for="pays">Pays</label>
                        <input type="text" id="pays" name="pays" 
                               value="<%= maison.getPays() != null ? maison.getPays() : "France" %>">
                    </div>
                </div>

                
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-list-check"></i>
                        <h2>Équipements</h2>
                    </div>

                    <%
                        String equipementsExistants = maison.getEquipements();
                    %>
                    <div class="grille-equipements">
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Wi-Fi"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Wi-Fi") ? "checked" : "" %>>
                            <i class="fa-solid fa-wifi"></i> Wi-Fi
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Cuisine"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Cuisine") ? "checked" : "" %>>
                            <i class="fa-solid fa-kitchen-set"></i> Cuisine
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Climatisation"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Climatisation") ? "checked" : "" %>>
                            <i class="fa-solid fa-fan"></i> Climatisation
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Parking gratuit"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Parking gratuit") ? "checked" : "" %>>
                            <i class="fa-solid fa-square-parking"></i> Parking gratuit
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Piscine"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Piscine") ? "checked" : "" %>>
                            <i class="fa-solid fa-water-ladder"></i> Piscine
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Lave-linge"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Lave-linge") ? "checked" : "" %>>
                            <i class="fa-solid fa-shirt"></i> Lave-linge
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Sèche-cheveux"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Sèche-cheveux") ? "checked" : "" %>>
                            <i class="fa-solid fa-wind"></i> Sèche-cheveux
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Télévision"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Télévision") ? "checked" : "" %>>
                            <i class="fa-solid fa-tv"></i> Télévision
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Animaux acceptés"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Animaux acceptés") ? "checked" : "" %>>
                            <i class="fa-solid fa-paw"></i> Animaux acceptés
                        </label>
                        <label class="equipement-item">
                            <input type="checkbox" name="equipements" value="Jardin"
                                   <%= equipementsExistants != null && equipementsExistants.contains("Jardin") ? "checked" : "" %>>
                            <i class="fa-solid fa-tree"></i> Jardin
                        </label>
                    </div>
                </div>

          
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-images"></i>
                        <h2>Photos actuelles</h2>
                        <span class="section-info"><%= medias != null ? medias.size() : 0 %> photo(s)</span>
                    </div>

                    
                    <div class="photos-existantes" id="photosExistantes">
                        <% if (medias != null && !medias.isEmpty()) { 
                            for (MediaMaison media : medias) { %>
                                <div class="photo-existante" id="photo-<%= media.getIdMedia() %>">
                                    <img src="${pageContext.request.contextPath}/assets/images/uploads/<%= media.getCheminFichier() %>" 
                                         alt="Photo"
                                         onerror="this.style.display='none';this.parentElement.querySelector('.photo-placeholder').style.display='flex';">
                                    <div class="photo-placeholder" style="display:none;width:100%;height:160px;align-items:center;justify-content:center;background:#f1f5f9;">
                                        <i class="fa-solid fa-image" style="font-size:32px;color:#94a3b8;"></i>
                                    </div>
                                    <div class="photo-actions">
                                        <label class="photo-radio-label">
                                            <input type="radio" name="photoPrincipale" value="<%= media.getIdMedia() %>"
                                                   <%= media.isEstImagePrincipale() ? "checked" : "" %>>
                                            <span><i class="fa-solid fa-star"></i> Principale</span>
                                        </label>
                                        <button type="button" class="photo-supprimer-btn" 
                                                onclick="marquerSuppression(<%= media.getIdMedia() %>)"
                                                title="Supprimer cette photo">
                                            <i class="fa-solid fa-trash"></i> Supprimer
                                        </button>
                                    </div>
                                </div>
                        <%  } 
                        } else { %>
                            <p class="aucune-photo">Aucune photo pour cette annonce.</p>
                        <% } %>
                    </div>

                   
                    <div id="supprimerPhotosContainer"></div>

                    
                    <div class="section-titre" style="margin-top: 24px;">
                        <i class="fa-solid fa-cloud-arrow-up"></i>
                        <h2>Ajouter des photos</h2>
                        <span class="section-info">Max 5 photos au total</span>
                    </div>

                    <div class="zone-upload" id="zoneUploadModifier">
                        <input type="file" id="nouvellesPhotosModifier" 
                               accept="image/*" multiple class="input-file">
                        <label for="nouvellesPhotosModifier" class="upload-label" id="uploadLabelModifier">
                            <i class="fa-solid fa-cloud-arrow-up"></i>
                            <span class="upload-texte">Cliquez pour choisir des photos</span>
                            <span class="upload-info">JPG, PNG ou WebP - Max 10 Mo par photo</span>
                        </label>
                    </div>

                    <div class="preview-photos" id="previewPhotosModifier"></div>
                    
                    <p class="compteur-photos" id="compteurPhotosModifier" style="display:none;">
                        <span id="nbPhotosModifier">0</span>/5 nouvelles photos
                    </p>
                </div>

                
                <div class="formulaire-section">
                    <div class="section-titre">
                        <i class="fa-solid fa-file-lines"></i>
                        <h2>Règlement intérieur</h2>
                        <span class="section-info">Optionnel</span>
                    </div>

                    <div class="champ-groupe">
                        <textarea id="reglementInterieur" name="reglementInterieur" rows="4" 
                                  placeholder="Heure d'arrivée, heure de départ, règles..."><%= maison.getReglementInterieur() != null ? maison.getReglementInterieur() : "" %></textarea>
                    </div>
                </div>

               
                <div class="formulaire-actions">
                    <a href="${pageContext.request.contextPath}/mes-maisons" class="bouton-annuler">
                        <i class="fa-solid fa-xmark"></i> Annuler
                    </a>
                    <button type="submit" class="bouton-publier" id="boutonModifier">
                        <i class="fa-solid fa-check"></i> Enregistrer les modifications
                    </button>
                </div>

            </form>

        </div>
    </main>

    <jsp:include page="/WEB-INF/vues/commun/footer.jsp" />

    <script src="${pageContext.request.contextPath}/assets/js/maison-script.js"></script>

</body>
</html>