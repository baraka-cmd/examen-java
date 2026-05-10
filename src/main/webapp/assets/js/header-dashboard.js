/**
 * =============================================
 * SCRIPT DU HEADER DU DASHBOARD
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 2.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Dropdown menu profil (clic + Échap + clic extérieur)
 * 2. Menu mobile hamburger (animation + clic extérieur)
 * 3. Fermeture automatique au redimensionnement
 * 4. Accessibilité complète (ARIA, focus, clavier)
 * 5. Détection lien actif automatique
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS DU DOM
    // ============================================

    var boutonProfil = document.getElementById('boutonProfil');
    var dropdownProfil = document.getElementById('dropdownProfil');
    var boutonMenuMobile = document.getElementById('boutonMenuMobile');
    var headerNav = document.querySelector('.header-nav');
    var header = document.querySelector('.header-dashboard');
    var menuMobile = null; // Sera créé dynamiquement

    // ============================================
    // 2. INITIALISATION
    // ============================================

    /**
     * Point d'entrée principal.
     */
    function initialiser() {
        if (!header) return;

        initialiserDropdownProfil();
        initialiserMenuMobile();
        initialiserClicExterieur();
        initialiserRedimensionnement();
        initialiserRaccourciClavier();
        initialiserLienActif();
        supprimerAncienMenuMobile();
    }

    // ============================================
    // 3. DROPDOWN MENU PROFIL
    // ============================================

    /**
     * Gère l'ouverture/fermeture du dropdown profil.
     */
    function initialiserDropdownProfil() {
        if (!boutonProfil || !dropdownProfil) return;

        // Clic sur le bouton profil
        boutonProfil.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            basculerDropdown();
        });

        // Empêcher la propagation depuis l'intérieur du dropdown
        dropdownProfil.addEventListener('click', function (e) {
            e.stopPropagation();
        });

        // Touche Échap pour fermer
        dropdownProfil.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                e.preventDefault();
                fermerDropdown();
                boutonProfil.focus();
            }
        });

        // Fermer quand on tab hors du dropdown (dernier élément)
        var itemsDropdown = dropdownProfil.querySelectorAll('a, button');
        if (itemsDropdown.length > 0) {
            var dernierItem = itemsDropdown[itemsDropdown.length - 1];
            dernierItem.addEventListener('keydown', function (e) {
                if (e.key === 'Tab' && !e.shiftKey) {
                    fermerDropdown();
                }
            });

            // Premier élément + Shift+Tab ferme aussi
            var premierItem = itemsDropdown[0];
            premierItem.addEventListener('keydown', function (e) {
                if (e.key === 'Tab' && e.shiftKey) {
                    fermerDropdown();
                }
            });
        }
    }

    /**
     * Bascule l'état du dropdown.
     */
    function basculerDropdown() {
        if (!dropdownProfil || !boutonProfil) return;

        if (dropdownProfil.classList.contains('visible')) {
            fermerDropdown();
        } else {
            // Fermer le menu mobile si ouvert
            fermerMenuMobile();
            ouvrirDropdown();
        }
    }

    /**
     * Ouvre le dropdown profil.
     */
    function ouvrirDropdown() {
        if (!dropdownProfil || !boutonProfil) return;

        dropdownProfil.classList.add('visible');
        boutonProfil.classList.add('ouvert');
        boutonProfil.setAttribute('aria-expanded', 'true');

        // Focus sur le premier lien du dropdown après ouverture
        setTimeout(function () {
            var premierLien = dropdownProfil.querySelector('a, button');
            if (premierLien) {
                premierLien.focus();
            }
        }, 100);
    }

    /**
     * Ferme le dropdown profil.
     */
    function fermerDropdown() {
        if (!dropdownProfil || !boutonProfil) return;

        dropdownProfil.classList.remove('visible');
        boutonProfil.classList.remove('ouvert');
        boutonProfil.setAttribute('aria-expanded', 'false');
    }

    // ============================================
    // 4. MENU MOBILE (HAMBURGER)
    // ============================================

    /**
     * Gère le bouton hamburger pour le menu mobile.
     */
    function initialiserMenuMobile() {
        if (!boutonMenuMobile || !headerNav) return;

        boutonMenuMobile.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            basculerMenuMobile();
        });
    }

    /**
     * Bascule l'état du menu mobile.
     */
    function basculerMenuMobile() {
        if (!boutonMenuMobile) return;

        if (boutonMenuMobile.classList.contains('ouvert')) {
            fermerMenuMobile();
        } else {
            // Fermer le dropdown si ouvert
            fermerDropdown();
            ouvrirMenuMobile();
        }
    }

    /**
     * Ouvre le menu mobile.
     */
    function ouvrirMenuMobile() {
        if (!boutonMenuMobile) return;

        boutonMenuMobile.classList.add('ouvert');
        boutonMenuMobile.setAttribute('aria-expanded', 'true');

        // Créer le menu mobile s'il n'existe pas encore dans le DOM
        menuMobile = document.querySelector('.menu-mobile');
        if (!menuMobile) {
            menuMobile = creerMenuMobile();
        }

        if (menuMobile) {
            menuMobile.classList.add('ouvert');
            menuMobile.setAttribute('aria-hidden', 'false');
        }
    }

    /**
     * Ferme le menu mobile.
     */
    function fermerMenuMobile() {
        if (boutonMenuMobile) {
            boutonMenuMobile.classList.remove('ouvert');
            boutonMenuMobile.setAttribute('aria-expanded', 'false');
        }

        menuMobile = document.querySelector('.menu-mobile');
        if (menuMobile) {
            menuMobile.classList.remove('ouvert');
            menuMobile.setAttribute('aria-hidden', 'true');
        }
    }

    /**
     * Crée dynamiquement le menu mobile à partir de la navigation desktop.
     * @returns {HTMLElement} l'élément menu mobile créé
     */
    function creerMenuMobile() {
        if (!headerNav || !header) return null;

        var menu = document.createElement('div');
        menu.className = 'menu-mobile';
        menu.setAttribute('aria-hidden', 'true');
        menu.setAttribute('role', 'navigation');
        menu.setAttribute('aria-label', 'Menu principal');

        // Cloner le contenu de la navigation
        var navClone = headerNav.cloneNode(true);
        menu.appendChild(navClone);

        // Insérer après le header
        header.after(menu);

        // Ajouter un écouteur pour fermer quand on clique sur un lien
        var liens = menu.querySelectorAll('a');
        for (var i = 0; i < liens.length; i++) {
            liens[i].addEventListener('click', function () {
                // Petit délai pour laisser le lien s'exécuter
                setTimeout(fermerMenuMobile, 150);
            });
        }

        return menu;
    }

    /**
     * Supprime un ancien menu mobile s'il existe déjà dans le DOM.
     */
    function supprimerAncienMenuMobile() {
        var ancien = document.querySelector('.menu-mobile');
        if (ancien) {
            ancien.parentNode.removeChild(ancien);
        }
    }

    // ============================================
    // 5. GESTION DU REDIMENSIONNEMENT
    // ============================================

    /**
     * Ferme le menu mobile si l'écran dépasse le breakpoint desktop.
     */
    function initialiserRedimensionnement() {
        var redimensionnementEnCours = false;

        window.addEventListener('resize', function () {
            if (!redimensionnementEnCours) {
                window.requestAnimationFrame(function () {
                    if (window.innerWidth >= 900) {
                        fermerMenuMobile();
                        // Supprimer le menu mobile du DOM sur desktop
                        var menu = document.querySelector('.menu-mobile');
                        if (menu) {
                            menu.parentNode.removeChild(menu);
                            menuMobile = null;
                        }
                    }
                    redimensionnementEnCours = false;
                });
                redimensionnementEnCours = true;
            }
        });
    }

    // ============================================
    // 6. FERMETURE AU CLIC EXTÉRIEUR
    // ============================================

    /**
     * Ferme les menus quand on clique ailleurs sur la page.
     */
    function initialiserClicExterieur() {
        document.addEventListener('click', function (e) {
            // Fermer le dropdown profil
            if (dropdownProfil && dropdownProfil.classList.contains('visible')) {
                if (boutonProfil && !boutonProfil.contains(e.target) && 
                    !dropdownProfil.contains(e.target)) {
                    fermerDropdown();
                }
            }

            // Fermer le menu mobile
            if (boutonMenuMobile && boutonMenuMobile.classList.contains('ouvert')) {
                var menuActuel = document.querySelector('.menu-mobile');
                if (!boutonMenuMobile.contains(e.target) && 
                    menuActuel && !menuActuel.contains(e.target)) {
                    fermerMenuMobile();
                }
            }
        });

        // Fermer au scroll (optionnel, utile sur mobile)
        window.addEventListener('scroll', function () {
            if (window.innerWidth < 900) {
                // On ne ferme pas au scroll sur mobile pour ne pas gêner
            }
        }, { passive: true });
    }

    // ============================================
    // 7. RACCOURCIS CLAVIER GLOBAUX
    // ============================================

    /**
     * Touche Échap pour fermer tous les menus ouverts.
     */
    function initialiserRaccourciClavier() {
        document.addEventListener('keydown', function (e) {
            if (e.key === 'Escape') {
                var dropdownOuvert = dropdownProfil && 
                    dropdownProfil.classList.contains('visible');
                var menuOuvert = boutonMenuMobile && 
                    boutonMenuMobile.classList.contains('ouvert');

                if (dropdownOuvert) {
                    e.preventDefault();
                    fermerDropdown();
                    if (boutonProfil) boutonProfil.focus();
                } else if (menuOuvert) {
                    e.preventDefault();
                    fermerMenuMobile();
                    if (boutonMenuMobile) boutonMenuMobile.focus();
                }
            }
        });
    }

    // ============================================
    // 8. DÉTECTION LIEN ACTIF
    // ============================================

    /**
     * Ajoute la classe 'actif' au lien correspondant à l'URL courante.
     */
    function initialiserLienActif() {
        var urlCourante = window.location.pathname;
        var tousLesLiens = document.querySelectorAll('.nav-lien');

        for (var i = 0; i < tousLesLiens.length; i++) {
            var lien = tousLesLiens[i];
            var href = lien.getAttribute('href');

            if (href && urlCourante.indexOf(href.replace(window.location.origin, '')) !== -1) {
                // Ne pas marquer le dashboard comme actif sur toutes les pages
                if (href === '/Examen_Java/dashboard' && urlCourante !== '/Examen_Java/dashboard') {
                    continue;
                }
                lien.classList.add('actif');
            }
        }
    }

    // ============================================
    // 9. DÉMARRAGE
    // ============================================
    initialiser();

})();