/**
 * =============================================
 * SCRIPT DE LA PAGE D'ACCUEIL ENRICHIE
 * Plateforme : Location Maisons
 * Auteur : Baraka Ntwali
 * Version : 2.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Header : ombre au scroll
 * 2. Compteurs animés (section chiffres clés)
 * 3. Apparition animée des sections au scroll
 * 4. Animation des cartes (staggered)
 * 5. Animation hero (parallaxe léger)
 * 6. Survol des cartes
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var header = document.querySelector('.accueil-header');
    var chiffresValeurs = document.querySelectorAll('.chiffre-valeur[data-cible]');
    var cartesAnnonces = document.querySelectorAll('.carte-annonce');
    var cartesTop = document.querySelectorAll('.carte-top-maison');
    var cartesVilles = document.querySelectorAll('.carte-ville');
    var cartesAvis = document.querySelectorAll('.carte-avis');
    var etapesCards = document.querySelectorAll('.etape-card');
    var heroSection = document.querySelector('.hero');
    var sectionsAnimees = document.querySelectorAll(
        '.section-annonces, .section-top-maisons, .section-villes, .section-avis, .section-fonctionnement, .section-confiance'
    );
    var chiffresSection = document.querySelector('.section-chiffres');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserHeaderScroll();
        initialiserCompteursChiffres();
        initialiserApparitionSections();
        initialiserAnimationCartes();
        initialiserSurvolCartes();
        initialiserAnimationHero();
    }

    // ============================================
    // 3. HEADER SCROLL
    // ============================================

    function initialiserHeaderScroll() {
        if (!header) return;

        var scrollTimeout;
        window.addEventListener('scroll', function () {
            if (scrollTimeout) return;
            scrollTimeout = setTimeout(function () {
                scrollTimeout = null;
                if (window.scrollY > 10) {
                    header.style.boxShadow = '0 4px 12px rgba(0,0,0,0.08)';
                    header.style.transition = 'box-shadow 0.3s ease';
                } else {
                    header.style.boxShadow = '0 1px 3px rgba(0,0,0,0.04)';
                }
            }, 50);
        });
    }

    // ============================================
    // 4. COMPTEURS CHIFFRES CLÉS
    // ============================================

    function initialiserCompteursChiffres() {
        if (!chiffresValeurs.length) return;

        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (entry.isIntersecting) {
                        animerCompteur(entry.target);
                        observer.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.5 });

            chiffresValeurs.forEach(function (valeur) {
                observer.observe(valeur);
            });
        } else {
            // Fallback : animer directement
            chiffresValeurs.forEach(function (valeur) {
                animerCompteur(valeur);
            });
        }
    }

    function animerCompteur(element) {
        var texteOriginal = element.textContent.trim();
        var valeurCible = parseInt(element.getAttribute('data-cible'), 10);

        if (isNaN(valeurCible) || valeurCible === 0) return;

        var suffixe = '';
        // Détecter si c'est une note (décimale)
        if (texteOriginal.indexOf('.') !== -1) {
            // Note moyenne : déjà affichée, ne pas animer
            return;
        }

        var valeurActuelle = 0;
        var duree = 1200;
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

            element.textContent = Math.round(valeurActuelle) + suffixe;
        }, intervalle);
    }

    // ============================================
    // 5. APPARITION DES SECTIONS AU SCROLL
    // ============================================

    function initialiserApparitionSections() {
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
        }, { threshold: 0.08, rootMargin: '0px 0px -40px 0px' });

        sectionsAnimees.forEach(function (section) {
            section.style.opacity = '0';
            section.style.transform = 'translateY(30px)';
            section.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
            observer.observe(section);
        });
    }

    // ============================================
    // 6. ANIMATION STAGGERED DES CARTES
    // ============================================

    function initialiserAnimationCartes() {
        animerGroupeCartes(cartesAnnonces, 100);
        animerGroupeCartes(cartesTop, 120);
        animerGroupeCartes(cartesVilles, 80);
        animerGroupeCartes(cartesAvis, 100);
        animerGroupeCartes(etapesCards, 150);
    }

    function animerGroupeCartes(cartes, delai) {
        if (!cartes.length) return;

        if ('IntersectionObserver' in window) {
            var observer = new IntersectionObserver(function (entries) {
                entries.forEach(function (entry) {
                    if (entry.isIntersecting) {
                        var carte = entry.target;
                        var index = Array.from(cartes).indexOf(carte);
                        carte.style.opacity = '0';
                        carte.style.transform = 'translateY(30px)';
                        carte.style.transition = 'opacity 0.5s ease, transform 0.5s ease';

                        setTimeout(function () {
                            carte.style.opacity = '1';
                            carte.style.transform = 'translateY(0)';
                        }, delai * (index + 1));

                        observer.unobserve(carte);
                    }
                });
            }, { threshold: 0.1 });

            cartes.forEach(function (carte) {
                observer.observe(carte);
            });
        }
    }

    // ============================================
    // 7. SURVOL CARTES
    // ============================================

    function initialiserSurvolCartes() {
        // Cartes annonces
        cartesAnnonces.forEach(function (carte) {
            ajouterSurvolImage(carte, '.carte-annonce-image img');
        });

        // Cartes top maisons
        cartesTop.forEach(function (carte) {
            ajouterSurvolImage(carte, '.carte-top-image img');
        });
    }

    function ajouterSurvolImage(carte, selecteur) {
        carte.addEventListener('mouseenter', function () {
            var img = carte.querySelector(selecteur);
            if (img) {
                img.style.transform = 'scale(1.08)';
                img.style.transition = 'transform 0.5s ease';
            }
        });

        carte.addEventListener('mouseleave', function () {
            var img = carte.querySelector(selecteur);
            if (img) {
                img.style.transform = 'scale(1)';
            }
        });
    }

    // ============================================
    // 8. ANIMATION HERO (PARALLAXE)
    // ============================================

    function initialiserAnimationHero() {
        if (!heroSection) return;

        var scrollTimeout;
        window.addEventListener('scroll', function () {
            if (scrollTimeout) return;
            scrollTimeout = setTimeout(function () {
                scrollTimeout = null;
                var scrollY = window.scrollY;
                if (scrollY < 600) {
                    heroSection.style.backgroundPosition = 'center ' + (scrollY * 0.3) + 'px';
                }
            }, 20);
        });
    }

    // ============================================
    // 9. DÉMARRAGE
    // ============================================
    initialiser();

})();