/**
 * =============================================
 * SCRIPT DU FOOTER PROFESSIONNEL
 * Plateforme : Location Maisons
 * Auteur : Baraka Ntwali
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Bouton retour en haut (apparition/disparition fluide)
 * 2. Défilement fluide vers le haut avec easing
 * 3. Animation des liens du footer au survol
 * 4. Effet pulse sur les boutons de contact
 * 5. Validation visuelle des liens réseaux sociaux
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var boutonRetourHaut = document.getElementById('boutonRetourHaut');
    var liensFooter = document.querySelectorAll('.footer-liens a, .footer-contact a');
    var boutonsFooter = document.querySelectorAll('.footer-bouton');
    var liensReseaux = document.querySelectorAll('.reseau-lien');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserRetourHaut();
        initialiserAnimationsLiens();
        initialiserBoutonsContact();
        initialiserReseauxSociaux();
    }

    // ============================================
    // 3. BOUTON RETOUR EN HAUT
    // ============================================

    /**
     * Gère l'apparition, la disparition et le clic du bouton retour en haut.
     */
    function initialiserRetourHaut() {
        if (!boutonRetourHaut) return;

        // Variable pour éviter les appels trop fréquents (throttle)
        var scrollTimeout;

        window.addEventListener('scroll', function () {
            // Throttle : exécuter au maximum toutes les 100ms
            if (scrollTimeout) return;
            
            scrollTimeout = setTimeout(function () {
                scrollTimeout = null;

                if (window.scrollY > 500) {
                    boutonRetourHaut.classList.add('visible');
                } else {
                    boutonRetourHaut.classList.remove('visible');
                }
            }, 100);
        });

        // Remonter en haut avec animation fluide
        boutonRetourHaut.addEventListener('click', function () {
            // Effet de pulse avant de remonter
            boutonRetourHaut.style.transform = 'scale(0.9)';
            setTimeout(function () {
                boutonRetourHaut.style.transform = '';
            }, 150);

            // Défilement fluide personnalisé
            defilerVersHaut();
        });

        // Accessibilité : touche Entrée ou Espace
        boutonRetourHaut.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                defilerVersHaut();
            }
        });
    }

    /**
     * Défilement fluide vers le haut avec effet easing.
     */
    function defilerVersHaut() {
        var positionActuelle = window.pageYOffset;
        var duree = 600; // ms
        var debut = null;

        function animation(timestamp) {
            if (!debut) debut = timestamp;
            var progression = timestamp - debut;
            var pourcentage = Math.min(progression / duree, 1);

            // Easing : easeInOutCubic
            var easing = pourcentage < 0.5 
                ? 4 * pourcentage * pourcentage * pourcentage 
                : 1 - Math.pow(-2 * pourcentage + 2, 3) / 2;

            window.scrollTo(0, positionActuelle * (1 - easing));

            if (progression < duree) {
                requestAnimationFrame(animation);
            }
        }

        requestAnimationFrame(animation);
    }

    // ============================================
    // 4. ANIMATION DES LIENS
    // ============================================

    /**
     * Ajoute un effet visuel au survol des liens du footer.
     */
    function initialiserAnimationsLiens() {
        if (!liensFooter.length) return;

        liensFooter.forEach(function (lien) {
            lien.addEventListener('mouseenter', function () {
                var fleche = lien.querySelector('.fa-chevron-right');
                if (fleche) {
                    fleche.style.transform = 'translateX(4px)';
                    fleche.style.transition = 'transform 0.3s ease';
                }
            });

            lien.addEventListener('mouseleave', function () {
                var fleche = lien.querySelector('.fa-chevron-right');
                if (fleche) {
                    fleche.style.transform = 'translateX(0)';
                }
            });
        });
    }

    // ============================================
    // 5. BOUTONS DE CONTACT (EFFET PULSE)
    // ============================================

    /**
     * Ajoute un effet pulse subtil sur les boutons WhatsApp et Contact.
     */
    function initialiserBoutonsContact() {
        if (!boutonsFooter.length) return;

        boutonsFooter.forEach(function (bouton) {
            bouton.addEventListener('click', function () {
                // Effet de clic
                bouton.style.transform = 'scale(0.96)';
                bouton.style.transition = 'transform 0.15s ease';
                
                setTimeout(function () {
                    bouton.style.transform = 'scale(1)';
                }, 150);
            });

            // Effet de survol supplémentaire
            bouton.addEventListener('mouseenter', function () {
                bouton.style.transform = 'translateY(-2px)';
                bouton.style.transition = 'all 0.3s ease';
            });

            bouton.addEventListener('mouseleave', function () {
                bouton.style.transform = 'translateY(0)';
            });
        });
    }

    // ============================================
    // 6. RÉSEAUX SOCIAUX
    // ============================================

    /**
     * Ajoute une confirmation visuelle au clic sur les réseaux sociaux.
     */
    function initialiserReseauxSociaux() {
        if (!liensReseaux.length) return;

        liensReseaux.forEach(function (lien) {
            lien.addEventListener('click', function () {
                // Effet de pulse
                lien.style.transform = 'scale(0.85)';
                lien.style.transition = 'transform 0.2s ease';
                
                setTimeout(function () {
                    lien.style.transform = 'translateY(-3px)';
                }, 200);
            });

            // Animation continue subtile (optionnelle)
            lien.addEventListener('mouseenter', function () {
                lien.style.transform = 'translateY(-3px)';
                lien.style.boxShadow = '0 4px 12px rgba(59,130,246,0.3)';
            });

            lien.addEventListener('mouseleave', function () {
                lien.style.transform = 'translateY(0)';
                lien.style.boxShadow = 'none';
            });
        });
    }

    // ============================================
    // 7. DÉMARRAGE
    // ============================================
    initialiser();

})();