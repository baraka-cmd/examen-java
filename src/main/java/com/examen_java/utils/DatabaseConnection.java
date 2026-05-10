package com.examen_java.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {

    
    private static final String URL = "jdbc:mysql://localhost:3306/location_maisons_pro";
    
    
    private static final String USER = "root";
    
    
    private static final String PASSWORD = "";
    
   
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    
    
    static {
        try {
            Class.forName(DRIVER);
            System.out.println("Driver MySQL chargé avec succès.");
        } catch (ClassNotFoundException e) {
            System.err.println("ERREUR : Driver MySQL introuvable !");
            System.err.println("Ajoute mysql-connector-j-8.0.33.jar dans Libraries");
            e.printStackTrace();
        }
    }


    
    private DatabaseConnection() {
        
    }

    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    
    public static void tester() {
        try (Connection conn = getConnection()) {
            if (conn != null && conn.isValid(3)) {
                System.out.println("=== CONNEXION RÉUSSIE ===");
                System.out.println("Base : " + conn.getCatalog());
            } else {
                System.out.println("=== ÉCHEC DE CONNEXION ===");
            }
        } catch (SQLException e) {
            System.err.println("=== ÉCHEC DE CONNEXION ===");
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}