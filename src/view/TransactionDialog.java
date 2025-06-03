package view;

import controller.CategoryController;
import model.Category;
import model.Transaction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Dialog untuk input/edit transaksi
 * Implementasi GUI dengan Java Swing
 */
public class TransactionDialog extends JDialog {
    private JDateChooser dateChooser;
    private JComboBox<Transaction.TransactionType> typeComboBox;
    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextArea descriptionArea;
    
    private CategoryController categoryController;
    private Transaction transaction;
    private boolean confirmed = false;
    
    public TransactionDialog(JFrame parent, String title, Transaction transaction) {
        super(parent, title, true);
        this.transaction = transaction;
        this.categoryController = new CategoryController();
        
        initComponents();
        layoutComponents();
        setupEventHandlers();
        
        if (transaction != null) {
            populateFields();
        }
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        // Date chooser (menggunakan JSpinner sebagai alternatif JDateChooser)
        dateChooser = new JDateChooser();
        dateChooser.setDate(new Date());
        
        // Transaction type combo box
        typeComboBox = new JComboBox<>(Transaction.TransactionType.values());
        
        // Category combo box
        categoryComboBox = new JComboBox<>();
        updateCategoryComboBox();
        
        // Amount field
        amountField = new JTextField();
        
        // Description area
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout());
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Tanggal
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateChooser, gbc);
        
        // Jenis
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Jenis:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(typeComboBox, gbc);
        
        // Kategori
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(categoryComboBox, gbc);
        
        // Jumlah
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Jumlah:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(amountField, gbc);
        
        // Keterangan
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Keterangan:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Simpan");
        JButton cancelButton = new JButton("Batal");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Event handlers for buttons
        saveButton.addActionListener(e -> saveTransaction());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private void setupEventHandlers() {
        // Update category combo box when transaction type changes
        typeComboBox.addActionListener(e -> updateCategoryComboBox());
    }
    
    private void updateCategoryComboBox() {
        Transaction.TransactionType selectedType = (Transaction.TransactionType) typeComboBox.getSelectedItem();
        if (selectedType != null) {
            categoryComboBox.removeAllItems();
            List<Category> categories = categoryController.getCategoriesByType(selectedType);
            for (Category category : categories) {
                categoryComboBox.addItem(category);
            }
        }
    }
    
    private void populateFields() {
        if (transaction != null) {
            // Set date
            Date date = Date.from(transaction.getTransactionDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(date);
            
            // Set type
            typeComboBox.setSelectedItem(transaction.getType());
            
            // Set category
            updateCategoryComboBox();
            for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
                Category category = categoryComboBox.getItemAt(i);
                if (category.getId() == transaction.getCategoryId()) {
                    categoryComboBox.setSelectedItem(category);
                    break;
                }
            }
            
            // Set amount
            amountField.setText(transaction.getAmount().toString());
            
            // Set description
            descriptionArea.setText(transaction.getDescription());
        }
    }
    
    private void saveTransaction() {
        if (validateInput()) {
            // Create or update transaction object
            if (transaction == null) {
                transaction = new Transaction();
            }
            
            // Set values from form
            LocalDate localDate = dateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            transaction.setTransactionDate(localDate);
            transaction.setType((Transaction.TransactionType) typeComboBox.getSelectedItem());
            
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
            if (selectedCategory != null) {
                transaction.setCategoryId(selectedCategory.getId());
            }
            
            transaction.setAmount(new BigDecimal(amountField.getText()));
            transaction.setDescription(descriptionArea.getText());
            
            confirmed = true;
            dispose();
        }
    }
    
    private boolean validateInput() {
        // Validate amount
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Jumlah harus lebih besar dari 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Validate category selection
        if (categoryComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih kategori!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public Transaction getTransaction() {
        return confirmed ? transaction : null;
    }
}

// Simple JDateChooser implementation (since it's not in standard Java)
class JDateChooser extends JPanel {
    private JSpinner dateSpinner;
    
    public JDateChooser() {
        setLayout(new BorderLayout());
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        add(dateSpinner, BorderLayout.CENTER);
    }
    
    public void setDate(Date date) {
        dateSpinner.setValue(date);
    }
    
    public Date getDate() {
        return (Date) dateSpinner.getValue();
    }
}