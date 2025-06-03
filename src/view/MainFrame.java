package view;

import controller.TransactionController;
import model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Main GUI Frame - Implementasi View dalam pola MVC
 * Menggunakan Java Swing untuk antarmuka pengguna
 */
public class MainFrame extends JFrame {
    private TransactionController transactionController;
    private TransactionTableModel tableModel;
    private JTable transactionTable;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel balanceLabel;
    
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
    
    public MainFrame() {
        transactionController = new TransactionController();
        initComponents();
        layoutComponents();
        setupEventHandlers();
        refreshData();
        
        setTitle("Aplikasi Manajemen Keuangan Pribadi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        // Table model dan table
        tableModel = new TransactionTableModel();
        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Summary labels
        totalIncomeLabel = new JLabel("Total Pemasukan: Rp 0");
        totalExpenseLabel = new JLabel("Total Pengeluaran: Rp 0");
        balanceLabel = new JLabel("Saldo: Rp 0");
        
        // Style labels
        totalIncomeLabel.setForeground(new Color(0, 150, 0));
        totalExpenseLabel.setForeground(new Color(200, 0, 0));
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(Font.BOLD, 14f));
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Top panel - Summary
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Ringkasan Keuangan"));
        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(balanceLabel);
        add(summaryPanel, BorderLayout.NORTH);
        
        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Daftar Transaksi"));
        tablePanel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Tambah Transaksi");
        JButton editButton = new JButton("Edit Transaksi");
        JButton deleteButton = new JButton("Hapus Transaksi");
        JButton refreshButton = new JButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event handlers
        addButton.addActionListener(e -> addTransaction());
        editButton.addActionListener(e -> editTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());
        refreshButton.addActionListener(e -> refreshData());
    }
    
    private void setupEventHandlers() {
        // Double click to edit
        transactionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editTransaction();
                }
            }
        });
    }
    
    /**
     * Menambah transaksi baru
     */
    private void addTransaction() {
        TransactionDialog dialog = new TransactionDialog(this, "Tambah Transaksi", null);
        dialog.setVisible(true);
        
        Transaction newTransaction = dialog.getTransaction();
        
        if (newTransaction != null) {
            if (transactionController.addTransaction(newTransaction)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan transaksi!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Mengedit transaksi yang dipilih
     */
    private void editTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Transaction selectedTransaction = tableModel.getTransactionAt(selectedRow);
        TransactionDialog dialog = new TransactionDialog(this, "Edit Transaksi", selectedTransaction);
        dialog.setVisible(true);
        
        Transaction updatedTransaction = dialog.getTransaction();
        if (updatedTransaction != null) {
            if (transactionController.updateTransaction(updatedTransaction)) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui transaksi!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Menghapus transaksi yang dipilih
     */
    private void deleteTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Transaction selectedTransaction = tableModel.getTransactionAt(selectedRow);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus transaksi ini?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (transactionController.deleteTransaction(selectedTransaction.getId())) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Refresh data dari database dan update tampilan
     */
    private void refreshData() {
        // Update table
        List<Transaction> transactions = transactionController.getAllTransactions();
        tableModel.setTransactions(transactions);
        
        // Update summary
        updateSummary();
    }
    
    /**
     * Update ringkasan keuangan
     */
    private void updateSummary() {
        BigDecimal totalIncome = transactionController.getTotalIncome();
        BigDecimal totalExpense = transactionController.getTotalExpense();
        BigDecimal balance = totalIncome.subtract(totalExpense);
        
        totalIncomeLabel.setText("Total Pemasukan: " + currencyFormat.format(totalIncome));
        totalExpenseLabel.setText("Total Pengeluaran: " + currencyFormat.format(totalExpense));
        balanceLabel.setText("Saldo: " + currencyFormat.format(balance));
        
        // Set color based on balance
        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            balanceLabel.setForeground(new Color(0, 150, 0));
        } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
            balanceLabel.setForeground(new Color(200, 0, 0));
        } else {
            balanceLabel.setForeground(Color.BLACK);
        }
    }
}