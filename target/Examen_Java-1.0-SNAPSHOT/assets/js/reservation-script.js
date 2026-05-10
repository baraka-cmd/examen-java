/**
 * =============================================
 * SCRIPT DE LA PAGE DE RÉSERVATION
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Animation du bouton de confirmation
 * 2. Protection contre la double soumission
 * 3. Animation de la carte de prix au scroll
 * 4. Animation des icônes de confirmation
 * 5. Gestion du bouton retour
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var boutonConfirmer = document.querySelector('.bouton-confirmer-reservation');
    var formulaireConfirmation = document.querySelector('.formulaire-confirmation');
    var cartePrix = document.querySelector('.carte-prix');
    var confirmationIcone = document.querySelector('.confirmation-icone');
    var etapesConfirmation = document.querySelectorAll('.etape');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserBoutonConfirmation();
        initialiserAnimationPrix();
        initialiserAnimationConfirmation();
        initialiserHistoriqueNavigation();
    }

    // ============================================
    // 3. BOUTON DE CONFIRMATION
    // ============================================

    /**
     * Gère le clic sur le bouton "Confirmer la réservation".
     * Protège contre la double soumission.
     */
    function initialiserBoutonConfirmation() {
        if (!boutonConfirmer || !formulaireConfirmation) return;

        formulaireConfirmation.addEventListener('submit', function (e) {
            // Désactiver le bouton immédiatement
            boutonConfirmer.disabled = true;
            boutonConfirmer.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Réservation en cours...';
            boutonConfirmer.style.opacity = '0.8';
            boutonConfirmer.style.cursor = 'not-allowed';

            // Effet de pulse sur la carte prix
            if (cartePrix) {
                cartePrix.style.transform = 'scale(0.98)';
                cartePrix.style.transition = 'transform 0.2s ease';
                setTimeout(function () {
                    cartePrix.style.transform = 'scale(1)';
                }, 300);
            }
        });
    }

    // ============================================
    // 4. ANIMATION CARTE PRIX AU SCROLL
    // ============================================

    /**
     * Ajoute un effet visuel à la carte de prix quand elle devient visible.
     */
    function initialiserAnimationPrix() {
        if (!cartePrix) return;

        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (entry.isIntersecting) {
                        cartePrix.style.opacity = '1';
                        cartePrix.style.transform = 'translateY(0)';
                        observer.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.3 });

            cartePrix.style.opacity = '0';
            cartePrix.style.transform = 'translateY(20px)';
            cartePrix.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            observer.observe(cartePrix);
        } else {
            cartePrix.style.opacity = '1';
        }
    }

    // ============================================
    // 5. ANIMATION PAGE DE CONFIRMATION
    // ============================================

    /**
     * Anime les éléments de la page de confirmation.
     */
    function initialiserAnimationConfirmation() {
        // Animation de l'icône de confirmation
        if (confirmationIcone) {
            confirmationIcone.style.opacity = '0';
            confirmationIcone.style.transform = 'scale(0.5)';
            confirmationIcone.style.transition = 'opacity 0.5s ease, transform 0.5s ease';

            setTimeout(function () {
                confirmationIcone.style.opacity = '1';
                confirmationIcone.style.transform = 'scale(1)';
            }, 200);
        }

        // Animation des étapes
        if (etapesConfirmation.length) {
            etapesConfirmation.forEach(function (etape, index) {
                etape.style.opacity = '0';
                etape.style.transform = 'translateY(10px)';
                etape.style.transition = 'opacity 0.4s ease, transform 0.4s ease';

                setTimeout(function () {
                    etape.style.opacity = '1';
                    etape.style.transform = 'translateY(0)';
                }, 400 + (index * 200));
            });
        }

        // Animation des lignes entre les étapes
        var lignesEtapes = document.querySelectorAll('.etape-ligne');
        if (lignesEtapes.length) {
            lignesEtapes.forEach(function (ligne, index) {
                ligne.style.transform = 'scaleX(0)';
                ligne.style.transition = 'transform 0.4s ease';
                ligne.style.transformOrigin = 'left';

                setTimeout(function () {
                    ligne.style.transform = 'scaleX(1)';
                }, 600 + (index * 200));
            });
        }
    }

    // ============================================
    // 6. GESTION NAVIGATION (BOUTON RETOUR)
    // ============================================

    /**
     * Avertit l'utilisateur s'il quitte la page pendant une réservation.
     */
    function initialiserHistoriqueNavigation() {
        // Seulement sur la page de récapitulatif (pas la confirmation)
        if (!formulaireConfirmation) return;

        var reservationEnCours = true;

        // Avertir avant de quitter la page
        window.addEventListener('beforeunload', function (e) {
            if (reservationEnCours && !boutonConfirmer.disabled) {
                var message = 'Vous avez une réservation en cours. Voulez-vous vraiment quitter ?';
                e.returnValue = message;
                return message;
            }
        });

        // Désactiver l'avertissement après soumission
        if (boutonConfirmer) {
            boutonConfirmer.addEventListener('click', function () {
                reservationEnCours = false;
            });
        }

        // Gérer les liens de la page
        var tousLesLiens = document.querySelectorAll('a');
        tousLesLiens.forEach(function (lien) {
            lien.addEventListener('click', function (e) {
                var href = lien.getAttribute('href');
                // Ne pas avertir pour les liens internes ou #
                if (href && href !== '#' && !href.startsWith('javascript')) {
                    reservationEnCours = false;
                }
            });
        });
    }

    // ============================================
    // 7. DÉMARRAGE
    // ============================================
    initialiser();

})();