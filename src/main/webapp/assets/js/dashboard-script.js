/**
 * =============================================
 * SCRIPT DES DASHBOARDS (LOCATAIRE & PROPRIÉTAIRE)
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 2.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Animation compteur des statistiques
 * 2. Animation apparition au scroll
 * 3. Survol cartes réservation/action
 * 4. Validation formulaire recherche rapide
 * 5. Gestion clic cartes d'action
 * 6. Effet hover favoris
 * 7. Animation carte bienvenue
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS DU DOM
    // ============================================

    var cartesStatistiques = document.querySelectorAll('.carte-statistique');
    var cartesReservation = document.querySelectorAll('.carte-reservation');
    var cartesAction = document.querySelectorAll('.carte-action');
    var formulaireRecherche = document.querySelector('.formulaire-recherche-rapide');
    var favorisItems = document.querySelectorAll('.favori-item');
    var carteBienvenue = document.querySelector('.carte-bienvenue');
    var sectionsAnimees = document.querySelectorAll('.section-reservations, .carte-sidebar, .section-actions-rapides');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserAnimationCompteurs();
        initialiserAnimationApparition();
        initialiserSurvolCartes();
        initialiserCartesAction();
        initialiserFormulaireRecherche();
        initialiserFavorisHover();
        initialiserAnimationBienvenue();
        initialiserActualisationPage();
        injecterStylesAnimation();
    }

    // ============================================
    // 3. ANIMATION COMPTEURS STATISTIQUES
    // ============================================

    /**
     * Anime les valeurs statistiques de 0 à leur valeur finale.
     */
    function initialiserAnimationCompteurs() {
        if (!cartesStatistiques.length) return;

        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (entry.isIntersecting) {
                        var valeurElement = entry.target.querySelector('.statistique-valeur');
                        if (valeurElement) {
                            animerCompteur(valeurElement);
                        }
                        observer.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.5 });

            cartesStatistiques.forEach(function (carte) {
                observer.observe(carte);
            });
        } else {
            cartesStatistiques.forEach(function (carte) {
                var valeurElement = carte.querySelector('.statistique-valeur');
                if (valeurElement) {
                    animerCompteur(valeurElement);
                }
            });
        }
    }

    /**
     * Anime un compteur de 0 à la valeur cible.
     */
    function animerCompteur(element) {
        var texteOriginal = element.textContent.trim();
        // Détecter si c'est un nombre, un pourcentage ou un montant
        var suffixe = '';
        var valeurCible = 0;

        if (texteOriginal.indexOf('%') !== -1) {
            valeurCible = parseInt(texteOriginal.replace('%', ''), 10);
            suffixe = '%';
        } else if (texteOriginal.indexOf('€') !== -1) {
            valeurCible = parseInt(texteOriginal.replace('€', '').replace(/\s/g, ''), 10);
            suffixe = ' €';
        } else {
            valeurCible = parseInt(texteOriginal, 10);
        }

        if (isNaN(valeurCible) || valeurCible === 0) return;

        var valeurActuelle = 0;
        var duree = 800;
        var pas = Math.ceil(valeurCible / (duree / 16));
        var intervalle;

        element.textContent = '0' + suffixe;

        intervalle = setInterval(function () {
            valeurActuelle += pas;
            if (valeurActuelle >= valeurCible) {
                valeurActuelle = valeurCible;
                clearInterval(intervalle);
            }
            element.textContent = valeurActuelle + suffixe;
        }, 16);
    }

    // ============================================
    // 4. ANIMATION APPARITION AU SCROLL
    // ============================================

    /**
     * Les sections apparaissent en fondu quand on scrolle.
     */
    function initialiserAnimationApparition() {
        if (!sectionsAnimees.length) return;
        if (!('IntersectionObserver' in window)) return;

        var observer = new IntersectionObserver(function (entries) {
            entries.forEach(function (entry) {
                if (entry.isIntersecting) {
                    entry.target.style.opacity = '1';
                    entry.target.style.transform = 'translateY(0)';
                    observer.unobserve(entry.target);
                }
            });
        }, { threshold: 0.1, rootMargin: '0px 0px -30px 0px' });

        sectionsAnimees.forEach(function (section) {
            section.style.opacity = '0';
            section.style.transform = 'translateY(20px)';
            section.style.transition = 'opacity 0.5s ease, transform 0.5s ease';
            observer.observe(section);
        });
    }

    // ============================================
    // 5. SURVOL CARTES RÉSERVATION
    // ============================================

    /**
     * Effet visuel au survol des cartes de réservation.
     */
    function initialiserSurvolCartes() {
        if (!cartesReservation.length) return;

        cartesReservation.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                var image = carte.querySelector('.reservation-image img');
                if (image) {
                    image.style.transform = 'scale(1.05)';
                    image.style.transition = 'transform 0.4s ease';
                }
            });

            carte.addEventListener('mouseleave', function () {
                var image = carte.querySelector('.reservation-image img');
                if (image) {
                    image.style.transform = 'scale(1)';
                }
            });
        });
    }

    // ============================================
    // 6. CARTES D'ACTION (PROPRIÉTAIRE)
    // ============================================

    /**
     * Ajoute un effet de clic et de chargement sur les cartes d'action.
     */
    function initialiserCartesAction() {
        if (!cartesAction.length) return;

        cartesAction.forEach(function (carte) {
            carte.addEventListener('click', function (e) {
                // Effet de pulse au clic
                carte.style.transform = 'scale(0.97)';
                setTimeout(function () {
                    carte.style.transform = '';
                }, 150);

                // La redirection se fait via le href naturellement
                // Ajouter un léger délai pour l'effet visuel si souhaité
            });

            // Accessibilité clavier
            carte.addEventListener('keydown', function (e) {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    carte.click();
                }
            });
        });
    }

    // ============================================
    // 7. FORMULAIRE RECHERCHE RAPIDE
    // ============================================

    /**
     * Valide le formulaire de recherche rapide.
     */
    function initialiserFormulaireRecherche() {
        if (!formulaireRecherche) return;

        formulaireRecherche.addEventListener('submit', function (e) {
            var ville = document.getElementById('villeRecherche');
            var capacite = document.getElementById('capaciteRecherche');

            var villeRemplie = ville && ville.value.trim() !== '';
            var capaciteRemplie = capacite && capacite.value.trim() !== '' && parseInt(capacite.value, 10) > 0;

            if (!villeRemplie && !capaciteRemplie) {
                e.preventDefault();

                if (ville) secouerElement(ville);
                if (capacite) secouerElement(capacite);

                var messageErreur = formulaireRecherche.querySelector('.message-erreur-recherche');
                if (!messageErreur) {
                    messageErreur = document.createElement('span');
                    messageErreur.className = 'message-erreur-recherche';
                    messageErreur.style.cssText =
                        'display:block;color:#ef4444;font-size:12px;' +
                        'margin-top:6px;text-align:center;animation:fadeIn 0.3s ease;';
                    messageErreur.textContent = 'Remplissez au moins un champ.';
                    formulaireRecherche.appendChild(messageErreur);

                    setTimeout(function () {
                        if (messageErreur && messageErreur.parentNode) {
                            messageErreur.remove();
                        }
                    }, 3000);
                }
            } else {
                var bouton = formulaireRecherche.querySelector('button[type="submit"]');
                if (bouton) {
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Recherche...';
                    bouton.disabled = true;
                }
            }
        });

        // Effacer erreur quand l'utilisateur tape
        formulaireRecherche.addEventListener('input', function () {
            var messageErreur = formulaireRecherche.querySelector('.message-erreur-recherche');
            if (messageErreur) {
                messageErreur.remove();
            }
        });
    }

    /**
     * Effet de secousse sur un élément.
     */
    function secouerElement(element) {
        element.style.animation = 'none';
        element.offsetHeight;
        element.style.animation = 'secousse 0.5s ease';
        element.style.borderColor = '#ef4444';

        setTimeout(function () {
            element.style.animation = '';
            element.style.borderColor = '';
        }, 500);
    }

    // ============================================
    // 8. EFFET HOVER FAVORIS
    // ============================================

    /**
     * Zoom image au survol des items favoris.
     */
    function initialiserFavorisHover() {
        if (!favorisItems.length) return;

        favorisItems.forEach(function (item) {
            item.addEventListener('mouseenter', function () {
                var image = item.querySelector('.favori-image img');
                if (image) {
                    image.style.transform = 'scale(1.08)';
                    image.style.transition = 'transform 0.35s ease';
                }
            });

            item.addEventListener('mouseleave', function () {
                var image = item.querySelector('.favori-image img');
                if (image) {
                    image.style.transform = 'scale(1)';
                }
            });
        });
    }

    // ============================================
    // 9. ANIMATION CARTE BIENVENUE
    // ============================================

    /**
     * Effet subtil sur la carte de bienvenue au survol.
     */
    function initialiserAnimationBienvenue() {
        if (!carteBienvenue) return;

        carteBienvenue.addEventListener('mouseenter', function () {
            var illustration = carteBienvenue.querySelector('.bienvenue-illustration i');
            if (illustration) {
                illustration.style.transform = 'rotate(-10deg) scale(1.1)';
                illustration.style.transition = 'transform 0.4s ease';
            }
        });

        carteBienvenue.addEventListener('mouseleave', function () {
            var illustration = carteBienvenue.querySelector('.bienvenue-illustration i');
            if (illustration) {
                illustration.style.transform = 'rotate(0deg) scale(1)';
            }
        });
    }

    // ============================================
    // 10. RAFRAÎCHISSEMENT AU RETOUR
    // ============================================

    /**
     * Gère le comportement au retour sur la page.
     */
    function initialiserActualisationPage() {
        window.addEventListener('pageshow', function (event) {
            if (event.persisted) {
                var valeurElements = document.querySelectorAll('.statistique-valeur');
                valeurElements.forEach(function (el) {
                    var texte = el.textContent.trim();
                    el.textContent = '0' + (texte.indexOf('€') !== -1 ? ' €' : texte.indexOf('%') !== -1 ? '%' : '');
                    animerCompteur(el);
                });
            }
        });
    }

    // ============================================
    // 11. INJECTION STYLES D'ANIMATION
    // ============================================

    /**
     * Injecte les keyframes manquantes dans le DOM.
     */
    function injecterStylesAnimation() {
        if (document.getElementById('keyframes-dashboard')) return;

        var style = document.createElement('style');
        style.id = 'keyframes-dashboard';
        style.textContent =
            '@keyframes secousse {' +
            '  0%, 100% { transform: translateX(0); }' +
            '  10%  { transform: translateX(-6px); }' +
            '  30%  { transform: translateX(6px); }' +
            '  50%  { transform: translateX(-4px); }' +
            '  70%  { transform: translateX(4px); }' +
            '  90%  { transform: translateX(-2px); }' +
            '}' +
            '@keyframes fadeIn {' +
            '  from { opacity: 0; transform: translateY(-4px); }' +
            '  to   { opacity: 1; transform: translateY(0); }' +
            '}' +
            '.carte-action:active {' +
            '  transform: scale(0.97) !important;' +
            '  transition: transform 0.1s ease !important;' +
            '}';
        document.head.appendChild(style);
    }

    // ============================================
    // 12. DÉMARRAGE
    // ============================================
    initialiser();

})();