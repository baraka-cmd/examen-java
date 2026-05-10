<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription - Location Maisons</title>

    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontawesome/fontawesome-free-7.3.0-web/css/all.css">

    <!-- Feuille de style (à créer plus tard) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/inscription-style.css">
</head>
<body>

    
    <div class="container-inscription">

        
        <div class="panneau-gauche">

            <div class="logo-icone">
                <i class="fa-solid fa-house-chimney"></i>
            </div>

            <h1>Location Maisons</h1>
            <p>Trouvez la maison de vos rêves ou proposez la vôtre à la location.</p>

            <div class="avantages">
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Réservation simple et rapide</span>
                </div>
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Paiement 100% sécurisé</span>
                </div>
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Assistance 24h/24 et 7j/7</span>
                </div>
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Annulation flexible</span>
                </div>
            </div>

            <div class="lien-connexion">
                <p>Déjà un compte ?</p>
                <a href="${pageContext.request.contextPath}/connexion">
                    <i class="fa-solid fa-arrow-right-to-bracket"></i> Se connecter
                </a>
            </div>

        </div>

    
        <div class="panneau-droit">

            <div class="entete-formulaire">
                <h2>
                    <i class="fa-solid fa-user-plus"></i>
                    Créer un compte
                </h2>
                <p>Remplissez le formulaire ci-dessous</p>
            </div>

            
            <c:if test="${not empty erreur}">
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span>${erreur}</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/inscription" method="post" class="formulaire-inscription" novalidate>

               
                <div class="groupe-champ">
                    <label for="nom">
                        <i class="fa-solid fa-address-card"></i> Nom <span class="obligatoire"></span>
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-user"></i>
                        <input type="text"
                               id="nom"
                               name="nom"
                               placeholder="Votre nom de famille"
                               value="${param.nom}"
                               required
                               autocomplete="family-name">
                    </div>
                </div>

                
                <div class="groupe-champ">
                    <label for="prenom">
                        <i class="fa-solid fa-address-card"></i> Prénom <span class="obligatoire"></span>
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-user"></i>
                        <input type="text"
                               id="prenom"
                               name="prenom"
                               placeholder="Votre prénom"
                               value="${param.prenom}"
                               required
                               autocomplete="given-name">
                    </div>
                </div>

             
                <div class="groupe-champ">
                    <label for="email">
                        <i class="fa-solid fa-envelope"></i> Adresse Email <span class="obligatoire"></span>
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-at"></i>
                        <input type="email"
                               id="email"
                               name="email"
                               placeholder="exemple@email.com"
                               value="${param.email}"
                               required
                               autocomplete="email">
                    </div>
                </div>

               
                <div class="groupe-champ">
                    <label for="telephone">
                        <i class="fa-solid fa-phone"></i> Téléphone
                        <span class="optionnel">(optionnel)</span>
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-mobile-screen"></i>
                        <input type="tel"
                               id="telephone"
                               name="telephone"
                               placeholder="+33 6 12 34 56 78"
                               value="${param.telephone}"
                               autocomplete="tel">
                    </div>
                </div>

               
                <div class="groupe-champ">
                    <label for="motDePasse">
                        <i class="fa-solid fa-lock"></i> Mot de passe <span class="obligatoire"></span>
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-key"></i>
                        <input type="password"
                               id="motDePasse"
                               name="motDePasse"
                               placeholder="Minimum 8 caractères"
                               required
                               autocomplete="new-password">
                        <button type="button" class="bouton-visibilite" tabindex="-1">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                </div>

             
                <div class="groupe-champ">
                    <label for="confirmation">
                        <i class="fa-solid fa-lock"></i> Confirmer le mot de passe <span class="obligatoire"></span>
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-key"></i>
                        <input type="password"
                               id="confirmation"
                               name="confirmation"
                               placeholder="Répétez le mot de passe"
                               required
                               autocomplete="new-password">
                        <button type="button" class="bouton-visibilite" tabindex="-1">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                </div>

              
                <div class="groupe-champ">
                    <label>
                        <i class="fa-solid fa-user-tag"></i> Vous êtes ? <span class="obligatoire"></span>
                    </label>
                    <div class="selecteur-role">
                        <label class="carte-role">
                            <input type="radio" name="role" value="LOCATAIRE" checked>
                            <span class="contenu-role">
                                <i class="fa-solid fa-magnifying-glass-location"></i>
                                <strong>Locataire</strong>
                                <small>Je cherche une maison</small>
                            </span>
                        </label>
                        <label class="carte-role">
                            <input type="radio" name="role" value="PROPRIETAIRE">
                            <span class="contenu-role">
                                <i class="fa-solid fa-house-laptop"></i>
                                <strong>Propriétaire</strong>
                                <small>Je propose une maison</small>
                            </span>
                        </label>
                    </div>
                </div>

             
                <div class="conditions">
                    <label>
                        <input type="checkbox" name="conditions" required>
                        <span>
                            J'accepte les
                            <a href="#">Conditions d'utilisation</a>
                            et la
                            <a href="#">Politique de confidentialité</a>
                        </span>
                    </label>
                </div>

               
                <button type="submit" class="bouton-inscription">
                    <i class="fa-solid fa-user-plus"></i> Créer mon compte
                </button>

            </form>

            
            <div class="retour-accueil">
                <a href="${pageContext.request.contextPath}/accueil">
                    <i class="fa-solid fa-arrow-left"></i> Retour à l'accueil
                </a>
            </div>

        </div>

    </div>

   
    <script src="${pageContext.request.contextPath}/assets/js/inscription-script.js"></script>

</body>
</html>