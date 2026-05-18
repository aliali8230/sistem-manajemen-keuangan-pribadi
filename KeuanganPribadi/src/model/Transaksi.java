package model;

import java.time.LocalDate;

///**
// * Transaksi - Model utama yang mewarisi BaseModel
// * Menerapkan konsep INHERITANCE & ENCAPSULATION
// */
public class Transaksi extends BaseModel {

    // Enum untuk jenis transaksi (Encapsulation of state)
    public enum JenisTransaksi {
        PEMASUKAN, PENGELUARAN
    }

    // Encapsulation: field private
    private double jumlah;
    private String kategori;
    private JenisTransaksi jenis;

    // Constructor lengkap
    public Transaksi(int id, LocalDate tanggal, String keterangan,
                     String kategori, JenisTransaksi jenis, double jumlah) {
        super(id, tanggal, keterangan); // memanggil constructor parent (Inheritance)
        this.jumlah   = jumlah;
        this.kategori = kategori;
        this.jenis    = jenis;
    }

    // Constructor tanpa id (untuk insert baru)
    public Transaksi(LocalDate tanggal, String keterangan,
                     String kategori, JenisTransaksi jenis, double jumlah) {
        this(0, tanggal, keterangan, kategori, jenis, jumlah);
    }

    // ===== Implementasi Abstract Method (Polymorphism) =====

    @Override
    public double getJumlah() {
        return jumlah;
    }

    @Override
    public String getJenis() {
        return jenis.name();
    }

    @Override
    public String getRingkasan() {
        return String.format("%s | %s | %s | Rp %,.0f",
                tanggal, jenis.name(), keterangan, jumlah);
    }

    // Getter & Setter (Encapsulation)
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public JenisTransaksi getJenisTransaksi() { return jenis; }
    public void setJenis(JenisTransaksi jenis) { this.jenis = jenis; }

    public void setJumlah(double jumlah) { this.jumlah = jumlah; }

    // Override toString (Polymorphism)
    @Override
    public String toString() {
        return String.format("Transaksi{id=%d, tanggal=%s, jenis=%s, jumlah=%.2f, kategori=%s}",
                id, tanggal, jenis, jumlah, kategori);
    }
}
