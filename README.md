# Aplikasi Manajemen Keuangan Pribadi

Aplikasi desktop untuk mengelola dan melacak keuangan pribadi dengan fitur pencatatan pemasukan dan pengeluaran.

## Fitur Utama

- Pencatatan transaksi pemasukan dan pengeluaran
- Kategorisasi transaksi
- Ringkasan saldo dan total pemasukan/pengeluaran
- Manajemen kategori transaksi
- Interface yang user-friendly dengan Java Swing

## Persyaratan Sistem

- Java Runtime Environment (JRE) 8 atau lebih tinggi
- MySQL Server
- JDBC Driver untuk MySQL

## Panduan Instalasi

1. Pastikan JRE dan MySQL Server sudah terinstal di sistem
2. Buat database dengan nama `financial_management`
3. Import struktur database dari file `database/financial_management.sql`
4. Sesuaikan konfigurasi database di `src/model/DatabaseConnection.java`:
   - Username (default: "root")
   - Password (default: "")
   - URL (default: "jdbc:mysql://localhost:3306/financial_management")

## Cara Menjalankan Aplikasi

1. Jalankan MySQL Server
2. Buka aplikasi melalui file JAR atau compile dan jalankan dari source code
3. Aplikasi akan menampilkan jendela utama dengan daftar transaksi dan ringkasan keuangan

## Panduan Penggunaan

### Menambah Transaksi
1. Klik tombol "Tambah Transaksi"
2. Isi form dengan detail transaksi:
   - Tanggal
   - Jenis (Pemasukan/Pengeluaran)
   - Kategori
   - Jumlah
   - Keterangan
3. Klik "Simpan"

### Mengedit Transaksi
1. Pilih transaksi dari tabel
2. Klik tombol "Edit Transaksi"
3. Ubah detail yang diinginkan
4. Klik "Simpan"

### Menghapus Transaksi
1. Pilih transaksi dari tabel
2. Klik tombol "Hapus Transaksi"
3. Konfirmasi penghapusan

## Struktur Kode

### Package Structure

```
src/
├── controller/
│   ├── CategoryController.java     # Kontroller untuk manajemen kategori
│   └── TransactionController.java  # Kontroller untuk manajemen transaksi
├── model/
│   ├── Category.java              # Model data kategori
│   ├── DatabaseConnection.java    # Singleton koneksi database
│   └── Transaction.java           # Model data transaksi
├── view/
│   ├── MainFrame.java            # Jendela utama aplikasi
│   ├── TransactionDialog.java    # Dialog form transaksi
│   └── TransactionTableModel.java # Model tabel transaksi
└── main/
    └── FinancialApp.java         # Entry point aplikasi
```

### Penjelasan Komponen

#### Model
- **Transaction**: Menyimpan data transaksi (tanggal, jenis, kategori, jumlah, keterangan)
- **Category**: Menyimpan data kategori transaksi
- **DatabaseConnection**: Mengelola koneksi ke database MySQL

#### Controller
- **TransactionController**: Menangani operasi CRUD untuk transaksi
- **CategoryController**: Menangani operasi CRUD untuk kategori

#### View
- **MainFrame**: Tampilan utama dengan daftar transaksi dan ringkasan
- **TransactionDialog**: Form untuk menambah/edit transaksi
- **TransactionTableModel**: Model data untuk tabel transaksi

## Alur Kerja Aplikasi

1. **Inisialisasi**:
   - `FinancialApp` menginisialisasi `MainFrame`
   - `MainFrame` membuat instance controller dan komponen UI

2. **Manajemen Data**:
   - Controller berkomunikasi dengan database melalui `DatabaseConnection`
   - Model menyimpan struktur data
   - View menampilkan data dan menerima input user

3. **Event Handling**:
   - User action ditangkap oleh listener di view
   - Controller memproses action dan mengupdate model
   - View direfresh untuk menampilkan perubahan

## Database Schema

### Tabel `categories`
- `id` (Primary Key)
- `name` (Nama kategori)
- `type` (INCOME/EXPENSE)

### Tabel `transactions`
- `id` (Primary Key)
- `transaction_date` (Tanggal transaksi)
- `type` (INCOME/EXPENSE)
- `category_id` (Foreign Key ke categories)
- `amount` (Jumlah)
- `description` (Keterangan)

## Best Practices yang Diimplementasikan

1. **Design Pattern**:
   - Singleton pattern untuk koneksi database
   - MVC architecture (Model-View-Controller)
   - Observer pattern untuk update UI

2. **Error Handling**:
   - Validasi input
   - Exception handling untuk operasi database
   - User feedback melalui dialog

3. **UI/UX**:
   - Responsive layout
   - Intuitive navigation
   - Clear feedback messages

## Pengembangan Lebih Lanjut

Beberapa ide untuk pengembangan:
- Fitur export/import data
- Grafik visualisasi keuangan
- Multiple currency support
- Fitur backup/restore
- Manajemen budget 

## Implementasi 5 Pillar OOP 
  
  - Encapsulation : file model/Transaction.java -> akses data hanya melalui getter/setter (private)
  - Inheritance : file view/MainFrame.java -> MainFrame extends JFrame 
  - Polymorphism : file view/TransactionTableModel.java -> method overriding 
  - Abstraction : file model/DatabaseConnection.java -> menyembunyikan kompleksitas koneksi database
  - Interface : file view/MainFrame.java -> implementasi interface Java Swing (addActionListener)