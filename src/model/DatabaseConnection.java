package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class untuk koneksi database
 * Implementasi pola Singleton untuk mengelola koneksi database
 */
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/financial_management";
    private static final String USERNAME = "root"; // Sesuaikan dengan username MySQL Anda
    private static final String PASSWORD = ""; // Sesuaikan dengan password MySQL Anda
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Koneksi database berhasil!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error koneksi database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Singleton getInstance method
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error mendapatkan koneksi: " + e.getMessage());
        }
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error menutup koneksi: " + e.getMessage());
        }
    }
}