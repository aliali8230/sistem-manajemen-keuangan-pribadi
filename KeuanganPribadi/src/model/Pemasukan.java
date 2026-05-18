package model;

import java.time.LocalDate;

///**
// * Pemasukan - Subclass dari Transaksi
// * Menerapkan INHERITANCE & POLYMORPHISM lebih dalam
// */
public class Pemasukan extends Transaksi {

    private String sumberPendapatan;

    public Pemasukan(int id, LocalDate tanggal, String keterangan,
                     String kategori, double jumlah, String sumberPendapatan) {
        super(id, tanggal, keterangan, kategori, JenisTransaksi.PEMASUKAN, jumlah);
        this.sumberPendapatan = sumberPendapatan;
    }

    public Pemasukan(LocalDate tanggal, String keterangan,
                     String kategori, double jumlah) {
        this(0, tanggal, keterangan, kategori, jumlah, "Umum");
    }

    // Polymorphism: override getRingkasan
    @Override
    public String getRingkasan() {
        return String.format("💚 PEMASUKAN | %s | %s | Rp %,.0f",
                tanggal, keterangan, getJumlah());
    }

    public String getSumberPendapatan() { return sumberPendapatan; }
    public void setSumberPendapatan(String sumberPendapatan) {
        this.sumberPendapatan = sumberPendapatan;
    }
}
