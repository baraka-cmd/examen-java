<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Calendar" %>
<%
    int anneeActuelle = Calendar.getInstance().get(Calendar.YEAR);
    String roleFooter = (String) session.getAttribute("role");
    boolean estConnecteFooter = (session.getAttribute("idUtilisateur") != null);
%>

<footer class="site-footer">
    <div class="footer-container">

        <div class="footer-colonne footer-colonne-large">
            <div class="footer-logo">
                <div class="footer-logo-icone">
                    <i class="fa-solid fa-house-chimney"></i>
                </div>
                <div class="footer-logo-texte">
                    <span class="logo-nom">Location Maisons</span>
                    <span class="logo-slogan">Trouvez la maison de vos rêves</span>
                </div>
            </div>
            <p class="footer-description">
                La plateforme de location de maisons la plus simple et la plus sécurisée. 
                Que vous soyez locataire ou propriétaire, nous vous accompagnons à chaque étape.
            </p>
            <div class="footer-reseaux">
                <span class="reseaux-titre">Suivez-nous</span>
                <div class="reseaux-icones">
                    <a href="https://facebook.com/irbarakapuruzi" target="_blank" rel="noopener" class="reseau-lien" title="Facebook" aria-label="Facebook">
                        <i class="fa-brands fa-facebook-f"></i>
                    </a>
                    <a href="#" class="reseau-lien" title="Instagram" aria-label="Instagram">
                        <i class="fa-brands fa-instagram"></i>
                    </a>
                    <a href="#" class="reseau-lien" title="X (Twitter)" aria-label="X">
                        <i class="fa-brands fa-x-twitter"></i>
                    </a>
                    <a href="#" class="reseau-lien" title="LinkedIn" aria-label="LinkedIn">
                        <i class="fa-brands fa-linkedin-in"></i>
                    </a>
                    <a href="https://wa.me/243984270438" target="_blank" rel="noopener" class="reseau-lien" title="WhatsApp" aria-label="WhatsApp">
                        <i class="fa-brands fa-whatsapp"></i>
                    </a>
                </div>
            </div>
        </div>

        
        <div class="footer-colonne">
            <h4>Navigation</h4>
            <ul class="footer-liens">
                <li><a href="${pageContext.request.contextPath}/accueil"><i class="fa-solid fa-chevron-right"></i> Accueil</a></li>
                <li><a href="${pageContext.request.contextPath}/recherche"><i class="fa-solid fa-chevron-right"></i> Explorer</a></li>
                
                <%-- Liens selon statut --%>
                <% if (!estConnecteFooter) { %>
                    <li><a href="${pageContext.request.contextPath}/connexion"><i class="fa-solid fa-chevron-right"></i> Connexion</a></li>
                    <li><a href="${pageContext.request.contextPath}/inscription"><i class="fa-solid fa-chevron-right"></i> Inscription</a></li>
                <% } else if ("LOCATAIRE".equalsIgnoreCase(roleFooter)) { %>
                    <li><a href="${pageContext.request.contextPath}/mes-reservations"><i class="fa-solid fa-chevron-right"></i> Mes réservations</a></li>
                    <li><a href="${pageContext.request.contextPath}/favoris"><i class="fa-solid fa-chevron-right"></i> Mes favoris</a></li>
                <% } else if ("PROPRIETAIRE".equalsIgnoreCase(roleFooter)) { %>
                    <li><a href="${pageContext.request.contextPath}/mes-maisons"><i class="fa-solid fa-chevron-right"></i> Mes annonces</a></li>
                    <li><a href="${pageContext.request.contextPath}/revenus"><i class="fa-solid fa-chevron-right"></i> Revenus</a></li>
                <% } %>
            </ul>
        </div>

    
        <div class="footer-colonne">
            <% if (estConnecteFooter && "LOCATAIRE".equalsIgnoreCase(roleFooter)) { %>
                <h4>Espace Locataire</h4>
                <ul class="footer-liens">
                    <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa-solid fa-chevron-right"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/mes-reservations"><i class="fa-solid fa-chevron-right"></i> Mes réservations</a></li>
                    <li><a href="${pageContext.request.contextPath}/favoris"><i class="fa-solid fa-chevron-right"></i> Mes favoris</a></li>
                    <li><a href="${pageContext.request.contextPath}/mes-paiements"><i class="fa-solid fa-chevron-right"></i> Mes paiements</a></li>
                    <li><a href="${pageContext.request.contextPath}/messages"><i class="fa-solid fa-chevron-right"></i> Messages</a></li>
                </ul>
            <% } else if (estConnecteFooter && "PROPRIETAIRE".equalsIgnoreCase(roleFooter)) { %>
                <h4>Espace Propriétaire</h4>
                <ul class="footer-liens">
                    <li><a href="${pageContext.request.contextPath}/dashboard"><i class="fa-solid fa-chevron-right"></i> Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/mes-maisons"><i class="fa-solid fa-chevron-right"></i> Mes annonces</a></li>
                    <li><a href="${pageContext.request.contextPath}/maison/ajouter"><i class="fa-solid fa-chevron-right"></i> Publier une annonce</a></li>
                    <li><a href="${pageContext.request.contextPath}/reservations-sur-mes-biens"><i class="fa-solid fa-chevron-right"></i> Réservations reçues</a></li>
                    <li><a href="${pageContext.request.contextPath}/revenus"><i class="fa-solid fa-chevron-right"></i> Revenus</a></li>
                    <li><a href="${pageContext.request.contextPath}/messages"><i class="fa-solid fa-chevron-right"></i> Messages</a></li>
                </ul>
            <% } else { %>
                <h4>Démarrer</h4>
                <ul class="footer-liens">
                    <li><a href="${pageContext.request.contextPath}/inscription"><i class="fa-solid fa-chevron-right"></i> Créer un compte</a></li>
                    <li><a href="${pageContext.request.contextPath}/connexion"><i class="fa-solid fa-chevron-right"></i> Se connecter</a></li>
                    <li><a href="${pageContext.request.contextPath}/inscription"><i class="fa-solid fa-chevron-right"></i> Devenir propriétaire</a></li>
                    <li><a href="${pageContext.request.contextPath}/recherche"><i class="fa-solid fa-chevron-right"></i> Explorer les maisons</a></li>
                </ul>
            <% } %>
        </div>

       
        <div class="footer-colonne">
            <h4>Contact</h4>
            <ul class="footer-contact">
                <li>
                    <i class="fa-solid fa-user"></i>
                    <span><strong>Baraka Ntwali</strong></span>
                </li>
                <li>
                    <i class="fa-solid fa-envelope"></i>
                    <a href="mailto:barakantwali06@gmail.com">barakantwali06@gmail.com</a>
                </li>
                <li>
                    <i class="fa-solid fa-phone"></i>
                    <a href="tel:+243984270438">+243 984 270 438</a>
                </li>
                <li>
                    <i class="fa-solid fa-location-dot"></i>
                    <span>Congo - Kinshasa</span>
                </li>
            </ul>
            <div class="footer-boutons">
                <a href="mailto:barakantwali06@gmail.com" class="footer-bouton footer-bouton-contact">
                    <i class="fa-solid fa-paper-plane"></i> Nous contacter
                </a>
                <a href="https://wa.me/243984270438" target="_blank" rel="noopener" class="footer-bouton footer-bouton-whatsapp">
                    <i class="fa-brands fa-whatsapp"></i> WhatsApp
                </a>
            </div>
        </div>

    </div>

  
    <div class="footer-barre">
        <div class="footer-container">
            <div class="footer-barre-contenu">
                <p>&copy; <%= anneeActuelle %> <strong>Location Maisons</strong>. Tous droits réservés. Développé par <strong>Baraka Ntwali</strong></p>
                <div class="footer-barre-liens">
                    <a href="#">Confidentialité</a>
                    <span class="separateur">|</span>
                    <a href="#">Conditions</a>
                    <span class="separateur">|</span>
                    <a href="#">Cookies</a>
                    <span class="separateur">|</span>
                    <a href="#">Mentions légales</a>
                </div>
            </div>
        </div>
    </div>
</footer>


<button id="boutonRetourHaut" class="bouton-retour-haut" title="Retour en haut" aria-label="Retour en haut">
    <i class="fa-solid fa-chevron-up"></i>
</button>


<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer-style.css">


<script src="${pageContext.request.contextPath}/assets/js/footer-script.js"></script>