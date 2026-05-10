package com.examen_java.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class PasswordUtil {

   
    private static final String ALGORITHME = "SHA-256";

    
    private static final int TAILLE_SEL = 32;

   
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    
    private static final String SEPARATEUR = ":";

    
    private PasswordUtil() {
        throw new IllegalStateException("Classe utilitaire - Ne pas instancier");
    }

    
    private static String genererSel() {
        byte[] sel = new byte[TAILLE_SEL];
        SECURE_RANDOM.nextBytes(sel);
        return Base64.getEncoder().encodeToString(sel);
    }

    
    private static String calculerHash(String motDePasse, String sel) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHME);
            digest.update(Base64.getDecoder().decode(sel));
            byte[] hash = digest.digest(motDePasse.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithme " + ALGORITHME + " non disponible", e);
        }
    }

   
    public static String hacher(String motDePasseClair) {
        if (motDePasseClair == null || motDePasseClair.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
        }
        String sel = genererSel();
        String hash = calculerHash(motDePasseClair, sel);
        return sel + SEPARATEUR + hash;
    }

    public static boolean verifier(String motDePasseClair, String hashStocke) {
        if (motDePasseClair == null || motDePasseClair.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
        }
        if (hashStocke == null || hashStocke.trim().isEmpty()) {
            throw new IllegalArgumentException("Le hash stocké ne peut pas être null ou vide");
        }
        if (!hashStocke.contains(SEPARATEUR)) {
            throw new IllegalArgumentException("Format de hash stocké invalide");
        }

        String[] parties = hashStocke.split(SEPARATEUR, 2);
        String sel = parties[0];
        String hashOriginal = parties[1];
        String hashCalcule = calculerHash(motDePasseClair, sel);

       
        return MessageDigest.isEqual(
                hashOriginal.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                hashCalcule.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
    }
}