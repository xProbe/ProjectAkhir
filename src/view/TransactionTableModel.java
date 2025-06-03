package view;

import model.Transaction;
import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Custom TableModel untuk menampilkan data transaksi
 */
public class TransactionTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Tanggal", "Jenis", "Kategori", "Jumlah", "Keterangan"};
    private List<Transaction> transactions = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return transactions.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction transaction = transactions.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return transaction.getId();
            case 1: return transaction.getTransactionDate().format(dateFormatter);
            case 2: return transaction.getType().getDisplayName();
            case 3: return transaction.getCategoryName();
            case 4: return currencyFormat.format(transaction.getAmount());
            case 5: return transaction.getDescription();
            default: return null;
        }
    }
    
    public Transaction getTransactionAt(int rowIndex) {
        return transactions.get(rowIndex);
    }
}