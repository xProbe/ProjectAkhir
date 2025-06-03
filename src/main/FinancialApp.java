package main;

import javax.swing.*;
import view.MainFrame;

/**
 * Kelas utama aplikasi
 * Entry point untuk menjalankan aplikasi manajemen keuangan pribadi
 */
public class FinancialApp {
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Jalankan GUI di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new MainFrame().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Error menjalankan aplikasi: " + e.getMessage() + 
                    "\n\nPastikan:\n" +
                    "1. MySQL server berjalan\n" +
                    "2. Database 'financial_management' sudah dibuat\n" +
                    "3. JDBC driver MySQL tersedia di classpath",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        });
    }
}