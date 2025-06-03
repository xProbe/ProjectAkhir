package controller;

import model.Transaction;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Controller untuk mengelola operasi CRUD transaksi
 * Implementasi pola MVC - Controller layer
 */
public class TransactionController {
    private Connection connection;
    
    public TransactionController() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }
    
    /**
     * Create - Menambah transaksi baru
     */
    public boolean addTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (transaction_date, type, category_id, amount, description) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(transaction.getTransactionDate()));
            stmt.setString(2, transaction.getType().name());
            stmt.setInt(3, transaction.getCategoryId());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, transaction.getDescription());
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error menambah transaksi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Read - Mengambil semua transaksi
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = """
            SELECT t.id, t.transaction_date, t.type, t.category_id, c.name as category_name, 
                   t.amount, t.description 
            FROM transactions t 
            LEFT JOIN categories c ON t.category_id = c.id 
            ORDER BY t.transaction_date DESC
            """;
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId(rs.getInt("id"));
                transaction.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
                transaction.setType(Transaction.TransactionType.valueOf(rs.getString("type")));
                transaction.setCategoryId(rs.getInt("category_id"));
                transaction.setCategoryName(rs.getString("category_name"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setDescription(rs.getString("description"));
                
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data transaksi: " + e.getMessage());
        }
        
        return transactions;
    }
    
    /**
     * Update - Memperbarui transaksi
     */
    public boolean updateTransaction(Transaction transaction) {
        String sql = "UPDATE transactions SET transaction_date=?, type=?, category_id=?, amount=?, description=? WHERE id=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(transaction.getTransactionDate()));
            stmt.setString(2, transaction.getType().name());
            stmt.setInt(3, transaction.getCategoryId());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, transaction.getDescription());
            stmt.setInt(6, transaction.getId());
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error memperbarui transaksi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete - Menghapus transaksi
     */
    public boolean deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error menghapus transaksi: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mendapatkan summary keuangan
     */
    public BigDecimal getTotalIncome() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("Error mendapatkan total pemasukan: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    public BigDecimal getTotalExpense() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            System.err.println("Error mendapatkan total pengeluaran: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
}