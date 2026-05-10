/**
 * =============================================
 * SCRIPT DE LA PAGE PROFIL
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Validation des formulaires
 * 2. Vérification correspondance mots de passe
 * 3. Confirmation avant changement mot de passe
 * 4. Animation de la carte d'identité
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES
    // ============================================

    var formulaireProfil = document.querySelector('form input[name="action"][value="modifierProfil"]') 
        ? document.querySelector('form input[name="action"][value="modifierProfil"]').closest('form') 
        : null;

    var formulaireMdp = document.querySelector('form input[name="action"][value="changerMotDePasse"]') 
        ? document.querySelector('form input[name="action"][value="changerMotDePasse"]').closest('form') 
        : null;

    var carteIdentite = document.querySelector('.profil-carte-identite');
    var avatarGrand = document.querySelector('.avatar-grand');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserFormulaireProfil();
        initialiserFormulaireMdp();
        initialiserAnimationCarte();
    }

    // ============================================
    // 3. FORMULAIRE MODIFICATION PROFIL
    // ============================================

    function initialiserFormulaireProfil() {
        if (!formulaireProfil) return;

        formulaireProfil.addEventListener('submit', function (e) {
            var nom = document.getElementById('nom');
            var prenom = document.getElementById('prenom');

            var erreur = null;

            if (nom && !nom.value.trim()) {
                erreur = 'Le nom est obligatoire.';
                marquerErreur(nom);
            } else if (nom) {
                effacerErreur(nom);
            }

            if (prenom && !prenom.value.trim()) {
                erreur = 'Le prénom est obligatoire.';
                marquerErreur(prenom);
            } else if (prenom) {
                effacerErreur(prenom);
            }

            if (erreur) {
                e.preventDefault();
                afficherMessageErreur(formulaireProfil, erreur);
            } else {
                var bouton = formulaireProfil.querySelector('.bouton-sauvegarder');
                if (bouton) {
                    bouton.disabled = true;
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Enregistrement...';
                    bouton.style.opacity = '0.7';
                }
            }
        });
    }

    // ============================================
    // 4. FORMULAIRE CHANGEMENT MOT DE PASSE
    // ============================================

    function initialiserFormulaireMdp() {
        if (!formulaireMdp) return;

        var ancien = document.getElementById('ancienMotDePasse');
        var nouveau = document.getElementById('nouveauMotDePasse');
        var confirmation = document.getElementById('confirmationMotDePasse');

        // Vérification en temps réel de la correspondance
        if (confirmation && nouveau) {
            confirmation.addEventListener('input', verifierCorrespondance);
            nouveau.addEventListener('input', function () {
                if (confirmation.value) verifierCorrespondance();
            });
        }

        formulaireMdp.addEventListener('submit', function (e) {
            var erreur = null;

            if (ancien && !ancien.value) {
                erreur = 'Veuillez saisir votre mot de passe actuel.';
                marquerErreur(ancien);
            } else if (ancien) {
                effacerErreur(ancien);
            }

            if (nouveau && nouveau.value.length < 8) {
                erreur = 'Le nouveau mot de passe doit contenir au moins 8 caractères.';
                marquerErreur(nouveau);
            } else if (nouveau) {
                effacerErreur(nouveau);
            }

            if (nouveau && confirmation && nouveau.value !== confirmation.value) {
                erreur = 'Les nouveaux mots de passe ne correspondent pas.';
                marquerErreur(confirmation);
            } else if (confirmation && nouveau && nouveau.value === confirmation.value && confirmation.value) {
                effacerErreur(confirmation);
            }

            if (erreur) {
                e.preventDefault();
                afficherMessageErreur(formulaireMdp, erreur);
            } else {
                if (!confirm('Voulez-vous vraiment changer votre mot de passe ?')) {
                    e.preventDefault();
                    return;
                }

                var bouton = formulaireMdp.querySelector('.bouton-mdp');
                if (bouton) {
                    bouton.disabled = true;
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Changement...';
                    bouton.style.opacity = '0.7';
                }
            }
        });
    }

    /**
     * Vérifie que la confirmation correspond au nouveau mot de passe.
     */
    function verifierCorrespondance() {
        var nouveau = document.getElementById('nouveauMotDePasse');
        var confirmation = document.getElementById('confirmationMotDePasse');

        if (!nouveau || !confirmation) return;

        if (confirmation.value && nouveau.value !== confirmation.value) {
            marquerErreur(confirmation);
            var message = confirmation.parentElement.parentElement.querySelector('.message-champ-erreur');
            if (!message) {
                message = document.createElement('span');
                message.className = 'message-champ-erreur';
                message.style.cssText = 'display:block;color:#ef4444;font-size:12px;margin-top:4px;';
                confirmation.parentElement.parentElement.appendChild(message);
            }
            message.textContent = 'Les mots de passe ne correspondent pas.';
        } else if (confirmation.value && nouveau.value === confirmation.value) {
            effacerErreur(confirmation);
            var message = confirmation.parentElement.parentElement.querySelector('.message-champ-erreur');
            if (message) message.remove();
        }
    }

    // ============================================
    // 5. ANIMATION CARTE D'IDENTITÉ
    // ============================================

    function initialiserAnimationCarte() {
        if (!carteIdentite) return;

        carteIdentite.addEventListener('mouseenter', function () {
            if (avatarGrand) {
                avatarGrand.style.transform = 'scale(1.1)';
                avatarGrand.style.transition = 'transform 0.4s ease';
            }
        });

        carteIdentite.addEventListener('mouseleave', function () {
            if (avatarGrand) {
                avatarGrand.style.transform = 'scale(1)';
            }
        });
    }

    // ============================================
    // 6. UTILITAIRES
    // ============================================

    function marquerErreur(champ) {
        champ.style.borderColor = '#ef4444';
        champ.style.background = '#fef2f2';
    }

    function effacerErreur(champ) {
        champ.style.borderColor = '';
        champ.style.background = '';
    }

    function afficherMessageErreur(formulaire, message) {
        var existant = formulaire.querySelector('.message-erreur-local');
        if (existant) existant.remove();

        var msg = document.createElement('div');
        msg.className = 'message-erreur message-erreur-local';
        msg.style.cssText = 
            'display:flex;align-items:center;gap:10px;background:#fef2f2;' +
            'border:1px solid #fecaca;border-left:4px solid #ef4444;' +
            'color:#991b1b;padding:12px 16px;border-radius:10px;' +
            'font-size:13px;margin-bottom:16px;animation:slideDown 0.3s ease;';
        msg.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i><span>' + message + '</span>';

        var sectionTitre = formulaire.querySelector('.section-titre');
        if (sectionTitre) {
            sectionTitre.after(msg);
        } else {
            formulaire.insertBefore(msg, formulaire.firstChild);
        }

        setTimeout(function () {
            if (msg.parentNode) msg.remove();
        }, 5000);
    }

    // ============================================
    // 7. DÉMARRAGE
    // ============================================
    initialiser();

})();