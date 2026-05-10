/**
 * =============================================
 * SCRIPT DE LA PAGE DE RECHERCHE
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Validation des dates (début < fin, pas dans le passé)
 * 2. Validation des champs numériques (capacité > 0, prix >= 0)
 * 3. Animation des cartes résultats au survol
 * 4. Effet de chargement sur le bouton rechercher
 * 5. Réinitialisation du formulaire
 * 6. Apparition animée des résultats
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var formulaireRecherche = document.querySelector('.formulaire-recherche');
    var champDateDebut = document.querySelector('input[name="dateDebut"]');
    var champDateFin = document.querySelector('input[name="dateFin"]');
    var champCapacite = document.querySelector('input[name="capacite"]');
    var champPrixMax = document.querySelector('input[name="prixMax"]');
    var boutonRechercher = document.querySelector('.bouton-rechercher');
    var cartesResultat = document.querySelectorAll('.carte-resultat');
    var boutonReinitialiser = document.querySelector('.lien-tout-voir .fa-rotate');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserValidationDates();
        initialiserValidationChamps();
        initialiserSoumissionFormulaire();
        initialiserAnimationCartes();
        initialiserDateMinimale();
    }

    // ============================================
    // 3. VALIDATION DES DATES
    // ============================================

    /**
     * Valide les dates en temps réel.
     */
    function initialiserValidationDates() {
        if (!champDateDebut || !champDateFin) return;

        // Mettre à jour la date minimale de fin quand la date de début change
        champDateDebut.addEventListener('change', function () {
            if (champDateDebut.value) {
                champDateFin.setAttribute('min', champDateDebut.value);
                
                // Si la date de fin est avant la nouvelle date de début, on la réinitialise
                if (champDateFin.value && champDateFin.value < champDateDebut.value) {
                    champDateFin.value = '';
                }
            }
            effacerErreur(champDateDebut);
            effacerErreur(champDateFin);
        });

        // Vérifier quand la date de fin change
        champDateFin.addEventListener('change', function () {
            if (champDateDebut.value && champDateFin.value) {
                if (champDateFin.value < champDateDebut.value) {
                    marquerErreur(champDateFin);
                    afficherMessageChamp(champDateFin, 'La date de fin doit être après la date de début.');
                } else {
                    effacerErreur(champDateFin);
                    supprimerMessageChamp(champDateFin);
                }
            }
        });

        // Si une date est saisie, l'autre devient obligatoire
        champDateDebut.addEventListener('change', verifierPaireDates);
        champDateFin.addEventListener('change', verifierPaireDates);
    }

    /**
     * Vérifie que les deux dates sont remplies ensemble.
     */
    function verifierPaireDates() {
        var debutRempli = champDateDebut && champDateDebut.value;
        var finRemplie = champDateFin && champDateFin.value;

        if (debutRempli && !finRemplie) {
            champDateFin.style.borderColor = '#f59e0b';
            champDateFin.style.background = '#fffbeb';
        } else if (!debutRempli && finRemplie) {
            champDateDebut.style.borderColor = '#f59e0b';
            champDateDebut.style.background = '#fffbeb';
        } else {
            if (champDateDebut) {
                champDateDebut.style.borderColor = '';
                champDateDebut.style.background = '';
            }
            if (champDateFin) {
                champDateFin.style.borderColor = '';
                champDateFin.style.background = '';
            }
        }
    }

    /**
     * Définit la date minimale (aujourd'hui) sur les champs date.
     */
    function initialiserDateMinimale() {
        var aujourdhui = new Date();
        var annee = aujourdhui.getFullYear();
        var mois = String(aujourdhui.getMonth() + 1).padStart(2, '0');
        var jour = String(aujourdhui.getDate()).padStart(2, '0');
        var dateMin = annee + '-' + mois + '-' + jour;

        if (champDateDebut) champDateDebut.setAttribute('min', dateMin);
        if (champDateFin) champDateFin.setAttribute('min', dateMin);
    }

    // ============================================
    // 4. VALIDATION CHAMPS NUMÉRIQUES
    // ============================================

    /**
     * Valide les champs capacité et prix max.
     */
    function initialiserValidationChamps() {
        if (champCapacite) {
            champCapacite.addEventListener('input', function () {
                var valeur = parseInt(champCapacite.value, 10);
                if (valeur < 1) {
                    champCapacite.value = '';
                }
            });
        }

        if (champPrixMax) {
            champPrixMax.addEventListener('input', function () {
                var valeur = parseFloat(champPrixMax.value);
                if (valeur < 0) {
                    champPrixMax.value = '';
                }
            });
        }
    }

    // ============================================
    // 5. SOUMISSION DU FORMULAIRE
    // ============================================

    /**
     * Gère la soumission avec effet de chargement.
     */
    function initialiserSoumissionFormulaire() {
        if (!formulaireRecherche) return;

        formulaireRecherche.addEventListener('submit', function (e) {
            // Vérifier la paire de dates
            var debut = champDateDebut ? champDateDebut.value : '';
            var fin = champDateFin ? champDateFin.value : '';

            if ((debut && !fin) || (!debut && fin)) {
                e.preventDefault();
                if (!fin) {
                    marquerErreur(champDateFin);
                    afficherMessageChamp(champDateFin, 'Veuillez remplir la date de départ.');
                }
                if (!debut) {
                    marquerErreur(champDateDebut);
                    afficherMessageChamp(champDateDebut, 'Veuillez remplir la date d\'arrivée.');
                }
                return;
            }

            // Effet de chargement
            if (boutonRechercher) {
                boutonRechercher.disabled = true;
                boutonRechercher.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Recherche...';
                boutonRechercher.style.opacity = '0.8';
            }
        });
    }

    // ============================================
    // 6. ANIMATION CARTES RÉSULTATS
    // ============================================

    /**
     * Anime l'apparition des cartes résultats.
     */
    function initialiserAnimationCartes() {
        if (!cartesResultat.length) return;

        // Apparition staggered
        cartesResultat.forEach(function (carte, index) {
            carte.style.opacity = '0';
            carte.style.transform = 'translateY(30px)';
            carte.style.transition = 'opacity 0.4s ease, transform 0.4s ease';

            setTimeout(function () {
                carte.style.opacity = '1';
                carte.style.transform = 'translateY(0)';
            }, 80 * (index + 1));
        });

        // Effet au survol (zoom image)
        cartesResultat.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                var img = carte.querySelector('.carte-resultat-image img');
                if (img) {
                    img.style.transform = 'scale(1.08)';
                    img.style.transition = 'transform 0.5s ease';
                }
            });

            carte.addEventListener('mouseleave', function () {
                var img = carte.querySelector('.carte-resultat-image img');
                if (img) {
                    img.style.transform = 'scale(1)';
                }
            });
        });
    }

    // ============================================
    // 7. UTILITAIRES
    // ============================================

    /**
     * Marque un champ en erreur.
     */
    function marquerErreur(champ) {
        if (!champ) return;
        champ.style.borderColor = '#ef4444';
        champ.style.background = '#fef2f2';
    }

    /**
     * Efface l'erreur sur un champ.
     */
    function effacerErreur(champ) {
        if (!champ) return;
        champ.style.borderColor = '';
        champ.style.background = '';
    }

    /**
     * Affiche un message d'erreur sous un champ.
     */
    function afficherMessageChamp(champ, message) {
        if (!champ) return;
        supprimerMessageChamp(champ);

        var msg = document.createElement('span');
        msg.className = 'message-champ-erreur';
        msg.textContent = message;
        msg.style.cssText = 
            'display:block;color:#ef4444;font-size:11px;margin-top:4px;' +
            'animation:fadeIn 0.3s ease;';

        champ.parentElement.appendChild(msg);
    }

    /**
     * Supprime le message d'erreur sous un champ.
     */
    function supprimerMessageChamp(champ) {
        if (!champ) return;
        var parent = champ.parentElement;
        if (parent) {
            var ancien = parent.querySelector('.message-champ-erreur');
            if (ancien) ancien.remove();
        }
    }

    // ============================================
    // 8. DÉMARRAGE
    // ============================================
    initialiser();

})();