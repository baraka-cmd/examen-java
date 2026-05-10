/**
 * =============================================
 * SCRIPT DE LA PAGE DES FAVORIS
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Confirmation avant retrait d'un favori
 * 2. Animation de suppression d'une carte
 * 3. Apparition animée des cartes
 * 4. Survol des cartes avec zoom image
 * 5. Mise à jour du compteur après suppression
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var cartesFavoris = document.querySelectorAll('.carte-favori');
    var boutonsRetirer = document.querySelectorAll('.bouton-retirer-favori');
    var grilleFavoris = document.querySelector('.favoris-grille');
    var compteurFavoris = document.querySelector('.entete-gauche p strong');
    var messageSucces = document.querySelector('.message-succes');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserAnimationCartes();
        initialiserSurvolCartes();
        initialiserBoutonsRetirer();
        initialiserEffacementMessage();
    }

    // ============================================
    // 3. ANIMATION APPARITION CARTES
    // ============================================

    /**
     * Les cartes apparaissent avec un effet staggered.
     */
    function initialiserAnimationCartes() {
        if (!cartesFavoris.length) return;

        cartesFavoris.forEach(function (carte, index) {
            carte.style.opacity = '0';
            carte.style.transform = 'translateY(30px)';
            carte.style.transition = 'opacity 0.4s ease, transform 0.4s ease';

            setTimeout(function () {
                carte.style.opacity = '1';
                carte.style.transform = 'translateY(0)';
            }, 70 * (index + 1));
        });
    }

    // ============================================
    // 4. SURVOL CARTES
    // ============================================

    /**
     * Effet de zoom sur l'image au survol de la carte.
     */
    function initialiserSurvolCartes() {
        if (!cartesFavoris.length) return;

        cartesFavoris.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                var img = carte.querySelector('.carte-favori-image img');
                if (img) {
                    img.style.transform = 'scale(1.08)';
                    img.style.transition = 'transform 0.5s ease';
                }

                var bouton = carte.querySelector('.bouton-retirer-favori');
                if (bouton) {
                    bouton.style.opacity = '1';
                }
            });

            carte.addEventListener('mouseleave', function () {
                var img = carte.querySelector('.carte-favori-image img');
                if (img) {
                    img.style.transform = 'scale(1)';
                }

                var bouton = carte.querySelector('.bouton-retirer-favori');
                if (bouton) {
                    bouton.style.opacity = '0.7';
                }
            });
        });
    }

    // ============================================
    // 5. BOUTONS RETIRER
    // ============================================

    /**
     * Gère le retrait d'un favori avec animation.
     */
    function initialiserBoutonsRetirer() {
        if (!boutonsRetirer.length) return;

        // Initialiser l'opacité des boutons
        boutonsRetirer.forEach(function (bouton) {
            bouton.style.opacity = '0.7';
            bouton.style.transition = 'opacity 0.3s ease';
        });

        boutonsRetirer.forEach(function (bouton) {
            bouton.addEventListener('click', function (e) {
                e.preventDefault();

                var href = bouton.getAttribute('href');
                if (!href) return;

                if (confirm('Retirer cette maison de vos favoris ?')) {
                    // Trouver la carte parente
                    var carte = bouton.closest('.carte-favori');
                    
                    // Animation de sortie
                    if (carte) {
                        carte.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
                        carte.style.opacity = '0';
                        carte.style.transform = 'scale(0.9)';

                        // Mettre à jour le compteur visuellement
                        if (compteurFavoris) {
                            var nombreActuel = parseInt(compteurFavoris.textContent, 10);
                            if (!isNaN(nombreActuel) && nombreActuel > 0) {
                                compteurFavoris.textContent = nombreActuel - 1;
                                // Mettre à jour le pluriel
                                var texteComplet = compteurFavoris.parentElement;
                                if (texteComplet) {
                                    var nouveauNombre = nombreActuel - 1;
                                    var motMaison = nouveauNombre <= 1 ? 'maison sauvegardée' : 'maisons sauvegardées';
                                    texteComplet.innerHTML = '<strong>' + nouveauNombre + '</strong> ' + motMaison;
                                }
                            }
                        }
                    }

                    // Rediriger après l'animation
                    setTimeout(function () {
                        window.location.href = href;
                    }, 300);
                }
            });
        });
    }

    // ============================================
    // 6. EFFACEMENT MESSAGE SUCCÈS
    // ============================================

    /**
     * Cache le message de succès après quelques secondes.
     */
    function initialiserEffacementMessage() {
        if (!messageSucces) return;

        setTimeout(function () {
            messageSucces.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            messageSucces.style.opacity = '0';
            messageSucces.style.transform = 'translateY(-10px)';

            setTimeout(function () {
                if (messageSucces.parentNode) {
                    messageSucces.style.display = 'none';
                }
            }, 500);
        }, 4000);
    }

    // ============================================
    // 7. DÉMARRAGE
    // ============================================
    initialiser();

})();