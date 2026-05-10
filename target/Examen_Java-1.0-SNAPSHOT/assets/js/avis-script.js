/**
 * =============================================
 * SCRIPT DE LA PAGE DONNER MON AVIS
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Système d'étoiles cliquables pour chaque critère
 * 2. Calcul automatique de la note globale
 * 3. Effet visuel au survol des étoiles
 * 4. Compteur de caractères pour le commentaire
 * 5. Validation avant soumission
 * 6. Protection contre la double soumission
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var groupesEtoiles = document.querySelectorAll('.critere-etoiles');
    var noteGlobaleValeur = document.getElementById('noteGlobale');
    var etoilesGlobales = document.getElementById('etoilesGlobales');
    var textareaCommentaire = document.getElementById('commentaire');
    var compteurCaracteres = document.querySelector('.compteur-caracteres');
    var formulaire = document.querySelector('.avis-formulaire');
    var boutonPublier = document.querySelector('.bouton-publier-avis');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserEtoiles();
        initialiserCompteurCaracteres();
        initialiserSoumission();
        mettreAJourNoteGlobale();
    }

    // ============================================
    // 3. SYSTÈME D'ÉTOILES
    // ============================================

    /**
     * Initialise les étoiles cliquables pour chaque critère.
     */
    function initialiserEtoiles() {
        if (!groupesEtoiles.length) return;

        groupesEtoiles.forEach(function (groupe) {
            var nomCritere = groupe.getAttribute('data-critere');
            var inputCache = document.getElementById(nomCritere);
            var etoiles = groupe.querySelectorAll('.etoile');

            // Clic sur une étoile
            etoiles.forEach(function (etoile) {
                etoile.addEventListener('click', function () {
                    var valeur = parseInt(etoile.getAttribute('data-valeur'), 10);
                    if (inputCache) inputCache.value = valeur;
                    colorierEtoiles(groupe, valeur);
                    mettreAJourNoteGlobale();
                });

                // Survol
                etoile.addEventListener('mouseenter', function () {
                    var valeur = parseInt(etoile.getAttribute('data-valeur'), 10);
                    colorierEtoilesTemporaire(groupe, valeur);
                });

                etoile.addEventListener('mouseleave', function () {
                    var valeurActuelle = inputCache ? parseInt(inputCache.value, 10) : 0;
                    colorierEtoiles(groupe, valeurActuelle);
                });
            });
        });
    }

    /**
     * Colorie les étoiles jusqu'à une valeur donnée.
     */
    function colorierEtoiles(groupe, valeur) {
        var etoiles = groupe.querySelectorAll('.etoile');
        etoiles.forEach(function (etoile) {
            var valEtoile = parseInt(etoile.getAttribute('data-valeur'), 10);
            if (valEtoile <= valeur) {
                etoile.classList.add('active');
                etoile.querySelector('i').className = 'fa-solid fa-star';
            } else {
                etoile.classList.remove('active');
                etoile.querySelector('i').className = 'fa-regular fa-star';
            }
        });
    }

    /**
     * Colorie temporairement les étoiles au survol.
     */
    function colorierEtoilesTemporaire(groupe, valeur) {
        var etoiles = groupe.querySelectorAll('.etoile');
        etoiles.forEach(function (etoile) {
            var valEtoile = parseInt(etoile.getAttribute('data-valeur'), 10);
            if (valEtoile <= valeur) {
                etoile.querySelector('i').className = 'fa-solid fa-star';
                etoile.querySelector('i').style.color = '#f59e0b';
            } else {
                etoile.querySelector('i').className = 'fa-regular fa-star';
                etoile.querySelector('i').style.color = '#cbd5e1';
            }
        });
    }

    // ============================================
    // 4. NOTE GLOBALE
    // ============================================

    /**
     * Calcule et affiche la note globale moyenne.
     */
    function mettreAJourNoteGlobale() {
        var notes = [
            parseInt(document.getElementById('note_proprete')?.value || 0),
            parseInt(document.getElementById('note_communication')?.value || 0),
            parseInt(document.getElementById('note_emplacement')?.value || 0),
            parseInt(document.getElementById('note_rapport_qualite_prix')?.value || 0)
        ];

        var somme = notes.reduce(function (a, b) { return a + b; }, 0);
        var moyenne = somme / notes.length;

        if (somme === 0) {
            if (noteGlobaleValeur) noteGlobaleValeur.textContent = '-';
            if (etoilesGlobales) etoilesGlobales.innerHTML = '';
            return;
        }

        if (noteGlobaleValeur) {
            noteGlobaleValeur.textContent = moyenne.toFixed(1);
        }

        // Afficher les étoiles globales
        if (etoilesGlobales) {
            etoilesGlobales.innerHTML = '';
            var etoilesPleines = Math.round(moyenne);
            for (var i = 0; i < 5; i++) {
                var etoile = document.createElement('span');
                if (i < etoilesPleines) {
                    etoile.innerHTML = '<i class="fa-solid fa-star"></i>';
                } else {
                    etoile.innerHTML = '<i class="fa-regular fa-star"></i>';
                }
                etoilesGlobales.appendChild(etoile);
            }
        }
    }

    // ============================================
    // 5. COMPTEUR DE CARACTÈRES
    // ============================================

    /**
     * Gère le compteur de caractères du commentaire.
     */
    function initialiserCompteurCaracteres() {
        if (!textareaCommentaire || !compteurCaracteres) return;

        var maxCaracteres = 1000;

        textareaCommentaire.addEventListener('input', function () {
            var nombre = textareaCommentaire.value.length;
            compteurCaracteres.textContent = nombre + ' / ' + maxCaracteres;

            // Couleurs selon la progression
            compteurCaracteres.classList.remove('proche-limite', 'limite-atteinte');
            if (nombre >= maxCaracteres) {
                compteurCaracteres.classList.add('limite-atteinte');
            } else if (nombre >= maxCaracteres * 0.8) {
                compteurCaracteres.classList.add('proche-limite');
            }
        });
    }

    // ============================================
    // 6. SOUMISSION DU FORMULAIRE
    // ============================================

    /**
     * Valide et protège la soumission du formulaire.
     */
    function initialiserSoumission() {
        if (!formulaire) return;

        formulaire.addEventListener('submit', function (e) {
            // Vérifier que tous les critères sont notés
            var criteres = ['note_proprete', 'note_communication', 'note_emplacement', 'note_rapport_qualite_prix'];
            var tousNotes = true;

            criteres.forEach(function (critere) {
                var input = document.getElementById(critere);
                if (!input || parseInt(input.value, 10) === 0) {
                    tousNotes = false;
                }
            });

            if (!tousNotes) {
                e.preventDefault();
                afficherMessageErreur('Veuillez attribuer une note à tous les critères avant de publier votre avis.');
                return;
            }

            // Désactiver le bouton
            if (boutonPublier) {
                boutonPublier.disabled = true;
                boutonPublier.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Publication...';
                boutonPublier.style.opacity = '0.8';
                boutonPublier.style.cursor = 'not-allowed';
            }
        });
    }

    /**
     * Affiche un message d'erreur temporaire.
     */
    function afficherMessageErreur(message) {
        // Supprimer un ancien message
        var ancien = document.querySelector('.message-erreur');
        if (ancien) {
            ancien.style.transition = 'opacity 0.3s ease';
            ancien.style.opacity = '0';
            setTimeout(function () {
                if (ancien.parentNode) ancien.remove();
            }, 300);
        }

        // Créer le nouveau message
        var msg = document.createElement('div');
        msg.className = 'message-erreur';
        msg.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i><span>' + message + '</span>';
        msg.style.cssText = 
            'display:flex;align-items:center;gap:10px;background:#fef2f2;border:1px solid #fecaca;' +
            'border-left:4px solid #ef4444;color:#991b1b;padding:14px 18px;border-radius:12px;' +
            'font-size:14px;font-weight:500;margin-bottom:24px;animation:slideDown 0.3s ease;';

        var carte = document.querySelector('.avis-formulaire-carte');
        if (carte) {
            carte.parentNode.insertBefore(msg, carte);
        }

        // Scroll vers le message
        msg.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // Disparition automatique
        setTimeout(function () {
            if (msg.parentNode) {
                msg.style.transition = 'opacity 0.5s ease';
                msg.style.opacity = '0';
                setTimeout(function () {
                    if (msg.parentNode) msg.remove();
                }, 500);
            }
        }, 4000);
    }

    // ============================================
    // 7. DÉMARRAGE
    // ============================================
    initialiser();

})();