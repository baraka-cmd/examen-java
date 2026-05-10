-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 10 mai 2026 à 14:29
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `location_maisons_pro`
--

-- --------------------------------------------------------

--
-- Structure de la table `avis`
--

CREATE TABLE `avis` (
  `id_avis` int(11) NOT NULL,
  `id_reservation` int(11) NOT NULL COMMENT 'Garantit un seul avis par réservation',
  `id_maison` int(11) NOT NULL,
  `id_auteur` int(11) NOT NULL COMMENT 'Le locataire qui a séjourné',
  `note_proprete` int(11) DEFAULT NULL CHECK (`note_proprete` >= 1 and `note_proprete` <= 5),
  `note_communication` int(11) DEFAULT NULL CHECK (`note_communication` >= 1 and `note_communication` <= 5),
  `note_emplacement` int(11) DEFAULT NULL CHECK (`note_emplacement` >= 1 and `note_emplacement` <= 5),
  `note_rapport_qualite_prix` int(11) DEFAULT NULL CHECK (`note_rapport_qualite_prix` >= 1 and `note_rapport_qualite_prix` <= 5),
  `note_moyenne` decimal(2,1) GENERATED ALWAYS AS ((`note_proprete` + `note_communication` + `note_emplacement` + `note_rapport_qualite_prix`) / 4.0) STORED,
  `commentaire` text DEFAULT NULL,
  `date_publication` timestamp NOT NULL DEFAULT current_timestamp(),
  `reponse_proprietaire` text DEFAULT NULL COMMENT 'Réponse du propriétaire',
  `date_reponse` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `favoris`
--

CREATE TABLE `favoris` (
  `id_favori` int(11) NOT NULL,
  `id_utilisateur` int(11) NOT NULL,
  `id_maison` int(11) NOT NULL,
  `date_ajout` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `favoris`
--

INSERT INTO `favoris` (`id_favori`, `id_utilisateur`, `id_maison`, `date_ajout`) VALUES
(1, 1, 1, '2026-05-09 12:05:17');

-- --------------------------------------------------------

--
-- Structure de la table `maisons`
--

CREATE TABLE `maisons` (
  `id_maison` int(11) NOT NULL,
  `id_proprietaire` int(11) NOT NULL COMMENT 'Référence utilisateurs.id_utilisateur',
  `titre` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `prix_base_nuit` decimal(10,2) NOT NULL CHECK (`prix_base_nuit` > 0),
  `capacite_personnes` int(11) NOT NULL CHECK (`capacite_personnes` > 0),
  `nombre_chambres` int(11) DEFAULT 1,
  `nombre_salles_bain` int(11) DEFAULT 1,
  `superficie_metres_carres` int(11) DEFAULT NULL,
  `adresse_ligne1` varchar(255) NOT NULL,
  `adresse_ligne2` varchar(255) DEFAULT NULL,
  `ville` varchar(100) NOT NULL,
  `code_postal` varchar(20) DEFAULT NULL,
  `pays` varchar(100) DEFAULT 'France',
  `equipements` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'Ex: ["Wi-Fi", "Cuisine", "Piscine"]' CHECK (json_valid(`equipements`)),
  `reglement_interieur` text DEFAULT NULL,
  `date_creation` timestamp NOT NULL DEFAULT current_timestamp(),
  `date_modification` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `est_visible` tinyint(1) DEFAULT 1 COMMENT 'Annonce active ou en pause'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `maisons`
--

INSERT INTO `maisons` (`id_maison`, `id_proprietaire`, `titre`, `description`, `prix_base_nuit`, `capacite_personnes`, `nombre_chambres`, `nombre_salles_bain`, `superficie_metres_carres`, `adresse_ligne1`, `adresse_ligne2`, `ville`, `code_postal`, `pays`, `equipements`, `reglement_interieur`, `date_creation`, `date_modification`, `est_visible`) VALUES
(1, 2, 'MAISON BARAKA NTWALI', 'la maison de qualite de Ir baraka ntwali', 10.00, 5, 10, 2, 10, 'GOMA', 'batiment 3', 'GOMA', '12345', 'RDC', '[\"Wi-Fi\", \"Climatisation\", \"Piscine\", \"Jardin\"]', 'La maison doit rester propre', '2026-05-09 11:55:01', '2026-05-09 11:55:01', 1),
(2, 2, 'maison a louer', 'maison a louer au bon prix', 25.00, 15, 20, 2, 30, '16 rue place d\'independance', 'batiment 39', 'bukavu', '6789', 'RDC', '[\"Climatisation\", \"Parking gratuit\", \"Piscine\", \"Animaux acceptÃ©s\", \"Jardin\"]', 'la proprete avant tout', '2026-05-09 11:58:25', '2026-05-09 11:58:25', 1),
(3, 4, 'MAISON SALAMA', 'Maison a location', 30.00, 10, 20, 3, 20, 'bukavu', 'batiment 3', 'bukavu', '14758', 'RDC', '[\"Cuisine\", \"Climatisation\", \"Piscine\", \"Lave-linge\", \"TÃ©lÃ©vision\", \"Animaux acceptÃ©s\"]', 'toujours au bon prix', '2026-05-09 12:03:19', '2026-05-09 12:03:19', 1);

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `maisons_avec_note`
-- (Voir ci-dessous la vue réelle)
--
CREATE TABLE `maisons_avec_note` (
`id_maison` int(11)
,`titre` varchar(255)
,`ville` varchar(100)
,`prix_base_nuit` decimal(10,2)
,`capacite_personnes` int(11)
,`nombre_avis` bigint(21)
,`note_moyenne_avis` decimal(3,1)
);

-- --------------------------------------------------------

--
-- Structure de la table `medias`
--

CREATE TABLE `medias` (
  `id_media` int(11) NOT NULL,
  `id_maison` int(11) NOT NULL,
  `chemin_fichier` varchar(500) NOT NULL COMMENT 'Relatif au dossier uploads',
  `legende` varchar(255) DEFAULT NULL,
  `est_image_principale` tinyint(1) DEFAULT 0,
  `ordre_affichage` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `medias`
--

INSERT INTO `medias` (`id_media`, `id_maison`, `chemin_fichier`, `legende`, `est_image_principale`, `ordre_affichage`) VALUES
(1, 1, '2f0092d0-0d88-4218-9473-19a54c2679e4.jpg', NULL, 1, 0),
(2, 1, '72c4fc13-09a1-4f28-afa1-547771735c1a.jpeg', NULL, 0, 1),
(3, 1, '5cc0d0c0-5a9f-45a0-80ee-6e2a0f3c2fe8.jpg', NULL, 0, 2),
(4, 2, 'e83625bc-05c2-46c2-9717-b68d94a260a3.jpeg', NULL, 1, 0),
(5, 2, 'c6c47404-06a5-4f76-94cf-3ca8aab5c275.jpeg', NULL, 0, 1),
(6, 3, 'e36c8789-c3b7-4271-a5bf-3779111355a2.jpeg', NULL, 1, 0),
(7, 3, 'fe98b977-e36a-40ac-addd-a082844b4292.jpg', NULL, 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `messages`
--

CREATE TABLE `messages` (
  `id_message` int(11) NOT NULL,
  `id_reservation` int(11) NOT NULL,
  `id_expediteur` int(11) NOT NULL,
  `contenu` text NOT NULL,
  `date_envoi` timestamp NOT NULL DEFAULT current_timestamp(),
  `est_lu` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `messages`
--

INSERT INTO `messages` (`id_message`, `id_reservation`, `id_expediteur`, `contenu`, `date_envoi`, `est_lu`) VALUES
(1, 1, 1, 'Bonjour je viens de reserver une maison', '2026-05-09 12:08:37', 1),
(2, 1, 2, 'je viens de voir votre reservation et votre paiement\r\nje viens de valider', '2026-05-09 12:10:42', 1);

-- --------------------------------------------------------

--
-- Structure de la table `paiements`
--

CREATE TABLE `paiements` (
  `id_paiement` int(11) NOT NULL,
  `id_reservation` int(11) NOT NULL,
  `montant` decimal(10,2) NOT NULL,
  `methode_paiement` varchar(50) DEFAULT 'CARTE_BANCAIRE',
  `statut` enum('REUSSI','ECHOUE','REMBOURSE','EN_COURS') NOT NULL,
  `reference_transaction` varchar(255) DEFAULT NULL COMMENT 'ID transaction bancaire',
  `date_paiement` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `paiements`
--

INSERT INTO `paiements` (`id_paiement`, `id_reservation`, `montant`, `methode_paiement`, `statut`, `reference_transaction`, `date_paiement`) VALUES
(1, 1, 90.00, 'VIREMENT', 'REUSSI', 'TXN-702FDACE', '2026-05-09 12:06:32');

-- --------------------------------------------------------

--
-- Structure de la table `profils`
--

CREATE TABLE `profils` (
  `id_profil` int(11) NOT NULL,
  `id_utilisateur` int(11) NOT NULL,
  `role` enum('LOCATAIRE','PROPRIETAIRE','ADMINISTRATEUR') NOT NULL,
  `date_attribution` timestamp NOT NULL DEFAULT current_timestamp(),
  `est_verifie` tinyint(1) DEFAULT 0 COMMENT 'Badge propriétaire vérifié'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `profils`
--

INSERT INTO `profils` (`id_profil`, `id_utilisateur`, `role`, `date_attribution`, `est_verifie`) VALUES
(1, 1, 'LOCATAIRE', '2026-05-09 11:48:18', 0),
(2, 2, 'PROPRIETAIRE', '2026-05-09 11:51:06', 0),
(3, 3, 'LOCATAIRE', '2026-05-09 11:59:48', 0),
(4, 4, 'PROPRIETAIRE', '2026-05-09 12:00:44', 0);

-- --------------------------------------------------------

--
-- Structure de la table `reservations`
--

CREATE TABLE `reservations` (
  `id_reservation` int(11) NOT NULL,
  `id_maison` int(11) NOT NULL,
  `id_locataire` int(11) NOT NULL COMMENT 'Référence utilisateurs.id_utilisateur',
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `nombre_personnes` int(11) NOT NULL,
  `montant_total` decimal(10,2) NOT NULL COMMENT 'Montant figé à la réservation',
  `statut` enum('BROUILLON','EN_ATTENTE_PAIEMENT','CONFIRMEE','ANNULEE_PAR_LOCATAIRE','ANNULEE_PAR_PROPRIETAIRE','TERMINEE') DEFAULT 'EN_ATTENTE_PAIEMENT',
  `date_reservation` timestamp NOT NULL DEFAULT current_timestamp(),
  `date_modification` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `message_locataire` text DEFAULT NULL COMMENT 'Message facultatif du locataire'
) ;

--
-- Déchargement des données de la table `reservations`
--

INSERT INTO `reservations` (`id_reservation`, `id_maison`, `id_locataire`, `date_debut`, `date_fin`, `nombre_personnes`, `montant_total`, `statut`, `date_reservation`, `date_modification`, `message_locataire`) VALUES
(1, 1, 1, '2026-05-09', '2026-05-18', 4, 90.00, 'CONFIRMEE', '2026-05-09 12:05:57', '2026-05-09 12:06:32', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `tarifs_speciaux`
--

CREATE TABLE `tarifs_speciaux` (
  `id_tarif` int(11) NOT NULL,
  `id_maison` int(11) NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `prix_nuit` decimal(10,2) NOT NULL CHECK (`prix_nuit` > 0),
  `motif` varchar(255) DEFAULT NULL COMMENT 'Ex: Haute saison été, Noël'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `utilisateurs`
--

CREATE TABLE `utilisateurs` (
  `id_utilisateur` int(11) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mot_de_passe_hash` varchar(255) NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `telephone` varchar(30) DEFAULT NULL,
  `photo_profil_url` varchar(500) DEFAULT NULL,
  `date_inscription` timestamp NOT NULL DEFAULT current_timestamp(),
  `derniere_connexion` timestamp NULL DEFAULT NULL,
  `email_verifie` tinyint(1) DEFAULT 0,
  `est_actif` tinyint(1) DEFAULT 1 COMMENT 'Soft delete / bannissement'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `utilisateurs`
--

INSERT INTO `utilisateurs` (`id_utilisateur`, `email`, `mot_de_passe_hash`, `nom`, `prenom`, `telephone`, `photo_profil_url`, `date_inscription`, `derniere_connexion`, `email_verifie`, `est_actif`) VALUES
(1, 'barakantwali06@gmail.com', 'c/7MAMY5Bft9d/iQs49sPAld+gby4kq1+blVGej1KBw=:H1Tsvku0+NVkspb7KIZnbJSJSxnmXU7Hlx6tj+yD/sg=', 'BARAKA', 'NTWALI', '09876655444', NULL, '2026-05-09 11:48:16', '2026-05-09 12:12:33', 0, 1),
(2, 'barakantwali006@gmail.com', 'HWTzEW5/R79VUHWfj2hI3g6VVUCWVdgLKM/aTwGVSGY=:SC/NC3ZkSdMkAScFwpPp0oEFtDISI0NRDtb6mOuyR/0=', 'Felix', 'baraka', '09556677788', NULL, '2026-05-09 11:51:06', '2026-05-09 12:09:09', 0, 1),
(3, 'barakantwali0006@gmail.com', 'Q/duU+1uSoqy1JHRFBisWTLUjLLR40PAZKqYSP5ETJk=:wR8PrYhsQq7uE4vXMuA1EWXHPWYzwzDgJiodLSXr7A0=', 'Bahati', 'Balume', '093636454536', NULL, '2026-05-09 11:59:47', '2026-05-09 12:14:32', 0, 1),
(4, 'barakantwali00006@gmail.com', '8CaAtpfZak39PobpI6tBSDvJRrbMGERTK78zivzlTgk=:XchegBCCEVXeLoUXAJlVd8vpOH7xjqFaS+QLcjVwoh0=', 'Salama', 'Janvier', '096446363636', NULL, '2026-05-09 12:00:43', '2026-05-10 13:30:32', 0, 1);

-- --------------------------------------------------------

--
-- Structure de la vue `maisons_avec_note`
--
DROP TABLE IF EXISTS `maisons_avec_note`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `maisons_avec_note`  AS SELECT `m`.`id_maison` AS `id_maison`, `m`.`titre` AS `titre`, `m`.`ville` AS `ville`, `m`.`prix_base_nuit` AS `prix_base_nuit`, `m`.`capacite_personnes` AS `capacite_personnes`, count(`a`.`id_avis`) AS `nombre_avis`, round(avg(`a`.`note_moyenne`),1) AS `note_moyenne_avis` FROM (`maisons` `m` left join `avis` `a` on(`m`.`id_maison` = `a`.`id_maison`)) GROUP BY `m`.`id_maison` ;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `avis`
--
ALTER TABLE `avis`
  ADD PRIMARY KEY (`id_avis`),
  ADD UNIQUE KEY `id_reservation` (`id_reservation`),
  ADD KEY `id_maison` (`id_maison`),
  ADD KEY `id_auteur` (`id_auteur`);

--
-- Index pour la table `favoris`
--
ALTER TABLE `favoris`
  ADD PRIMARY KEY (`id_favori`),
  ADD UNIQUE KEY `unique_favori` (`id_utilisateur`,`id_maison`),
  ADD KEY `id_maison` (`id_maison`);

--
-- Index pour la table `maisons`
--
ALTER TABLE `maisons`
  ADD PRIMARY KEY (`id_maison`),
  ADD KEY `idx_ville` (`ville`),
  ADD KEY `idx_capacite` (`capacite_personnes`),
  ADD KEY `idx_prix` (`prix_base_nuit`),
  ADD KEY `idx_proprietaire` (`id_proprietaire`);

--
-- Index pour la table `medias`
--
ALTER TABLE `medias`
  ADD PRIMARY KEY (`id_media`),
  ADD KEY `idx_maison_media` (`id_maison`);

--
-- Index pour la table `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`id_message`),
  ADD KEY `id_expediteur` (`id_expediteur`),
  ADD KEY `idx_conversation` (`id_reservation`,`date_envoi`);

--
-- Index pour la table `paiements`
--
ALTER TABLE `paiements`
  ADD PRIMARY KEY (`id_paiement`),
  ADD KEY `idx_reservation_paiement` (`id_reservation`);

--
-- Index pour la table `profils`
--
ALTER TABLE `profils`
  ADD PRIMARY KEY (`id_profil`),
  ADD UNIQUE KEY `unique_utilisateur_role` (`id_utilisateur`,`role`);

--
-- Index pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id_reservation`),
  ADD KEY `idx_conflit_dates` (`id_maison`,`date_debut`,`date_fin`,`statut`),
  ADD KEY `idx_locataire` (`id_locataire`),
  ADD KEY `idx_statut` (`statut`);

--
-- Index pour la table `tarifs_speciaux`
--
ALTER TABLE `tarifs_speciaux`
  ADD PRIMARY KEY (`id_tarif`),
  ADD KEY `idx_tarif_dates` (`id_maison`,`date_debut`,`date_fin`);

--
-- Index pour la table `utilisateurs`
--
ALTER TABLE `utilisateurs`
  ADD PRIMARY KEY (`id_utilisateur`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_email` (`email`),
  ADD KEY `idx_nom_prenom` (`nom`,`prenom`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `avis`
--
ALTER TABLE `avis`
  MODIFY `id_avis` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `favoris`
--
ALTER TABLE `favoris`
  MODIFY `id_favori` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `maisons`
--
ALTER TABLE `maisons`
  MODIFY `id_maison` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `medias`
--
ALTER TABLE `medias`
  MODIFY `id_media` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `messages`
--
ALTER TABLE `messages`
  MODIFY `id_message` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `paiements`
--
ALTER TABLE `paiements`
  MODIFY `id_paiement` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `profils`
--
ALTER TABLE `profils`
  MODIFY `id_profil` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id_reservation` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `tarifs_speciaux`
--
ALTER TABLE `tarifs_speciaux`
  MODIFY `id_tarif` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `utilisateurs`
--
ALTER TABLE `utilisateurs`
  MODIFY `id_utilisateur` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `avis`
--
ALTER TABLE `avis`
  ADD CONSTRAINT `avis_ibfk_1` FOREIGN KEY (`id_reservation`) REFERENCES `reservations` (`id_reservation`),
  ADD CONSTRAINT `avis_ibfk_2` FOREIGN KEY (`id_maison`) REFERENCES `maisons` (`id_maison`) ON DELETE CASCADE,
  ADD CONSTRAINT `avis_ibfk_3` FOREIGN KEY (`id_auteur`) REFERENCES `utilisateurs` (`id_utilisateur`);

--
-- Contraintes pour la table `favoris`
--
ALTER TABLE `favoris`
  ADD CONSTRAINT `favoris_ibfk_1` FOREIGN KEY (`id_utilisateur`) REFERENCES `utilisateurs` (`id_utilisateur`) ON DELETE CASCADE,
  ADD CONSTRAINT `favoris_ibfk_2` FOREIGN KEY (`id_maison`) REFERENCES `maisons` (`id_maison`) ON DELETE CASCADE;

--
-- Contraintes pour la table `maisons`
--
ALTER TABLE `maisons`
  ADD CONSTRAINT `maisons_ibfk_1` FOREIGN KEY (`id_proprietaire`) REFERENCES `utilisateurs` (`id_utilisateur`);

--
-- Contraintes pour la table `medias`
--
ALTER TABLE `medias`
  ADD CONSTRAINT `medias_ibfk_1` FOREIGN KEY (`id_maison`) REFERENCES `maisons` (`id_maison`) ON DELETE CASCADE;

--
-- Contraintes pour la table `messages`
--
ALTER TABLE `messages`
  ADD CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`id_reservation`) REFERENCES `reservations` (`id_reservation`) ON DELETE CASCADE,
  ADD CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`id_expediteur`) REFERENCES `utilisateurs` (`id_utilisateur`) ON DELETE CASCADE;

--
-- Contraintes pour la table `paiements`
--
ALTER TABLE `paiements`
  ADD CONSTRAINT `paiements_ibfk_1` FOREIGN KEY (`id_reservation`) REFERENCES `reservations` (`id_reservation`);

--
-- Contraintes pour la table `profils`
--
ALTER TABLE `profils`
  ADD CONSTRAINT `profils_ibfk_1` FOREIGN KEY (`id_utilisateur`) REFERENCES `utilisateurs` (`id_utilisateur`) ON DELETE CASCADE;

--
-- Contraintes pour la table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`id_maison`) REFERENCES `maisons` (`id_maison`),
  ADD CONSTRAINT `reservations_ibfk_2` FOREIGN KEY (`id_locataire`) REFERENCES `utilisateurs` (`id_utilisateur`);

--
-- Contraintes pour la table `tarifs_speciaux`
--
ALTER TABLE `tarifs_speciaux`
  ADD CONSTRAINT `tarifs_speciaux_ibfk_1` FOREIGN KEY (`id_maison`) REFERENCES `maisons` (`id_maison`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
