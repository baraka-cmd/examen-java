/**
 * =============================================
 * SCRIPT DE LA PAGE DES REVENUS
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Animation des barres de progression
 * 2. Animation des compteurs dans les cartes résumé
 * 3. Survol des lignes du tableau
 * 4. Filtres actifs avec effet visuel
 * 5. Tri visuel du tableau (optionnel)
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var barresRemplissage = document.querySelectorAll('.barre-remplissage');
    var resumeValeurs = document.querySelectorAll('.resume-valeur');
    var lignesTableau = document.querySelectorAll('.tableau-revenus tbody tr');
    var filtreBoutons = document.querySelectorAll('.filtre-bouton');
    var cartesResume = document.querySelectorAll('.resume-carte');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserAnimationBarres();
        initialiserAnimationCompteurs();
        initialiserSurvolTableau();
        initialiserFiltresActifs();
        initialiserSurvolCartesResume();
    }

    // ============================================
    // 3. ANIMATION DES BARRES DE PROGRESSION
    // ============================================

    /**
     * Anime les barres de progression du revenu par maison.
     * Les barres partent de 0 et atteignent leur largeur finale.
     */
    function initialiserAnimationBarres() {
        if (!barresRemplissage.length) return;

        // Observer pour déclencher l'animation quand visible
        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (entry.isIntersecting) {
                        var barre = entry.target;
                        var largeurFinale = barre.style.width;
                        
                        // Réinitialiser et animer
                        barre.style.width = '0%';
                        barre.style.transition = 'width 1.2s ease-out';
                        
                        setTimeout(function () {
                            barre.style.width = largeurFinale;
                        }, 200);

                        observer.unobserve(barre);
                    }
                });
            }, { threshold: 0.3 });

            barresRemplissage.forEach(function (barre) {
                observer.observe(barre);
            });
        } else {
            // Fallback : animation directe
            barresRemplissage.forEach(function (barre) {
                var largeurFinale = barre.style.width;
                barre.style.width = '0%';
                barre.style.transition = 'width 1.2s ease-out';
                
                setTimeout(function () {
                    barre.style.width = largeurFinale;
                }, 300);
            });
        }
    }

    // ============================================
    // 4. ANIMATION DES COMPTEURS
    // ============================================

    /**
     * Anime les valeurs dans les cartes résumé.
     * Les nombres s'incrémentent de 0 à leur valeur finale.
     */
    function initialiserAnimationCompteurs() {
        if (!resumeValeurs.length) return;

        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (entry.isIntersecting) {
                        animerValeur(entry.target);
                        observer.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.5 });

            resumeValeurs.forEach(function (valeur) {
                observer.observe(valeur);
            });
        } else {
            resumeValeurs.forEach(function (valeur) {
                animerValeur(valeur);
            });
        }
    }

    /**
     * Anime une valeur (nombre € ou nombre simple).
     * @param {HTMLElement} element - L'élément contenant la valeur
     */
    function animerValeur(element) {
        var texte = element.textContent.trim();
        var suffixe = '';
        var valeurCible = 0;
        var estDecimal = false;

        // Détecter le format
        if (texte.indexOf('€') !== -1) {
            suffixe = ' €';
            var nombreStr = texte.replace('€', '').replace(/\s/g, '');
            if (nombreStr.indexOf('.') !== -1) estDecimal = true;
            valeurCible = parseFloat(nombreStr);
        } else {
            valeurCible = parseInt(texte, 10);
        }

        if (isNaN(valeurCible) || valeurCible === 0) return;

        var valeurActuelle = 0;
        var duree = 1000;
        var intervalle = 16;
        var nombrePas = duree / intervalle;
        var pas = valeurCible / nombrePas;
        var compteur = 0;

        element.textContent = '0' + suffixe;

        var animation = setInterval(function () {
            compteur++;
            valeurActuelle += pas;

            if (compteur >= nombrePas) {
                valeurActuelle = valeurCible;
                clearInterval(animation);
            }

            if (estDecimal) {
                element.textContent = valeurActuelle.toFixed(0) + suffixe;
            } else {
                element.textContent = Math.round(valeurActuelle) + suffixe;
            }
        }, intervalle);
    }

    // ============================================
    // 5. SURVOL TABLEAU
    // ============================================

    /**
     * Effet visuel au survol des lignes du tableau.
     */
    function initialiserSurvolTableau() {
        if (!lignesTableau.length) return;

        lignesTableau.forEach(function (ligne) {
            ligne.addEventListener('mouseenter', function () {
                ligne.style.backgroundColor = '#f8fafc';
                ligne.style.transition = 'background-color 0.2s ease';
            });

            ligne.addEventListener('mouseleave', function () {
                ligne.style.backgroundColor = '';
            });

            // Effet au clic (sélection temporaire)
            ligne.addEventListener('click', function () {
                ligne.style.backgroundColor = '#eff6ff';
                setTimeout(function () {
                    ligne.style.backgroundColor = '';
                }, 400);
            });
        });
    }

    // ============================================
    // 6. FILTRES ACTIFS
    // ============================================

    /**
     * Ajoute un effet visuel au clic sur les filtres.
     */
    function initialiserFiltresActifs() {
        if (!filtreBoutons.length) return;

        filtreBoutons.forEach(function (bouton) {
            bouton.addEventListener('click', function (e) {
                // Effet de clic
                bouton.style.transform = 'scale(0.95)';
                setTimeout(function () {
                    bouton.style.transform = '';
                }, 150);

                // Si le filtre est déjà actif, on laisse la redirection naturelle
                if (bouton.classList.contains('filtre-actif')) {
                    e.preventDefault();
                }
            });
        });
    }

    // ============================================
    // 7. SURVOL CARTES RÉSUMÉ
    // ============================================

    /**
     * Léger effet au survol des cartes résumé.
     */
    function initialiserSurvolCartesResume() {
        if (!cartesResume.length) return;

        cartesResume.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                var icone = carte.querySelector('.resume-icone i');
                if (icone) {
                    icone.style.transform = 'scale(1.15)';
                    icone.style.transition = 'transform 0.3s ease';
                }
            });

            carte.addEventListener('mouseleave', function () {
                var icone = carte.querySelector('.resume-icone i');
                if (icone) {
                    icone.style.transform = 'scale(1)';
                }
            });
        });
    }

    // ============================================
    // 8. DÉMARRAGE
    // ============================================
    initialiser();

})();