/**
 * =============================================
 * SCRIPT DU FORMULAIRE D'INSCRIPTION
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS DU DOM
    // ============================================

    const formulaire = document.querySelector('.formulaire-inscription');
    const champMotDePasse = document.getElementById('motDePasse');
    const champConfirmation = document.getElementById('confirmation');
    const boutonsVisibilite = document.querySelectorAll('.bouton-visibilite');
    const messageErreur = document.querySelector('.message-erreur');
    const toutesLesCartesRole = document.querySelectorAll('.carte-role');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    /**
     * Initialise tous les écouteurs d'événements.
     */
    function initialiser() {
        if (!formulaire) return;

        // Validation en temps réel sur les champs
        initialiserValidationTempsReel();

        // Gestion de la visibilité des mots de passe
        initialiserVisibiliteMotDePasse();

        // Animation des cartes de rôle
        initialiserCartesRole();

        // Validation à la soumission
        formulaire.addEventListener('submit', validerSoumission);

        // Effacement automatique du message d'erreur
        if (messageErreur) {
            initialiserEffacementErreur();
        }
    }

    // ============================================
    // 3. VALIDATION EN TEMPS RÉEL
    // ============================================

    /**
     * Ajoute la validation en temps réel sur chaque champ.
     */
    function initialiserValidationTempsReel() {
        const champs = formulaire.querySelectorAll('input[required]');

        champs.forEach(function (champ) {
            champ.addEventListener('blur', function () {
                validerChamp(champ);
            });

            champ.addEventListener('input', function () {
                // Si le champ était en erreur, on revalide
                if (champ.classList.contains('champ-invalide')) {
                    validerChamp(champ);
                }
            });
        });

        // Validation spécifique pour la confirmation du mot de passe
        if (champMotDePasse && champConfirmation) {
            champMotDePasse.addEventListener('input', verifierCorrespondanceMotDePasse);
            champConfirmation.addEventListener('input', verifierCorrespondanceMotDePasse);
        }
    }

    /**
     * Valide un champ individuel.
     * @param {HTMLElement} champ - Le champ à valider
     * @returns {boolean} true si valide, false sinon
     */
    function validerChamp(champ) {
        // Réinitialiser l'état
        champ.classList.remove('champ-invalide', 'champ-valide');

        // Supprimer l'ancien message d'erreur
        const ancienMessage = champ.parentElement.parentElement.querySelector('.message-champ-erreur');
        if (ancienMessage) {
            ancienMessage.remove();
        }

        // Vérifier si le champ est vide
        if (!champ.value.trim()) {
            afficherErreurChamp(champ, 'Ce champ est obligatoire.');
            return false;
        }

        // Validation email
        if (champ.type === 'email') {
            const regexEmail = /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
            if (!regexEmail.test(champ.value.trim())) {
                afficherErreurChamp(champ, 'Veuillez saisir une adresse email valide.');
                return false;
            }
        }

        // Validation téléphone (si rempli)
        if (champ.id === 'telephone' && champ.value.trim()) {
            const regexTel = /^(\+?\d{1,3}[- ]?)?\d{9,10}$/;
            if (!regexTel.test(champ.value.trim().replace(/\s/g, ''))) {
                afficherErreurChamp(champ, 'Veuillez saisir un numéro de téléphone valide.');
                return false;
            }
        }

        // Validation mot de passe
        if (champ.id === 'motDePasse') {
            if (champ.value.length < 8) {
                afficherErreurChamp(champ, 'Le mot de passe doit contenir au moins 8 caractères.');
                return false;
            }
        }

        // Champ valide
        champ.classList.add('champ-valide');
        return true;
    }

    /**
     * Vérifie que la confirmation correspond au mot de passe.
     */
    function verifierCorrespondanceMotDePasse() {
        if (!champMotDePasse || !champConfirmation) return;

        const ancienMessage = champConfirmation.parentElement.parentElement.querySelector('.message-champ-erreur');
        if (ancienMessage) {
            ancienMessage.remove();
        }
        champConfirmation.classList.remove('champ-invalide', 'champ-valide');

        if (champConfirmation.value) {
            if (champMotDePasse.value === champConfirmation.value) {
                champConfirmation.classList.add('champ-valide');
            } else {
                champConfirmation.classList.add('champ-invalide');
                afficherErreurChamp(champConfirmation, 'Les mots de passe ne correspondent pas.');
            }
        }
    }

    /**
     * Affiche un message d'erreur sous un champ.
     * @param {HTMLElement} champ - Le champ concerné
     * @param {string} message - Le message d'erreur
     */
    function afficherErreurChamp(champ, message) {
        champ.classList.add('champ-invalide');

        const messageErreurChamp = document.createElement('span');
        messageErreurChamp.className = 'message-champ-erreur';
        messageErreurChamp.textContent = message;

        champ.parentElement.parentElement.appendChild(messageErreurChamp);
    }

    // ============================================
    // 4. VISIBILITÉ DU MOT DE PASSE
    // ============================================

    /**
     * Initialise les boutons de visibilité des mots de passe.
     */
    function initialiserVisibiliteMotDePasse() {
        boutonsVisibilite.forEach(function (bouton) {
            bouton.addEventListener('click', function (e) {
                e.preventDefault();
                basculerVisibilite(bouton);
            });
        });
    }

    /**
     * Bascule la visibilité d'un champ mot de passe.
     * @param {HTMLElement} bouton - Le bouton cliqué
     */
    function basculerVisibilite(bouton) {
        const champIcone = bouton.parentElement;
        const champ = champIcone.querySelector('input');

        if (!champ) return;

        const icone = bouton.querySelector('i');

        if (champ.type === 'password') {
            champ.type = 'text';
            icone.classList.remove('fa-eye');
            icone.classList.add('fa-eye-slash');
            bouton.setAttribute('aria-label', 'Masquer le mot de passe');
        } else {
            champ.type = 'password';
            icone.classList.remove('fa-eye-slash');
            icone.classList.add('fa-eye');
            bouton.setAttribute('aria-label', 'Afficher le mot de passe');
        }

        // Remettre le focus sur le champ
        champ.focus();
    }

    // ============================================
    // 5. CARTES DE RÔLE (ANIMATION)
    // ============================================

    /**
     * Initialise les animations des cartes de rôle.
     */
    function initialiserCartesRole() {
        toutesLesCartesRole.forEach(function (carte) {
            const inputRadio = carte.querySelector('input[type="radio"]');

            carte.addEventListener('click', function () {
                inputRadio.checked = true;
                
                // Animation de pulse sur l'icône
                const icone = carte.querySelector('.contenu-role i');
                if (icone) {
                    icone.style.transform = 'scale(1.3)';
                    setTimeout(function () {
                        icone.style.transform = 'scale(1.1)';
                    }, 200);
                }
            });

            // Navigation clavier
            carte.addEventListener('keydown', function (e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    carte.click();
                }
            });
        });
    }

    // ============================================
    // 6. VALIDATION À LA SOUMISSION
    // ============================================

    /**
     * Valide le formulaire complet avant soumission.
     * @param {Event} e - L'événement de soumission
     */
    function validerSoumission(e) {
        let formulaireValide = true;
        const champsRequis = formulaire.querySelectorAll('input[required]');

        // Valider chaque champ requis
        champsRequis.forEach(function (champ) {
            if (!validerChamp(champ)) {
                formulaireValide = false;
            }
        });

        // Vérifier la correspondance des mots de passe
        if (champMotDePasse && champConfirmation) {
            if (champMotDePasse.value !== champConfirmation.value) {
                afficherErreurChamp(champConfirmation, 'Les mots de passe ne correspondent pas.');
                formulaireValide = false;
            }
        }

        // Vérifier les conditions d'utilisation
        const checkboxConditions = formulaire.querySelector('input[name="conditions"]');
        if (checkboxConditions && !checkboxConditions.checked) {
            afficherErreurConditions('Vous devez accepter les conditions d\'utilisation.');
            formulaireValide = false;
        }

        // Vérifier le rôle
        const roleSelectionne = formulaire.querySelector('input[name="role"]:checked');
        if (!roleSelectionne) {
            afficherErreurRole('Veuillez sélectionner un rôle.');
            formulaireValide = false;
        }

        if (!formulaireValide) {
            e.preventDefault();

            // Afficher un message d'erreur global
            if (messageErreur) {
                messageErreur.style.display = 'flex';
                messageErreur.querySelector('span').textContent = 
                    'Veuillez corriger les erreurs du formulaire.';
                messageErreur.scrollIntoView({ behavior: 'smooth', block: 'center' });
            }

            // Scroll au premier champ en erreur
            const premierChampInvalide = formulaire.querySelector('.champ-invalide');
            if (premierChampInvalide) {
                premierChampInvalide.scrollIntoView({ behavior: 'smooth', block: 'center' });
                premierChampInvalide.focus();
            }
        } else {
            // Désactiver le bouton pour éviter la double soumission
            const bouton = formulaire.querySelector('.bouton-inscription');
            if (bouton) {
                bouton.disabled = true;
                bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Création du compte...';
            }
        }
    }

    /**
     * Affiche une erreur sur la checkbox des conditions.
     * @param {string} message - Le message d'erreur
     */
    function afficherErreurConditions(message) {
        const sectionConditions = formulaire.querySelector('.conditions');
        const ancienMessage = sectionConditions.querySelector('.message-conditions-erreur');
        if (ancienMessage) ancienMessage.remove();

        const messageErreurCond = document.createElement('span');
        messageErreurCond.className = 'message-conditions-erreur';
        messageErreurCond.textContent = message;
        sectionConditions.appendChild(messageErreurCond);
    }

    /**
     * Affiche une erreur sur le sélecteur de rôle.
     * @param {string} message - Le message d'erreur
     */
    function afficherErreurRole(message) {
        const sectionRole = formulaire.querySelector('.selecteur-role');
        const ancienMessage = sectionRole.parentElement.querySelector('.message-role-erreur');
        if (ancienMessage) ancienMessage.remove();

        const messageErreurRole = document.createElement('span');
        messageErreurRole.className = 'message-role-erreur';
        messageErreurRole.textContent = message;
        sectionRole.parentElement.appendChild(messageErreurRole);
    }

    // ============================================
    // 7. EFFACEMENT AUTOMATIQUE DU MESSAGE D'ERREUR
    // ============================================

    /**
     * Cache le message d'erreur global quand l'utilisateur commence à taper.
     */
    function initialiserEffacementErreur() {
        const tousLesChamps = formulaire.querySelectorAll('input');
        tousLesChamps.forEach(function (champ) {
            champ.addEventListener('input', function () {
                if (messageErreur && messageErreur.style.display !== 'none') {
                    messageErreur.style.display = 'none';
                }
            });
        });

        // Clic sur les cartes de rôle
        toutesLesCartesRole.forEach(function (carte) {
            carte.addEventListener('click', function () {
                if (messageErreur && messageErreur.style.display !== 'none') {
                    messageErreur.style.display = 'none';
                }
                // Supprimer le message d'erreur du rôle
                const msgRole = formulaire.querySelector('.message-role-erreur');
                if (msgRole) msgRole.remove();
            });
        });

        // Clic sur la checkbox conditions
        const checkboxConditions = formulaire.querySelector('input[name="conditions"]');
        if (checkboxConditions) {
            checkboxConditions.addEventListener('change', function () {
                const msgCond = formulaire.querySelector('.message-conditions-erreur');
                if (msgCond) msgCond.remove();
            });
        }
    }

    // ============================================
    // 8. DÉMARRAGE
    // ============================================
    initialiser();

})();