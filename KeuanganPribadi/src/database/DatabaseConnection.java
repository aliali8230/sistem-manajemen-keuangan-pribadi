package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// DatabaseConnection - Singleton pattern untuk koneksi MySQL
// Menerapkan konsep Encapsulation

public class DatabaseConnection {

    // Encapsulation: field private -> biar gabisa di akses class lain
    private static DatabaseConnection instance; //nyimpen satu satunya db
    private Connection connection; //deklarasi koneksi 

    private static final String URL      = "jdbc:mysql://localhost:3306/keuangan_db"; //final artinya gabisa diubah lagi
    private static final String USER     = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";

    // Constructor private (Singleton)
    private DatabaseConnection() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi database berhasil!");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            System.err.println("Driver MySQL tidak ditemukan: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Gagal koneksi database: " + e.getMessage());
        }
    }

    // Singleton getInstance instance-> objek
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection(); //klo blum ada db, nnti bikin db nya
        }
        return instance; //klo udh ada, ya pake yg ada
    }

    // Getter connection (Encapsulation)
    public Connection getConnection() {
        return connection;
    }

    // Inisialisasi tabel jika belum ada
    private void initializeDatabase() {
        String createTable = """
            CREATE TABLE IF NOT EXISTS transaksi (
                id         INT AUTO_INCREMENT PRIMARY KEY,
                tanggal    DATE         NOT NULL,
                keterangan VARCHAR(255) NOT NULL,
                kategori   VARCHAR(100) NOT NULL,
                jenis      ENUM('PEMASUKAN','PENGELUARAN') NOT NULL,
                jumlah     DECIMAL(15,2) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTable);
            System.out.println("Tabel transaksi siap digunakan.");
        } catch (SQLException e) {
            System.err.println("Gagal membuat tabel: " + e.getMessage());
        }
    }

    // Tutup koneksi
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }
}
