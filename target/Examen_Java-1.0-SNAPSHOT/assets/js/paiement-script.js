/**
 * =============================================
 * SCRIPT DES PAGES DE PAIEMENT
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 2.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Sélection méthode de paiement (carte/virement)
 * 2. Affichage/masquage des détails carte bancaire
 * 3. Formatage automatique du numéro de carte (1234 5678 9012 3456)
 * 4. Formatage date expiration (MM/AA) avec validation
 * 5. Limitation CVV à 3-4 chiffres
 * 6. Protection contre la double soumission
 * 7. Animation du bouton de paiement
 * 8. Animation des cartes de paiement (page historique)
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    // Page de paiement
    var methodeCarte = document.getElementById('methodeCarte');
    var methodeVirement = document.getElementById('methodeVirement');
    var detailsCarte = document.getElementById('detailsCarte');
    var inputNumeroCarte = document.querySelector('#detailsCarte input[placeholder="1234 5678 9012 3456"]');
    var inputExpiration = document.querySelector('#detailsCarte input[placeholder="MM/AA"]');
    var inputCVV = document.querySelector('#detailsCarte input[placeholder="123"]');
    var formulaire = document.querySelector('.formulaire-paiement');
    var boutonPayer = document.querySelector('.bouton-payer');
    var methodes = document.querySelectorAll('.methode-item');

    // Page historique
    var cartesPaiement = document.querySelectorAll('.carte-paiement');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserMethodesPaiement();
        initialiserFormatageCarte();
        initialiserFormatageExpiration();
        initialiserFormatageCVV();
        initialiserSoumission();
        initialiserAnimationCartes();
    }

    // ============================================
    // 3. SÉLECTION MÉTHODE DE PAIEMENT
    // ============================================

    function initialiserMethodesPaiement() {
        if (!methodes.length) return;

        methodes.forEach(function (methode) {
            methode.addEventListener('click', function () {
                // Mettre à jour le radio
                var radio = methode.querySelector('input[type="radio"]');
                if (radio) radio.checked = true;

                // Mettre à jour les classes active
                methodes.forEach(function (m) { m.classList.remove('active'); });
                methode.classList.add('active');

                // Afficher/masquer les détails carte
                var type = radio ? radio.value : '';
                if (type === 'CARTE_BANCAIRE') {
                    if (detailsCarte) {
                        detailsCarte.style.display = 'block';
                        detailsCarte.style.animation = 'slideDown 0.3s ease';
                    }
                } else {
                    if (detailsCarte) {
                        detailsCarte.style.display = 'none';
                    }
                }
            });
        });
    }

    // ============================================
    // 4. FORMATAGE NUMÉRO DE CARTE
    // ============================================

    function initialiserFormatageCarte() {
        if (!inputNumeroCarte) return;

        inputNumeroCarte.addEventListener('input', function () {
            var valeur = inputNumeroCarte.value.replace(/\s/g, '').replace(/[^0-9]/g, '');
            
            // Limiter à 16 chiffres
            if (valeur.length > 16) valeur = valeur.substring(0, 16);

            // Ajouter des espaces tous les 4 chiffres
            var formate = '';
            for (var i = 0; i < valeur.length; i++) {
                if (i > 0 && i % 4 === 0) formate += ' ';
                formate += valeur.charAt(i);
            }

            inputNumeroCarte.value = formate;

            // Changer la couleur si le numéro est complet
            if (valeur.length === 16) {
                inputNumeroCarte.style.borderColor = '#10b981';
                inputNumeroCarte.style.background = '#f0fdf4';
            } else {
                inputNumeroCarte.style.borderColor = '#e2e8f0';
                inputNumeroCarte.style.background = '#ffffff';
            }
        });
    }

    // ============================================
    // 5. FORMATAGE DATE EXPIRATION
    // ============================================

    function initialiserFormatageExpiration() {
        if (!inputExpiration) return;

        inputExpiration.addEventListener('input', function () {
            var valeur = inputExpiration.value.replace(/[^0-9]/g, '');

            if (valeur.length > 4) valeur = valeur.substring(0, 4);

            if (valeur.length >= 2) {
                var mois = parseInt(valeur.substring(0, 2), 10);
                if (mois > 12) mois = 12;
                if (mois < 1) mois = 1;
                valeur = (mois < 10 ? '0' : '') + mois + '/' + valeur.substring(2);
            }

            inputExpiration.value = valeur;

            // Validation de la date
            if (valeur.length === 5) {
                var parties = valeur.split('/');
                var moisVal = parseInt(parties[0], 10);
                var anneeVal = parseInt('20' + parties[1], 10);
                var maintenant = new Date();
                var anneeActuelle = maintenant.getFullYear();
                var moisActuel = maintenant.getMonth() + 1;

                if (anneeVal > anneeActuelle || (anneeVal === anneeActuelle && moisVal >= moisActuel)) {
                    inputExpiration.style.borderColor = '#10b981';
                    inputExpiration.style.background = '#f0fdf4';
                } else {
                    inputExpiration.style.borderColor = '#ef4444';
                    inputExpiration.style.background = '#fef2f2';
                }
            } else {
                inputExpiration.style.borderColor = '#e2e8f0';
                inputExpiration.style.background = '#ffffff';
            }
        });
    }

    // ============================================
    // 6. FORMATAGE CVV
    // ============================================

    function initialiserFormatageCVV() {
        if (!inputCVV) return;

        inputCVV.addEventListener('input', function () {
            var valeur = inputCVV.value.replace(/[^0-9]/g, '');
            if (valeur.length > 4) valeur = valeur.substring(0, 4);
            inputCVV.value = valeur;

            if (valeur.length >= 3) {
                inputCVV.style.borderColor = '#10b981';
                inputCVV.style.background = '#f0fdf4';
            } else {
                inputCVV.style.borderColor = '#e2e8f0';
                inputCVV.style.background = '#ffffff';
            }
        });
    }

    // ============================================
    // 7. SOUMISSION DU FORMULAIRE
    // ============================================

    function initialiserSoumission() {
        if (!formulaire || !boutonPayer) return;

        formulaire.addEventListener('submit', function () {
            // Désactiver le bouton immédiatement
            boutonPayer.disabled = true;
            boutonPayer.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Paiement en cours...';
            boutonPayer.style.opacity = '0.8';
            boutonPayer.style.cursor = 'not-allowed';

            // Effet de pulse sur le formulaire
            var carteFormulaire = document.querySelector('.formulaire-carte');
            if (carteFormulaire) {
                carteFormulaire.style.transform = 'scale(0.99)';
                carteFormulaire.style.transition = 'transform 0.3s ease';
                setTimeout(function () {
                    carteFormulaire.style.transform = 'scale(1)';
                }, 400);
            }
        });
    }

    // ============================================
    // 8. ANIMATION CARTES (PAGE HISTORIQUE)
    // ============================================

    function initialiserAnimationCartes() {
        if (!cartesPaiement.length) return;

        cartesPaiement.forEach(function (carte, index) {
            carte.style.opacity = '0';
            carte.style.transform = 'translateY(20px)';
            carte.style.transition = 'opacity 0.4s ease, transform 0.4s ease';

            setTimeout(function () {
                carte.style.opacity = '1';
                carte.style.transform = 'translateY(0)';
            }, 70 * (index + 1));
        });
    }

    // ============================================
    // 9. STYLES INJECTÉS
    // ============================================

    function injecterStyles() {
        if (document.getElementById('keyframes-paiement')) return;

        var style = document.createElement('style');
        style.id = 'keyframes-paiement';
        style.textContent =
            '@keyframes slideDown {' +
            '  from { opacity: 0; transform: translateY(-8px); }' +
            '  to   { opacity: 1; transform: translateY(0); }' +
            '}';
        document.head.appendChild(style);
    }

    // ============================================
    // 10. DÉMARRAGE
    // ============================================
    injecterStyles();
    initialiser();

})();