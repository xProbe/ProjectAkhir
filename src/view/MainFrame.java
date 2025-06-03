package view;

import controller.TransactionController;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import model.Transaction;

public class MainFrame extends JFrame {
    private TransactionController transactionController;
    private TransactionTableModel tableModel;
    private JTable transactionTable;
    private JLabel totalIncomeLabel, totalExpenseLabel, balanceLabel;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    public MainFrame() {
        transactionController = new TransactionController();
        initComponents();
        layoutComponents();
        setupEventHandlers();
        refreshData();

        setTitle("💰 Aplikasi Manajemen Keuangan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 620);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Table model dan table
        tableModel = new TransactionTableModel();
        transactionTable = new JTable(tableModel);
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Styling table header
        JTableHeader header = transactionTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBackground(new Color(63, 81, 181));
        header.setForeground(Color.WHITE);
        header.setOpaque(true);

        transactionTable.setRowHeight(26);
        transactionTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        transactionTable.setShowVerticalLines(false);
        transactionTable.setGridColor(new Color(220, 220, 220));

        // Summary labels
        totalIncomeLabel = new JLabel("Total Pemasukan: Rp 0");
        totalExpenseLabel = new JLabel("Total Pengeluaran: Rp 0");
        balanceLabel = new JLabel("Saldo: Rp 0");

        // Style labels
        totalIncomeLabel.setForeground(new Color(56, 142, 60));
        totalExpenseLabel.setForeground(new Color(211, 47, 47));
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Tambah padding
        totalIncomeLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        totalExpenseLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        balanceLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Top panel - Summary
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "📊 Ringkasan Keuangan", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));
        summaryPanel.setBackground(new Color(250, 250, 250));

        summaryPanel.add(totalIncomeLabel);
        summaryPanel.add(totalExpenseLabel);
        summaryPanel.add(balanceLabel);
        add(summaryPanel, BorderLayout.NORTH);

        // Center panel - Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
                "📑 Daftar Transaksi", TitledBorder.LEFT, TitledBorder.TOP, new Font("SansSerif", Font.BOLD, 14)));
        tablePanel.setBackground(new Color(250, 250, 250));

        tablePanel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // Bottom panel - Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton addButton = createButton("➕ Tambah");
        JButton editButton = createButton("✏️ Edit");
        JButton deleteButton = createButton("🗑️ Hapus");
        JButton refreshButton = createButton("🔄 Refresh");

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

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBackground(new Color(63, 81, 181));
        button.setForeground(Color.WHITE);
        button.setBorder(new LineBorder(new Color(40, 53, 147)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 36));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(48, 63, 159));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(63, 81, 181));
            }
        });
        return button;
    }

    private void setupEventHandlers() {
        transactionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editTransaction();
                }
            }
        });
    }

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

    private void deleteTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih transaksi yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Transaction selectedTransaction = tableModel.getTransactionAt(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus transaksi ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (transactionController.deleteTransaction(selectedTransaction.getId())) {
                JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshData() {
        List<Transaction> transactions = transactionController.getAllTransactions();
        tableModel.setTransactions(transactions);
        updateSummary();
    }

    private void updateSummary() {
        BigDecimal totalIncome = transactionController.getTotalIncome();
        BigDecimal totalExpense = transactionController.getTotalExpense();
        BigDecimal balance = totalIncome.subtract(totalExpense);

        totalIncomeLabel.setText("Total Pemasukan: " + currencyFormat.format(totalIncome));
        totalExpenseLabel.setText("Total Pengeluaran: " + currencyFormat.format(totalExpense));
        balanceLabel.setText("Saldo: " + currencyFormat.format(balance));

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            balanceLabel.setForeground(new Color(56, 142, 60));
        } else if (balance.compareTo(BigDecimal.ZERO) < 0) {
            balanceLabel.setForeground(new Color(211, 47, 47));
        } else {
            balanceLabel.setForeground(Color.GRAY);
        }
    }
}
