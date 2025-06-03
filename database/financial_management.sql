-- financial_management.sql
CREATE DATABASE IF NOT EXISTS financial_management;
USE financial_management;

-- Tabel untuk kategori transaksi
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabel untuk transaksi keuangan
CREATE TABLE transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_date DATE NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    category_id INT,
    amount DECIMAL(15,2) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Insert kategori default
INSERT INTO categories (name, type) VALUES
('Gaji', 'INCOME'),
('Bonus', 'INCOME'),
('Investasi', 'INCOME'),
('Makanan', 'EXPENSE'),
('Transportasi', 'EXPENSE'),
('Hiburan', 'EXPENSE'),
('Kesehatan', 'EXPENSE'),
('Belanja', 'EXPENSE');