package model;

import java.time.LocalDate;

public class Pengeluaran extends Transaksi {

    private String tujuanPengeluaran;

    public Pengeluaran(int id, LocalDate tanggal, String keterangan,
                       String kategori, double jumlah, String tujuanPengeluaran) {
        super(id, tanggal, keterangan, kategori, JenisTransaksi.PENGELUARAN, jumlah);
        this.tujuanPengeluaran = tujuanPengeluaran;
    }

    public Pengeluaran(LocalDate tanggal, String keterangan,
                       String kategori, double jumlah) {
        this(0, tanggal, keterangan, kategori, jumlah, "Umum");
    }

    // Polymorphism: override getRingkasan
    @Override
    public String getRingkasan() {
        return String.format("❤️ PENGELUARAN | %s | %s | Rp %,.0f",
                tanggal, keterangan, getJumlah());
    }

    public String getTujuanPengeluaran() { return tujuanPengeluaran; }
    public void setTujuanPengeluaran(String tujuanPengeluaran) {
        this.tujuanPengeluaran = tujuanPengeluaran;
    }
}
