/**
 * =============================================
 * SCRIPT DE LA MESSAGERIE
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Scroll automatique vers le bas du fil de discussion
 * 2. Envoi du message avec Entrée (Shift+Entrée pour nouvelle ligne)
 * 3. Auto-redimensionnement du textarea
 * 4. Animation des nouvelles bulles
 * 5. Survol des conversations
 * 6. Adaptation du textarea à la largeur
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var filDiscussion = document.getElementById('filDiscussion');
    var textareaMessage = document.getElementById('contenu');
    var formulaireEnvoi = document.querySelector('.formulaire-envoi form');
    var boutonEnvoi = document.querySelector('.bouton-envoi');
    var conversationItems = document.querySelectorAll('.conversation-item');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserScrollAuto();
        initialiserEnvoiClavier();
        initialiserAutoRedimensionnement();
        initialiserSurvolConversations();
        initialiserFocusTextarea();
    }

    // ============================================
    // 3. SCROLL AUTO VERS LE BAS
    // ============================================

    /**
     * Fait défiler le fil de discussion vers le bas au chargement.
     */
    function initialiserScrollAuto() {
        if (!filDiscussion) return;

        // Scroll immédiat vers le bas
        filDiscussion.scrollTop = filDiscussion.scrollHeight;

        // Scroll après un court délai (au cas où les images chargent)
        setTimeout(function () {
            filDiscussion.scrollTop = filDiscussion.scrollHeight;
        }, 300);

        // Scroll fluide
        filDiscussion.style.scrollBehavior = 'smooth';
    }

    // ============================================
    // 4. ENVOI AVEC ENTRÉE
    // ============================================

    /**
     * Permet d'envoyer le message avec la touche Entrée.
     * Shift+Entrée pour faire un saut de ligne.
     */
    function initialiserEnvoiClavier() {
        if (!textareaMessage || !formulaireEnvoi) return;

        textareaMessage.addEventListener('keydown', function (e) {
            // Entrée sans Shift = envoyer
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();

                var contenu = textareaMessage.value.trim();
                if (contenu.length > 0) {
                    // Désactiver le bouton pour éviter double envoi
                    desactiverEnvoi();
                    formulaireEnvoi.submit();
                }
            }
            // Shift+Entrée = nouvelle ligne (comportement normal)
        });
    }

    /**
     * Désactive le bouton d'envoi et montre un spinner.
     */
    function desactiverEnvoi() {
        if (!boutonEnvoi) return;

        boutonEnvoi.disabled = true;
        boutonEnvoi.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i>';
        boutonEnvoi.style.opacity = '0.7';
        boutonEnvoi.style.cursor = 'not-allowed';
    }

    // ============================================
    // 5. AUTO-REDIMENSIONNEMENT DU TEXTAREA
    // ============================================

    /**
     * Ajuste automatiquement la hauteur du textarea selon le contenu.
     */
    function initialiserAutoRedimensionnement() {
        if (!textareaMessage) return;

        textareaMessage.addEventListener('input', function () {
            // Réinitialiser la hauteur pour calculer correctement
            this.style.height = 'auto';

            // Ajuster à la hauteur du contenu (max 120px défini en CSS)
            var nouvelleHauteur = Math.min(this.scrollHeight, 120);
            this.style.height = nouvelleHauteur + 'px';

            // Ajuster le padding si hauteur plus grande
            if (nouvelleHauteur > 50) {
                this.style.padding = '14px 18px';
            }
        });

        // Réinitialiser après envoi (quand la page se recharge)
        textareaMessage.style.height = 'auto';
    }

    // ============================================
    // 6. SURVOL CONVERSATIONS
    // ============================================

    /**
     * Effet visuel au survol des conversations.
     */
    function initialiserSurvolConversations() {
        if (!conversationItems.length) return;

        conversationItems.forEach(function (item) {
            item.addEventListener('mouseenter', function () {
                var fleche = item.querySelector('.conversation-fleche i');
                if (fleche) {
                    fleche.style.transform = 'translateX(3px)';
                    fleche.style.transition = 'transform 0.3s ease';
                }
            });

            item.addEventListener('mouseleave', function () {
                var fleche = item.querySelector('.conversation-fleche i');
                if (fleche) {
                    fleche.style.transform = 'translateX(0)';
                }
            });

            // Rendre toute la carte cliquable (au cas où le href ne fonctionne pas)
            item.addEventListener('click', function (e) {
                // Ne pas interférer avec d'autres clics
                if (e.target.tagName === 'A') return;
                var href = item.getAttribute('href');
                if (href) {
                    window.location.href = href;
                }
            });
        });
    }

    // ============================================
    // 7. FOCUS AUTOMATIQUE SUR LE TEXTAREA
    // ============================================

    /**
     * Place le curseur dans le textarea au chargement de la page conversation.
     */
    function initialiserFocusTextarea() {
        if (!textareaMessage) return;

        // Focus avec un léger délai pour que la page soit chargée
        setTimeout(function () {
            textareaMessage.focus();
        }, 500);

        // Placer le curseur à la fin du texte s'il y en a
        var longueur = textareaMessage.value.length;
        if (longueur > 0) {
            textareaMessage.setSelectionRange(longueur, longueur);
        }
    }

    // ============================================
    // 8. DÉMARRAGE
    // ============================================
    initialiser();

})();