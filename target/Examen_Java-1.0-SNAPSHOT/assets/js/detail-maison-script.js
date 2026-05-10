/**
 * =============================================
 * SCRIPT DE LA FICHE DÉTAIL D'UNE MAISON
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 1.0
 * =============================================
 * 
 * Fonctionnalités :
 * 1. Changement de photo principale au clic sur les miniatures
 * 2. Validation des dates de réservation
 * 3. Calcul du prix estimé en temps réel
 * 4. Gestion du bouton favori
 * 5. Partager la maison
 * 6. Animation galerie
 */

(function () {
    'use strict';

    // ============================================
    // 1. RÉFÉRENCES AUX ÉLÉMENTS
    // ============================================

    var photoPrincipale = document.getElementById('photoPrincipale');
    var miniatures = document.querySelectorAll('.miniature:not(.miniature-plus)');
    var champDateDebut = document.querySelector('input[name="dateDebut"]');
    var champDateFin = document.querySelector('input[name="dateFin"]');
    var champVoyageurs = document.querySelector('select[name="nombrePersonnes"]');
    var prixNuit = document.querySelector('.prix-valeur-detail');
    var boutonFavori = document.querySelector('.bouton-favori-detail');
    var boutonPartager = document.querySelector('.bouton-partager-detail');
    var boutonReserver = document.querySelector('.bouton-reserver');
    var galeriePrincipale = document.querySelector('.galerie-principale');

    // ============================================
    // 2. INITIALISATION
    // ============================================

    function initialiser() {
        initialiserGaleriePhotos();
        initialiserValidationDates();
   
        initialiserBoutonPartager();
        initialiserAnimationGalerie();
        initialiserDateMinimale();
    }

    // ============================================
    // 3. GALERIE PHOTOS
    // ============================================

    /**
     * Change la photo principale au clic sur une miniature.
     */
    function initialiserGaleriePhotos() {
        if (!miniatures.length || !photoPrincipale) return;

        miniatures.forEach(function (miniature) {
            miniature.addEventListener('click', function () {
                // Récupérer le chemin de l'image
                var img = miniature.querySelector('img');
                if (!img) return;

                var nouveauSrc = img.getAttribute('src');

                // Animation de transition
                photoPrincipale.style.opacity = '0';
                photoPrincipale.style.transition = 'opacity 0.2s ease';

                setTimeout(function () {
                    photoPrincipale.setAttribute('src', nouveauSrc);
                    photoPrincipale.style.opacity = '1';
                }, 200);

                // Mettre à jour la miniature active
                miniatures.forEach(function (m) {
                    m.classList.remove('miniature-active');
                });
                miniature.classList.add('miniature-active');
            });
        });
    }

    // ============================================
    // 4. VALIDATION DATES RÉSERVATION
    // ============================================

    /**
     * Valide les dates et calcule le prix estimé.
     */
    function initialiserValidationDates() {
        if (!champDateDebut || !champDateFin) return;

        // Mettre à jour la date minimale de fin
        champDateDebut.addEventListener('change', function () {
            if (champDateDebut.value) {
                champDateFin.setAttribute('min', champDateDebut.value);

                if (champDateFin.value && champDateFin.value < champDateDebut.value) {
                    champDateFin.value = '';
                }
            }
            calculerPrixEstime();
        });

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
            calculerPrixEstime();
        });

        if (champVoyageurs) {
            champVoyageurs.addEventListener('change', calculerPrixEstime);
        }
    }

    /**
     * Calcule et affiche le prix total estimé.
     */
    function calculerPrixEstime() {
        if (!champDateDebut || !champDateFin || !prixNuit) return;

        var debut = champDateDebut.value;
        var fin = champDateFin.value;

        if (!debut || !fin) return;

        var dateDebut = new Date(debut);
        var dateFin = new Date(fin);
        var difference = dateFin.getTime() - dateDebut.getTime();
        var nombreNuits = Math.ceil(difference / (1000 * 3600 * 24));

        if (nombreNuits <= 0) return;

        var prixParNuit = parseFloat(prixNuit.textContent.replace('€', '').replace(/\s/g, ''));
        var total = prixParNuit * nombreNuits;

        // Afficher le total sous le prix
        var totalElement = document.getElementById('prixTotal');
        if (!totalElement) {
            totalElement = document.createElement('div');
            totalElement.id = 'prixTotal';
            totalElement.style.cssText = 
                'text-align:center;margin-top:8px;padding-top:12px;' +
                'border-top:1px solid #f1f5f9;font-size:14px;color:#64748b;';
            prixNuit.parentElement.appendChild(totalElement);
        }
        totalElement.innerHTML = 
            '<span style="font-size:18px;font-weight:700;color:#0f172a;">' + 
            total.toFixed(0) + ' €</span> pour ' + nombreNuits + ' nuit' + 
            (nombreNuits > 1 ? 's' : '');
    }

    /**
     * Définit la date minimale (aujourd'hui).
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
    // 5. BOUTON FAVORI
    // ============================================

    /**
     * Gère le clic sur le bouton favori.
     */
    

    // ============================================
    // 6. BOUTON PARTAGER
    // ============================================

    /**
     * Gère le partage de la maison.
     */
    function initialiserBoutonPartager() {
        if (!boutonPartager) return;

        boutonPartager.addEventListener('click', function (e) {
            e.preventDefault();

            var url = window.location.href;

            // Essayer l'API Web Share
            if (navigator.share) {
                navigator.share({
                    title: document.title,
                    url: url
                }).catch(function () {
                    copierDansPressePapier(url);
                });
            } else {
                copierDansPressePapier(url);
            }
        });
    }

    /**
     * Copie l'URL dans le presse-papier.
     */
    function copierDansPressePapier(texte) {
        if (navigator.clipboard && navigator.clipboard.writeText) {
            navigator.clipboard.writeText(texte).then(function () {
                afficherToast('Lien copié dans le presse-papier !');
            }).catch(function () {
                // Fallback
                var input = document.createElement('input');
                input.value = texte;
                document.body.appendChild(input);
                input.select();
                document.execCommand('copy');
                document.body.removeChild(input);
                afficherToast('Lien copié !');
            });
        } else {
            // Fallback pour navigateurs anciens
            var input = document.createElement('input');
            input.value = texte;
            document.body.appendChild(input);
            input.select();
            document.execCommand('copy');
            document.body.removeChild(input);
            afficherToast('Lien copié !');
        }
    }

    /**
     * Affiche un toast temporaire.
     */
    function afficherToast(message) {
        // Supprimer un toast existant
        var ancien = document.querySelector('.toast-message');
        if (ancien) ancien.remove();

        var toast = document.createElement('div');
        toast.className = 'toast-message';
        toast.textContent = message;
        toast.style.cssText = 
            'position:fixed;bottom:30px;left:50%;transform:translateX(-50%);' +
            'background:#0f172a;color:#ffffff;padding:12px 24px;border-radius:10px;' +
            'font-size:14px;font-weight:500;z-index:9999;' +
            'animation:toastIn 0.3s ease, toastOut 0.3s ease 2.5s forwards;' +
            'box-shadow:0 8px 20px rgba(0,0,0,0.2);';

        document.body.appendChild(toast);

        setTimeout(function () {
            if (toast.parentNode) toast.remove();
        }, 3000);
    }

    // ============================================
    // 7. ANIMATION GALERIE
    // ============================================

    /**
     * Effet de survol sur la photo principale.
     */
    function initialiserAnimationGalerie() {
        if (!galeriePrincipale) return;

        galeriePrincipale.addEventListener('mousemove', function (e) {
            var img = galeriePrincipale.querySelector('img');
            if (!img || window.innerWidth < 768) return;

            var rect = galeriePrincipale.getBoundingClientRect();
            var x = e.clientX - rect.left;
            var y = e.clientY - rect.top;

            var xPercent = (x / rect.width) * 100;
            var yPercent = (y / rect.height) * 100;

            img.style.transformOrigin = xPercent + '% ' + yPercent + '%';
            img.style.transform = 'scale(1.5)';
            img.style.transition = 'transform 0.1s ease';
        });

        galeriePrincipale.addEventListener('mouseleave', function () {
            var img = galeriePrincipale.querySelector('img');
            if (img) {
                img.style.transform = 'scale(1)';
                img.style.transformOrigin = 'center center';
                img.style.transition = 'transform 0.4s ease';
            }
        });
    }

    // ============================================
    // 8. UTILITAIRES
    // ============================================

    function marquerErreur(champ) {
        champ.style.borderColor = '#ef4444';
        champ.style.background = '#fef2f2';
    }

    function effacerErreur(champ) {
        champ.style.borderColor = '';
        champ.style.background = '';
    }

    function afficherMessageChamp(champ, message) {
        supprimerMessageChamp(champ);
        var msg = document.createElement('span');
        msg.className = 'message-champ-erreur';
        msg.textContent = message;
        msg.style.cssText = 
            'display:block;color:#ef4444;font-size:11px;margin-top:4px;animation:fadeIn 0.3s ease;';
        champ.parentElement.appendChild(msg);
    }

    function supprimerMessageChamp(champ) {
        var parent = champ.parentElement;
        if (parent) {
            var ancien = parent.querySelector('.message-champ-erreur');
            if (ancien) ancien.remove();
        }
    }

    // ============================================
    // 9. STYLES INJECTÉS
    // ============================================

    function injecterStyles() {
        if (document.getElementById('keyframes-detail')) return;

        var style = document.createElement('style');
        style.id = 'keyframes-detail';
        style.textContent =
            '@keyframes toastIn {' +
            '  from { opacity: 0; transform: translateX(-50%) translateY(20px); }' +
            '  to   { opacity: 1; transform: translateX(-50%) translateY(0); }' +
            '}' +
            '@keyframes toastOut {' +
            '  from { opacity: 1; transform: translateX(-50%) translateY(0); }' +
            '  to   { opacity: 0; transform: translateX(-50%) translateY(20px); }' +
            '}' +
            '@keyframes fadeIn {' +
            '  from { opacity: 0; transform: translateY(-4px); }' +
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