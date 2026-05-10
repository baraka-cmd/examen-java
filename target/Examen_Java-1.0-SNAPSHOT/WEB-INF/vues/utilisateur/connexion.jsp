<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - Location Maisons</title>

    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/connexion-style.css">
</head>
<body>

    
    <div class="container-connexion">

        
        <div class="panneau-gauche">

            <div class="logo-icone">
                <i class="fa-solid fa-house-chimney"></i>
            </div>

            <h1>Location Maisons</h1>
            <p>Connectez-vous pour gérer vos réservations ou vos annonces en quelques clics.</p>

            <div class="avantages">
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Accédez à votre tableau de bord</span>
                </div>
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Gérez vos réservations</span>
                </div>
                <div class="avantage-item">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Publiez vos annonces</span>
                </div>
            </div>

            <div class="lien-inscription">
                <p>Pas encore de compte ?</p>
                <a href="${pageContext.request.contextPath}/inscription">
                    <i class="fa-solid fa-user-plus"></i> Créer un compte
                </a>
            </div>

        </div>

       
        <div class="panneau-droit">

            <div class="entete-formulaire">
                <h2>
                    <i class="fa-solid fa-arrow-right-to-bracket"></i>
                    Connexion
                </h2>
                <p>Connectez-vous à votre compte</p>
            </div>

            
            <c:if test="${not empty succes}">
                <div class="message-succes">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>${succes}</span>
                </div>
            </c:if>

            
            <c:if test="${param.deconnexion eq 'succes'}">
                <div class="message-succes">
                    <i class="fa-solid fa-circle-check"></i>
                    <span>Vous avez été déconnecté avec succès.</span>
                </div>
            </c:if>

          
            <c:if test="${not empty erreur}">
                <div class="message-erreur">
                    <i class="fa-solid fa-circle-exclamation"></i>
                    <span>${erreur}</span>
                </div>
            </c:if>

          
            <form action="${pageContext.request.contextPath}/connexion" method="post" class="formulaire-connexion" novalidate>

               
                <div class="groupe-champ">
                    <label for="email">
                        <i class="fa-solid fa-envelope"></i> Adresse Email
                    </label>
                    <div class="champ-icone">
                        <i class="fa-solid fa-at"></i>
                        <input type="email"
                               id="email"
                               name="email"
                               placeholder="exemple@email.com"
                               value="${not empty emailSaisi ? emailSaisi : param.email}"
                               required
                               autocomplete="email"
                               autofocus>
                    </div>
                </div>

                
                <div class="groupe-champ">
                    <div class="label-lien">
                        <label for="motDePasse">
                            <i class="fa-solid fa-lock"></i> Mot de passe
                        </label>
                        <a href="#" class="lien-mdp-oublie">
                            <i class="fa-solid fa-circle-question"></i> Mot de passe oublié ?
                        </a>
                    </div>
                    <div class="champ-icone">
                        <i class="fa-solid fa-key"></i>
                        <input type="password"
                               id="motDePasse"
                               name="motDePasse"
                               placeholder="Votre mot de passe"
                               required
                               autocomplete="current-password">
                        <button type="button" class="bouton-visibilite" tabindex="-1" aria-label="Afficher le mot de passe">
                            <i class="fa-solid fa-eye"></i>
                        </button>
                    </div>
                </div>

                
                <div class="souvenir-moi">
                    <label>
                        <input type="checkbox" name="souvenir" value="true">
                        <span>Se souvenir de moi</span>
                    </label>
                </div>

                
                <button type="submit" class="bouton-connexion">
                    <i class="fa-solid fa-right-to-bracket"></i> Se connecter
                </button>

            </form>

            
            <div class="retour-accueil">
                <a href="${pageContext.request.contextPath}/accueil">
                    <i class="fa-solid fa-arrow-left"></i> Retour à l'accueil
                </a>
            </div>

        </div>

    </div>

    
    <script src="${pageContext.request.contextPath}/assets/js/connexion-script.js"></script>

</body>
</html>