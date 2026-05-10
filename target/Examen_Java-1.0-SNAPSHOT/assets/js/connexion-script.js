/**
 * =============================================
 * SCRIPT DE LA PAGE DE CONNEXION
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * - Validation email en temps réel
 * - Validation mot de passe (non vide)
 * - Bouton œil pour afficher/masquer le mot de passe
 * - Blocage double soumission du formulaire
 * - Effacement automatique des messages d'erreur
 * - Focus automatique sur le champ email
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS DU DOM
    // ============================================

    const formulaire = document.querySelector('.formulaire-connexion');
    const champEmail = document.getElementById('email');
    const champMotDePasse = document.getElementById('motDePasse');
    const boutonVisibilite = document.querySelector('.bouton-visibilite');
    const messageErreur = document.querySelector('.message-erreur');
    const messageSucces = document.querySelector('.message-succes');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    /**
     * Point d'entrée : démarre tous les écouteurs.
     */
    function initialiser() {
        if (!formulaire) return;

        initialiserValidationTempsReel();
        initialiserVisibiliteMotDePasse();
        initialiserSoumissionFormulaire();
        initialiserEffacementMessages();
    }

    // ============================================
    // 3. VALIDATION EN TEMPS RÉEL
    // ============================================

    /**
     * Ajoute la validation sur chaque champ au moment où l'utilisateur le quitte.
     */
    function initialiserValidationTempsReel() {
        if (champEmail) {
            champEmail.addEventListener('blur', validerEmail);
            champEmail.addEventListener('input', function () {
                if (champEmail.classList.contains('champ-invalide')) {
                    validerEmail();
                }
            });
        }

        if (champMotDePasse) {
            champMotDePasse.addEventListener('blur', validerMotDePasse);
            champMotDePasse.addEventListener('input', function () {
                if (champMotDePasse.classList.contains('champ-invalide')) {
                    validerMotDePasse();
                }
            });
        }
    }

    /**
     * Valide le champ email.
     * @returns {boolean} true si valide, false sinon
     */
    function validerEmail() {
        nettoyerEtatChamp(champEmail);

        const valeur = champEmail.value.trim();

        if (!valeur) {
            afficherErreurChamp(champEmail, 'Veuillez saisir votre adresse email.');
            return false;
        }

        const regexEmail = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
        if (!regexEmail.test(valeur)) {
            afficherErreurChamp(champEmail, 'Format d\'email invalide. Exemple : nom@domaine.com');
            return false;
        }

        return true;
    }

    /**
     * Valide le champ mot de passe.
     * @returns {boolean} true si valide, false sinon
     */
    function validerMotDePasse() {
        nettoyerEtatChamp(champMotDePasse);

        if (!champMotDePasse.value) {
            afficherErreurChamp(champMotDePasse, 'Veuillez saisir votre mot de passe.');
            return false;
        }

        return true;
    }

    /**
     * Supprime les classes d'erreur et les messages associés à un champ.
     * @param {HTMLElement} champ - Le champ à nettoyer
     */
    function nettoyerEtatChamp(champ) {
        champ.classList.remove('champ-invalide');
        const groupeParent = champ.closest('.groupe-champ');
        if (groupeParent) {
            const ancienMessage = groupeParent.querySelector('.message-champ-erreur');
            if (ancienMessage) ancienMessage.remove();
        }
    }

    /**
     * Affiche un message d'erreur sous un champ.
     * @param {HTMLElement} champ - Le champ concerné
     * @param {string} message - Le message à afficher
     */
    function afficherErreurChamp(champ, message) {
        champ.classList.add('champ-invalide');

        const groupeParent = champ.closest('.groupe-champ');
        if (!groupeParent) return;

        // Éviter les doublons
        const ancienMessage = groupeParent.querySelector('.message-champ-erreur');
        if (ancienMessage) ancienMessage.remove();

        const messageErreurChamp = document.createElement('span');
        messageErreurChamp.className = 'message-champ-erreur';
        messageErreurChamp.textContent = message;
        groupeParent.appendChild(messageErreurChamp);
    }

    // ============================================
    // 4. VISIBILITÉ DU MOT DE PASSE
    // ============================================

    /**
     * Gère le bouton œil pour afficher/masquer le mot de passe.
     */
    function initialiserVisibiliteMotDePasse() {
        if (!boutonVisibilite) return;

        boutonVisibilite.addEventListener('click', function (e) {
            e.preventDefault();
            basculerVisibilite();
        });

        // Accessibilité : touche Entrée ou Espace sur le bouton
        boutonVisibilite.addEventListener('keydown', function (e) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                basculerVisibilite();
            }
        });
    }

    /**
     * Bascule l'affichage du mot de passe entre texte et password.
     */
    function basculerVisibilite() {
        if (!champMotDePasse) return;

        const icone = boutonVisibilite.querySelector('i');

        if (champMotDePasse.type === 'password') {
            champMotDePasse.type = 'text';
            icone.classList.remove('fa-eye');
            icone.classList.add('fa-eye-slash');
            boutonVisibilite.setAttribute('aria-label', 'Masquer le mot de passe');
        } else {
            champMotDePasse.type = 'password';
            icone.classList.remove('fa-eye-slash');
            icone.classList.add('fa-eye');
            boutonVisibilite.setAttribute('aria-label', 'Afficher le mot de passe');
        }

        // Remettre le focus sur le champ mot de passe
        champMotDePasse.focus();
    }

    // ============================================
    // 5. SOUMISSION DU FORMULAIRE
    // ============================================

    /**
     * Valide le formulaire avant envoi et bloque la double soumission.
     */
    function initialiserSoumissionFormulaire() {
        formulaire.addEventListener('submit', function (e) {
            let estValide = true;

            // Valider les deux champs
            if (!validerEmail()) estValide = false;
            if (!validerMotDePasse()) estValide = false;

            if (!estValide) {
                e.preventDefault();

                // Afficher message d'erreur global s'il existe
                if (messageErreur) {
                    messageErreur.style.display = 'flex';
                    messageErreur.querySelector('span').textContent = 
                        'Veuillez corriger les erreurs avant de vous connecter.';
                    messageErreur.scrollIntoView({ behavior: 'smooth', block: 'center' });
                }

                // Focus sur le premier champ en erreur
                const premierInvalide = formulaire.querySelector('.champ-invalide');
                if (premierInvalide) {
                    premierInvalide.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    premierInvalide.focus();
                }
            } else {
                // Bloquer la double soumission
                desactiverBoutonSoumission();
            }
        });
    }

    /**
     * Désactive le bouton de connexion pour éviter une double soumission.
     */
    function desactiverBoutonSoumission() {
        const bouton = formulaire.querySelector('.bouton-connexion');
        if (!bouton) return;

        bouton.disabled = true;
        bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Connexion en cours...';
    }

    // ============================================
    // 6. EFFACEMENT AUTOMATIQUE DES MESSAGES
    // ============================================

    /**
     * Cache les messages d'erreur/succès dès que l'utilisateur modifie un champ.
     */
    function initialiserEffacementMessages() {
        const tousLesChamps = formulaire.querySelectorAll('input');

        tousLesChamps.forEach(function (champ) {
            champ.addEventListener('input', cacherMessages);
            champ.addEventListener('focus', cacherMessages);
        });
    }

    /**
     * Cache les bannières de message globales.
     */
    function cacherMessages() {
        if (messageErreur) {
            messageErreur.style.display = 'none';
        }
        if (messageSucces) {
            messageSucces.style.display = 'none';
        }
    }

    // ============================================
    // 7. DÉMARRAGE
    // ============================================
    initialiser();

})();