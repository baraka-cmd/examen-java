/**
 * =============================================
 * SCRIPT DES PAGES MAISON (CRUD PROPRIÉTAIRE)
 * Plateforme : Location Maisons
 * Auteur : BARAKA
 * Version : 2.1
 * =============================================
 * 
 * Fonctionnalités :
 * - Upload photos avec accumulation (ajout + modification)
 * - Suppression visuelle des photos existantes (modification)
 * - Validation formulaire avant soumission
 * - Envoi avec FormData
 * - Survol cartes, compteur caractères, confirmation suppression
 */
(function () {
    'use strict';

    // Fichiers accumulés pour nouvelles photos
    var toutesLesPhotos = [];
    var maxPhotos = 5;

    // Page ajout
    var inputFile = document.getElementById('photos');
    var previewContainer = document.getElementById('previewPhotos');
    
    // Page modification
    var inputFileModifier = document.getElementById('nouvellesPhotosModifier');
    var previewContainerModifier = document.getElementById('previewPhotosModifier');
    var supprimerContainer = document.getElementById('supprimerPhotosContainer');
    var boutonModifier = document.getElementById('boutonModifier');

    var formulaire = document.querySelector('.formulaire-maison');
    var cartesMaison = document.querySelectorAll('.carte-maison');

    // ============================================
    // INITIALISATION
    // ============================================
    function initialiser() {
        if (inputFile && previewContainer) {
            initialiserUploadPhotos(inputFile, previewContainer, 'photos', 'zoneUpload', 'compteurPhotos', 'nbPhotos');
        }
        if (inputFileModifier && previewContainerModifier) {
            initialiserUploadPhotos(inputFileModifier, previewContainerModifier, 'nouvellesPhotos', 'zoneUploadModifier', 'compteurPhotosModifier', 'nbPhotosModifier');
        }
        initialiserFormulaire();
        initialiserSurvolCartes();
        initialiserConfirmationSuppression();
        initialiserCompteurCaracteres();
    }

    // ============================================
    // UPLOAD PHOTOS AVEC ACCUMULATION
    // ============================================
    function initialiserUploadPhotos(input, container, fieldName, zoneId, compteurId, nbId) {
        if (!input || !container) return;

        input.addEventListener('change', function () {
            var fichiers = this.files;
            if (!fichiers || fichiers.length === 0) return;

            var placesRestantes = maxPhotos - toutesLesPhotos.length;

            if (placesRestantes <= 0) {
                alert('Maximum ' + maxPhotos + ' photos atteint.');
                this.value = '';
                return;
            }

            var nombreAAjouter = Math.min(fichiers.length, placesRestantes);

            if (fichiers.length > placesRestantes) {
                alert('Vous ne pouvez ajouter que ' + placesRestantes + ' photo(s). Les autres seront ignorées.');
            }

            for (var i = 0; i < nombreAAjouter; i++) {
                var fichier = fichiers[i];
                if (!fichier.type.match('image.*')) continue;
                if (fichier.size > 10 * 1024 * 1024) {
                    alert('La photo "' + fichier.name + '" dépasse 10 Mo.');
                    continue;
                }
                toutesLesPhotos.push(fichier);
            }

            this.value = '';
            rafraichirPreviews(container);
            mettreAJourCompteur(compteurId, nbId);
            mettreAJourZone(zoneId);
        });
    }

    function rafraichirPreviews(container) {
        container.innerHTML = '';
        for (var i = 0; i < toutesLesPhotos.length; i++) {
            creerPreviewItem(toutesLesPhotos[i], i, container);
        }
    }

    function creerPreviewItem(fichier, index, container) {
        var lecteur = new FileReader();
        lecteur.onload = function (e) {
            var item = document.createElement('div');
            item.className = 'preview-item';
            item.setAttribute('data-index', index);

            var img = document.createElement('img');
            img.src = e.target.result;
            img.alt = fichier.name;

            var boutonSupprimer = document.createElement('button');
            boutonSupprimer.type = 'button';
            boutonSupprimer.className = 'preview-supprimer';
            boutonSupprimer.innerHTML = '<i class="fa-solid fa-xmark"></i>';
            boutonSupprimer.title = 'Supprimer cette photo';
            boutonSupprimer.onclick = function () {
                supprimerNouvellePhoto(index, container);
            };

            var badge = document.createElement('span');
            badge.className = 'preview-badge';
            badge.textContent = index === 0 ? 'Principale' : 'Photo ' + (index + 1);

            var infos = document.createElement('div');
            infos.className = 'preview-infos';
            var nom = document.createElement('span');
            nom.className = 'preview-nom';
            nom.textContent = fichier.name.length > 20 ? fichier.name.substring(0, 17) + '...' : fichier.name;
            var taille = document.createElement('span');
            taille.className = 'preview-taille';
            taille.textContent = formaterTaille(fichier.size);
            infos.appendChild(nom);
            infos.appendChild(taille);

            item.appendChild(img);
            item.appendChild(boutonSupprimer);
            item.appendChild(badge);
            item.appendChild(infos);
            container.appendChild(item);
        };
        lecteur.readAsDataURL(fichier);
    }

    function supprimerNouvellePhoto(index, container) {
        if (index >= 0 && index < toutesLesPhotos.length) {
            toutesLesPhotos.splice(index, 1);
            rafraichirPreviews(container);
            mettreAJourCompteur(
                container === previewContainer ? 'compteurPhotos' : 'compteurPhotosModifier',
                container === previewContainer ? 'nbPhotos' : 'nbPhotosModifier'
            );
            mettreAJourZone(container === previewContainer ? 'zoneUpload' : 'zoneUploadModifier');
        }
    }

    function mettreAJourCompteur(compteurId, nbId) {
        var compteur = document.getElementById(compteurId);
        var nb = document.getElementById(nbId);
        if (compteur && nb) {
            nb.textContent = toutesLesPhotos.length;
            compteur.style.display = toutesLesPhotos.length > 0 ? 'block' : 'none';
        }
    }

    function mettreAJourZone(zoneId) {
        var zone = document.getElementById(zoneId);
        if (zone) {
            if (toutesLesPhotos.length >= maxPhotos) {
                zone.style.opacity = '0.5';
                zone.style.pointerEvents = 'none';
            } else {
                zone.style.opacity = '1';
                zone.style.pointerEvents = 'auto';
            }
        }
    }

    function formaterTaille(octets) {
        if (octets < 1024) return octets + ' o';
        if (octets < 1048576) return (octets / 1024).toFixed(1) + ' Ko';
        return (octets / 1048576).toFixed(1) + ' Mo';
    }

    // ============================================
    // SUPPRESSION PHOTOS EXISTANTES (MODIFICATION)
    // ============================================
    window.marquerSuppression = function (idMedia) {
        var photoElement = document.getElementById('photo-' + idMedia);
        if (!photoElement) return;

        if (confirm('Supprimer cette photo ?')) {
            // Animation de sortie
            photoElement.style.transition = 'opacity 0.3s ease, transform 0.3s ease';
            photoElement.style.opacity = '0';
            photoElement.style.transform = 'scale(0.9)';

            setTimeout(function () {
                photoElement.remove();
                verifierPhotosRestantes();
            }, 300);

            // Ajouter un input caché pour la suppression
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'supprimerPhotos';
            input.value = idMedia;
            if (supprimerContainer) {
                supprimerContainer.appendChild(input);
            }
        }
    };

    function verifierPhotosRestantes() {
        var photosRestantes = document.querySelectorAll('.photo-existante');
        if (photosRestantes.length === 0) {
            var container = document.getElementById('photosExistantes');
            if (container) {
                container.innerHTML = '<p class="aucune-photo">Toutes les photos ont été supprimées.</p>';
            }
        }
    }

    // ============================================
    // FORMULAIRE : ENVOI AVEC FORMDATA
    // ============================================
    function initialiserFormulaire() {
        if (!formulaire) return;

        formulaire.addEventListener('submit', function (e) {
            if (!validerFormulaire()) {
                e.preventDefault();
                return;
            }

            // Déterminer le nom du champ fichier
            var nomChamp = inputFile ? 'photos' : 'nouvellesPhotos';

            if (toutesLesPhotos.length > 0) {
                e.preventDefault();
                soumettreAvecPhotos(nomChamp);
            } else {
                var bouton = formulaire.querySelector('.bouton-publier') || boutonModifier;
                if (bouton) {
                    bouton.disabled = true;
                    bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Enregistrement...';
                }
            }
        });
    }

    function soumettreAvecPhotos(nomChamp) {
        var formData = new FormData(formulaire);
        formData.delete(nomChamp);

        for (var i = 0; i < toutesLesPhotos.length; i++) {
            formData.append(nomChamp, toutesLesPhotos[i]);
        }

        var bouton = formulaire.querySelector('.bouton-publier') || boutonModifier;
        if (bouton) {
            bouton.disabled = true;
            bouton.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Enregistrement...';
        }

        fetch(formulaire.action, {
            method: 'POST',
            body: formData
        })
        .then(function (response) {
            if (response.redirected) {
                window.location.href = response.url;
            } else {
                return response.text();
            }
        })
        .then(function (html) {
            if (html) {
                document.documentElement.innerHTML = html;
            }
        })
        .catch(function (erreur) {
            console.error('Erreur:', erreur);
            alert('Erreur lors de l\'envoi. Veuillez réessayer.');
            if (bouton) {
                bouton.disabled = false;
                bouton.innerHTML = '<i class="fa-solid fa-check"></i> Enregistrer les modifications';
            }
        });
    }

    function validerFormulaire() {
        var titre = document.getElementById('titre');
        var prix = document.getElementById('prixBaseNuit');
        var ville = document.getElementById('ville');
        var adresse = document.getElementById('adresseLigne1');
        var erreurs = [];
        var premierErreur = null;

        if (titre && !titre.value.trim()) { erreurs.push('Le titre est obligatoire.'); marquerErreur(titre); if (!premierErreur) premierErreur = titre; }
        else if (titre) effacerErreur(titre);

        if (prix && (!prix.value || parseFloat(prix.value) <= 0)) { erreurs.push('Le prix doit être supérieur à 0.'); marquerErreur(prix); if (!premierErreur) premierErreur = prix; }
        else if (prix) effacerErreur(prix);

        if (ville && !ville.value.trim()) { erreurs.push('La ville est obligatoire.'); marquerErreur(ville); if (!premierErreur) premierErreur = ville; }
        else if (ville) effacerErreur(ville);

        if (adresse && !adresse.value.trim()) { erreurs.push('L\'adresse est obligatoire.'); marquerErreur(adresse); if (!premierErreur) premierErreur = adresse; }
        else if (adresse) effacerErreur(adresse);

        if (erreurs.length > 0) {
            var messageGlobal = document.createElement('div');
            messageGlobal.className = 'message-erreur';
            messageGlobal.innerHTML = '<i class="fa-solid fa-circle-exclamation"></i><span>' + erreurs.join('<br>') + '</span>';
            messageGlobal.style.cssText = 'display:flex;align-items:center;gap:10px;background:#fef2f2;border:1px solid #fecaca;border-left:4px solid #ef4444;color:#991b1b;padding:14px 18px;border-radius:10px;font-size:14px;margin-bottom:20px;animation:slideDown 0.3s ease;';
            var ancien = formulaire.querySelector('.message-erreur');
            if (ancien) ancien.remove();
            formulaire.insertBefore(messageGlobal, formulaire.firstChild);
            if (premierErreur) { premierErreur.scrollIntoView({ behavior: 'smooth', block: 'center' }); premierErreur.focus(); }
            setTimeout(function () { if (messageGlobal.parentNode) messageGlobal.remove(); }, 5000);
            return false;
        }
        return true;
    }

    function marquerErreur(champ) { champ.style.borderColor = '#ef4444'; champ.style.background = '#fef2f2'; }
    function effacerErreur(champ) { champ.style.borderColor = ''; champ.style.background = ''; }

    // ============================================
    // SURVOL CARTES
    // ============================================
    function initialiserSurvolCartes() {
        if (!cartesMaison.length) return;
        cartesMaison.forEach(function (carte) {
            carte.addEventListener('mouseenter', function () {
                var img = carte.querySelector('.carte-maison-image img');
                if (img) { img.style.transform = 'scale(1.05)'; img.style.transition = 'transform 0.4s ease'; }
            });
            carte.addEventListener('mouseleave', function () {
                var img = carte.querySelector('.carte-maison-image img');
                if (img) img.style.transform = 'scale(1)';
            });
        });
    }

    // ============================================
    // CONFIRMATION SUPPRESSION
    // ============================================
    function initialiserConfirmationSuppression() {
        var boutons = document.querySelectorAll('.bouton-desactiver');
        boutons.forEach(function (b) {
            b.addEventListener('click', function (e) {
                if (!confirm('Voulez-vous vraiment désactiver cette annonce ?')) e.preventDefault();
            });
        });
    }

    // ============================================
    // COMPTEUR CARACTÈRES
    // ============================================
    function initialiserCompteurCaracteres() {
        var desc = document.getElementById('description');
        if (!desc) return;
        var max = 2000;
        var compteur = document.createElement('span');
        compteur.className = 'compteur-caracteres';
        compteur.style.cssText = 'display:block;text-align:right;font-size:12px;color:#94a3b8;margin-top:4px;';
        desc.parentElement.appendChild(compteur);
        function update() {
            var reste = max - desc.value.length;
            compteur.textContent = desc.value.length + ' / ' + max;
            compteur.style.color = reste < 0 ? '#ef4444' : reste < 100 ? '#f59e0b' : '#94a3b8';
        }
        desc.addEventListener('input', update);
        update();
    }

    // ============================================
    // DÉMARRAGE
    // ============================================
    initialiser();
})();