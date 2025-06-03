package controller;

import model.Category;
import model.Transaction;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk mengelola kategori
 */
public class CategoryController {
    private Connection connection;
    
    public CategoryController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Mengambil semua kategori berdasarkan tipe
     */
    public List<Category> getCategoriesByType(Transaction.TransactionType type) {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name, type FROM categories WHERE type = ? ORDER BY name";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setType(Transaction.TransactionType.valueOf(rs.getString("type")));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil kategori: " + e.getMessage());
        }
        
        return categories;
    }
    
    /**
     * Mengambil semua kategori
     */
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name, type FROM categories ORDER BY type, name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setType(Transaction.TransactionType.valueOf(rs.getString("type")));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil semua kategori: " + e.getMessage());
        }
        
        return categories;
    }
}