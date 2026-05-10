/**
 * =============================================
 * SCRIPT DE LA PAGE MES RÉSERVATIONS
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Confirmation avant annulation
 * 2. Animation des cartes au survol
 * 3. Apparition animée des cartes
 * 4. Gestion des filtres actifs
 * 5. Survol des boutons d'action
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var cartesReservation = document.querySelectorAll('.carte-reservation-locataire');
    var boutonsAnnuler = document.querySelectorAll('.bouton-annuler-locataire');
    var boutonsAvis = document.querySelectorAll('.bouton-avis');
    var statItems = document.querySelectorAll('.stat-item');
    var messageSucces = document.querySelector('.message-succes');
    var messageErreur = document.querySelector('.message-erreur');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserAnimationCartes();
        initialiserSurvolCartes();
        initialiserBoutonsAnnulation();
        initialiserBoutonsAvis();
        initialiserFiltresActifs();
        initialiserEffacementMessages();
    }

    // ============================================
    // 3. ANIMATION APPARITION CARTES
    // ============================================

    /**
     * Les cartes apparaissent avec un effet staggered.
     */
    function initialiserAnimationCartes() {
        if (!cartesReservation.length) return;

        cartesReservation.forEach(function (carte, index) {
            carte.style.opacity = '0';
            carte.style.transform = 'translateY(30px)';
            carte.style.transition = 'opacity 0.4s ease, transform 0.4s ease';

            setTimeout(function () {
                carte.style.opacity = '1';
                carte.style.transform = 'translateY(0)';
            }, 80 * (index + 1));
        });
    }

    // ============================================
    // 4. SURVOL CARTES
    // ============================================

    /**
     * Effet visuel au survol des cartes.
     */
    function initialiserSurvolCartes() {
        if (!cartesReservation.length) return;

        cartesReservation.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                var img = carte.querySelector('.reservation-photo img');
                if (img) {
                    img.style.transform = 'scale(1.05)';
                    img.style.transition = 'transform 0.4s ease';
                }
            });

            carte.addEventListener('mouseleave', function () {
                var img = carte.querySelector('.reservation-photo img');
                if (img) {
                    img.style.transform = 'scale(1)';
                }
            });
        });
    }

    // ============================================
    // 5. BOUTONS ANNULATION
    // ============================================

    /**
     * Gère la confirmation avant annulation avec effet visuel.
     */
    function initialiserBoutonsAnnulation() {
        if (!boutonsAnnuler.length) return;

        boutonsAnnuler.forEach(function (bouton) {
            bouton.addEventListener('click', function (e) {
                var href = bouton.getAttribute('href');
                if (!href) return;

                e.preventDefault();

                if (confirm('Voulez-vous vraiment annuler cette réservation ?\n\nCette action est irréversible.')) {
                    // Effet de chargement
                    var texteOriginal = bouton.innerHTML;
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Annulation...';
                    bouton.style.pointerEvents = 'none';
                    bouton.style.opacity = '0.7';

                    // Rediriger après un court délai
                    setTimeout(function () {
                        window.location.href = href;
                    }, 500);
                }
            });
        });
    }

    // ============================================
    // 6. BOUTONS AVIS
    // ============================================

    /**
     * Effet visuel sur les boutons "Donner mon avis".
     */
    function initialiserBoutonsAvis() {
        if (!boutonsAvis.length) return;

        boutonsAvis.forEach(function (bouton) {
            bouton.addEventListener('click', function () {
                var texteOriginal = bouton.innerHTML;
                bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Chargement...';
                bouton.style.pointerEvents = 'none';
                bouton.style.opacity = '0.7';

                // Restaurer après 1.5s au cas où la redirection échoue
                setTimeout(function () {
                    bouton.innerHTML = texteOriginal;
                    bouton.style.pointerEvents = 'auto';
                    bouton.style.opacity = '1';
                }, 1500);
            });
        });
    }

    // ============================================
    // 7. FILTRES ACTIFS
    // ============================================

    /**
     * Effet visuel au clic sur les filtres.
     */
    function initialiserFiltresActifs() {
        if (!statItems.length) return;

        statItems.forEach(function (item) {
            item.addEventListener('click', function () {
                // Effet de clic
                item.style.transform = 'scale(0.95)';
                setTimeout(function () {
                    item.style.transform = '';
                }, 150);

                // Si le filtre est déjà actif, on empêche la redirection
                if (item.classList.contains('stat-actif')) {
                    // Déjà sur ce filtre, on laisse le comportement normal
                    // ou on pourrait rediriger vers "toutes"
                }
            });
        });
    }

    // ============================================
    // 8. EFFACEMENT MESSAGES
    // ============================================

    /**
     * Cache les messages après quelques secondes.
     */
    function initialiserEffacementMessages() {
        if (messageSucces) {
            setTimeout(function () {
                messageSucces.style.transition = 'opacity 0.5s ease';
                messageSucces.style.opacity = '0';
                setTimeout(function () {
                    if (messageSucces.parentNode) {
                        messageSucces.style.display = 'none';
                    }
                }, 500);
            }, 5000);
        }

        if (messageErreur) {
            setTimeout(function () {
                messageErreur.style.transition = 'opacity 0.5s ease';
                messageErreur.style.opacity = '0';
                setTimeout(function () {
                    if (messageErreur.parentNode) {
                        messageErreur.style.display = 'none';
                    }
                }, 500);
            }, 5000);
        }
    }

    // ============================================
    // 9. DÉMARRAGE
    // ============================================
    initialiser();

})();