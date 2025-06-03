package model;

/**
 * Model class untuk entitas Kategori
 */
public class Category {
    private int id;
    private String name;
    private Transaction.TransactionType type;
    
    public Category() {}
    
    public Category(String name, Transaction.TransactionType type) {
        this.name = name;
        this.type = type;
    }
    
    // Getters dan Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Transaction.TransactionType getType() { return type; }
    public void setType(Transaction.TransactionType type) { this.type = type; }
    
    @Override
    public String toString() {
        return name;
    }
}