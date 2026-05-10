/**
 * =============================================
 * SCRIPT DES RÉSERVATIONS PROPRIÉTAIRE
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Animation des statistiques au clic
 * 2. Confirmation visuelle avant action
 * 3. Animation des cartes au survol
 * 4. Filtrage visuel par statut
 * 5. Bouton "Message" avec popup
 * 6. Survol des boutons d'action
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var cartesReservation = document.querySelectorAll('.carte-reservation-proprio');
    var boutonsConfirmer = document.querySelectorAll('.bouton-confirmer');
    var boutonsAnnuler = document.querySelectorAll('.bouton-annuler');
    var boutonsMessage = document.querySelectorAll('.bouton-message');
    var statsItems = document.querySelectorAll('.stat-item');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserSurvolCartes();
        initialiserBoutonsConfirmation();
        initialiserBoutonsAnnulation();
        initialiserBoutonsMessage();
        initialiserStatsActives();
        initialiserAnimationsApparition();
        injecterStyles();
    }

    // ============================================
    // 3. SURVOL DES CARTES
    // ============================================

    /**
     * Effet visuel au survol des cartes de réservation.
     */
    function initialiserSurvolCartes() {
        if (!cartesReservation.length) return;

        cartesReservation.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                carte.style.transform = 'translateY(-2px)';
                carte.style.transition = 'all 0.3s ease';
            });

            carte.addEventListener('mouseleave', function () {
                carte.style.transform = 'translateY(0)';
            });
        });
    }

    // ============================================
    // 4. BOUTONS CONFIRMATION (EFFET VISUEL)
    // ============================================

    /**
     * Ajoute un effet de chargement sur les boutons Confirmer.
     */
    function initialiserBoutonsConfirmation() {
        if (!boutonsConfirmer.length) return;

        boutonsConfirmer.forEach(function (bouton) {
            bouton.addEventListener('click', function (e) {
                // La confirmation via confirm() est gérée par l'attribut onclick dans le HTML
                // Si l'utilisateur confirme, on ajoute un effet visuel
                var href = bouton.getAttribute('href');
                if (!href) return;

                // Intercepter le clic pour l'effet visuel
                e.preventDefault();

                if (confirm('Confirmer cette réservation ? Le locataire sera notifié.')) {
                    // Effet de chargement
                    var originalHTML = bouton.innerHTML;
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Confirmation...';
                    bouton.style.pointerEvents = 'none';
                    bouton.style.opacity = '0.7';

                    // Rediriger après un court délai
                    setTimeout(function () {
                        window.location.href = href;
                    }, 400);
                }
            });
        });
    }

    // ============================================
    // 5. BOUTONS ANNULATION (EFFET VISUEL)
    // ============================================

    /**
     * Ajoute un effet de chargement sur les boutons Annuler.
     */
    function initialiserBoutonsAnnulation() {
        if (!boutonsAnnuler.length) return;

        boutonsAnnuler.forEach(function (bouton) {
            bouton.addEventListener('click', function (e) {
                var href = bouton.getAttribute('href');
                if (!href) return;

                e.preventDefault();

                if (confirm('Voulez-vous vraiment annuler cette réservation ? Cette action est irréversible.')) {
                    var originalHTML = bouton.innerHTML;
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Annulation...';
                    bouton.style.pointerEvents = 'none';
                    bouton.style.opacity = '0.7';

                    setTimeout(function () {
                        window.location.href = href;
                    }, 400);
                }
            });
        });
    }

    // ============================================
    // 6. BOUTONS MESSAGE (POPUP)
    // ============================================

    /**
     * Gère l'affichage du message du locataire dans une popup.
     * Remplace le alert() basique par une modale légère.
     */
    function initialiserBoutonsMessage() {
        if (!boutonsMessage.length) return;

        boutonsMessage.forEach(function (bouton) {
            bouton.addEventListener('click', function () {
                var message = bouton.getAttribute('title');
                if (!message) return;

                afficherModaleMessage(message);
            });
        });
    }

    /**
     * Affiche une modale avec le message du locataire.
     * @param {string} message - Le message à afficher
     */
    function afficherModaleMessage(message) {
        // Supprimer une modale existante
        var modaleExistante = document.querySelector('.modale-message');
        if (modaleExistante) {
            modaleExistante.remove();
        }

        // Créer l'overlay
        var overlay = document.createElement('div');
        overlay.className = 'modale-message-overlay';

        // Créer la modale
        var modale = document.createElement('div');
        modale.className = 'modale-message';

        // En-tête
        var entete = document.createElement('div');
        entete.className = 'modale-entete';
        entete.innerHTML = '<h3><i class="fa-solid fa-message"></i> Message du locataire</h3>';

        var boutonFermer = document.createElement('button');
        boutonFermer.className = 'modale-fermer';
        boutonFermer.innerHTML = '<i class="fa-solid fa-xmark"></i>';
        boutonFermer.onclick = fermerModale;
        entete.appendChild(boutonFermer);

        // Corps
        var corps = document.createElement('div');
        corps.className = 'modale-corps';
        corps.innerHTML = '<p>' + message.replace(/\n/g, '<br>') + '</p>';

        // Pied
        var pied = document.createElement('div');
        pied.className = 'modale-pied';
        pied.innerHTML = '<button class="bouton-modale-fermer">Fermer</button>';
        pied.querySelector('.bouton-modale-fermer').onclick = fermerModale;

        // Assembler
        modale.appendChild(entete);
        modale.appendChild(corps);
        modale.appendChild(pied);
        overlay.appendChild(modale);
        document.body.appendChild(overlay);

        // Animation d'entrée
        setTimeout(function () {
            overlay.style.opacity = '1';
            modale.style.transform = 'translateY(0)';
            modale.style.opacity = '1';
        }, 10);

        // Fermer avec Échap
        document.addEventListener('keydown', function escHandler(e) {
            if (e.key === 'Escape') {
                fermerModale();
                document.removeEventListener('keydown', escHandler);
            }
        });

        // Fermer en cliquant sur l'overlay
        overlay.addEventListener('click', function (e) {
            if (e.target === overlay) {
                fermerModale();
            }
        });
    }

    /**
     * Ferme la modale de message.
     */
    function fermerModale() {
        var overlay = document.querySelector('.modale-message-overlay');
        if (overlay) {
            overlay.style.opacity = '0';
            var modale = overlay.querySelector('.modale-message');
            if (modale) {
                modale.style.transform = 'translateY(20px)';
                modale.style.opacity = '0';
            }
            setTimeout(function () {
                if (overlay.parentNode) {
                    overlay.parentNode.removeChild(overlay);
                }
            }, 300);
        }
    }

    // ============================================
    // 7. STATISTIQUES ACTIVES
    // ============================================

    /**
     * Met en évidence l'onglet statistique actif.
     */
    function initialiserStatsActives() {
        if (!statsItems.length) return;

        statsItems.forEach(function (item) {
            item.addEventListener('click', function () {
                // Effet de clic
                item.style.transform = 'scale(0.95)';
                setTimeout(function () {
                    item.style.transform = '';
                }, 150);
            });
        });
    }

    // ============================================
    // 8. ANIMATIONS D'APPARITION
    // ============================================

    /**
     * Les cartes apparaissent avec un léger fondu au chargement.
     */
    function initialiserAnimationsApparition() {
        if (!cartesReservation.length) return;

        // Effet staggered : chaque carte apparaît avec un délai
        cartesReservation.forEach(function (carte, index) {
            carte.style.opacity = '0';
            carte.style.transform = 'translateY(20px)';
            carte.style.transition = 'opacity 0.4s ease, transform 0.4s ease';

            setTimeout(function () {
                carte.style.opacity = '1';
                carte.style.transform = 'translateY(0)';
            }, 60 * (index + 1));
        });
    }

    // ============================================
    // 9. STYLES INJECTÉS
    // ============================================

    /**
     * Injecte les styles de la modale dans le DOM.
     */
    function injecterStyles() {
        if (document.getElementById('styles-reservations-proprio')) return;

        var style = document.createElement('style');
        style.id = 'styles-reservations-proprio';
        style.textContent =
            /* Modale message */
            '.modale-message-overlay {' +
            '  position: fixed; top: 0; left: 0; width: 100%; height: 100%;' +
            '  background: rgba(0,0,0,0.5); display: flex; align-items: center;' +
            '  justify-content: center; z-index: 9999; opacity: 0;' +
            '  transition: opacity 0.3s ease; padding: 20px;' +
            '}' +
            '.modale-message {' +
            '  background: #ffffff; border-radius: 16px; width: 100%; max-width: 520px;' +
            '  box-shadow: 0 20px 40px rgba(0,0,0,0.2);' +
            '  transform: translateY(20px); opacity: 0;' +
            '  transition: all 0.3s ease; overflow: hidden;' +
            '}' +
            '.modale-entete {' +
            '  padding: 20px 24px; border-bottom: 1px solid #e2e8f0;' +
            '  display: flex; align-items: center; justify-content: space-between;' +
            '}' +
            '.modale-entete h3 {' +
            '  font-size: 17px; font-weight: 600; color: #0f172a;' +
            '  display: flex; align-items: center; gap: 8px; margin: 0;' +
            '}' +
            '.modale-entete h3 i { color: #3b82f6; }' +
            '.modale-fermer {' +
            '  width: 36px; height: 36px; border-radius: 50%; border: none;' +
            '  background: #f1f5f9; color: #64748b; cursor: pointer;' +
            '  display: flex; align-items: center; justify-content: center;' +
            '  font-size: 16px; transition: all 0.3s ease;' +
            '}' +
            '.modale-fermer:hover { background: #e2e8f0; color: #0f172a; }' +
            '.modale-corps {' +
            '  padding: 24px;' +
            '}' +
            '.modale-corps p {' +
            '  font-size: 15px; color: #334155; line-height: 1.7; margin: 0;' +
            '  white-space: pre-wrap;' +
            '}' +
            '.modale-pied {' +
            '  padding: 16px 24px; border-top: 1px solid #e2e8f0;' +
            '  display: flex; justify-content: flex-end;' +
            '}' +
            '.bouton-modale-fermer {' +
            '  padding: 10px 22px; background: #3b82f6; color: #ffffff;' +
            '  border: none; border-radius: 8px; font-size: 14px; font-weight: 600;' +
            '  cursor: pointer; transition: all 0.3s ease; font-family: inherit;' +
            '}' +
            '.bouton-modale-fermer:hover { background: #2563eb; }' +
            /* Animation keyframes */
            '@keyframes pulse {' +
            '  0%, 100% { transform: scale(1); }' +
            '  50% { transform: scale(1.05); }' +
            '}';
        document.head.appendChild(style);
    }

    // ============================================
    // 10. DÉMARRAGE
    // ============================================
    initialiser();

})();