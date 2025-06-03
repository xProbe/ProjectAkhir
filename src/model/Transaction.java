package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Model class untuk entitas Transaksi
 * Implementasi enkapsulasi dengan getter dan setter
 */
public class Transaction {
    private int id;
    private LocalDate transactionDate;
    private TransactionType type;
    private int categoryId;
    private String categoryName;
    private BigDecimal amount;
    private String description;
    
    // Enum untuk jenis transaksi
    public enum TransactionType {
        INCOME("Pemasukan"),
        EXPENSE("Pengeluaran");
        
        private final String displayName;
        
        TransactionType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Constructor
    public Transaction() {}
    
    public Transaction(LocalDate transactionDate, TransactionType type, 
                      int categoryId, BigDecimal amount, String description) {
        this.transactionDate = transactionDate;
        this.type = type;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
    }
    
    // Getters dan Setters (Enkapsulasi)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }
    
    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }
    
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}