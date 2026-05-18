package controller;

import model.Transaksi;
import model.Transaksi.JenisTransaksi;
import model.TransaksiDAO;

import java.time.LocalDate;
import java.util.List;

///**
// * TransaksiController - Controller dalam pola MVC
// * Jembatan antara View dan Model
// * Menerapkan konsep ENCAPSULATION pada business logic
// */
public class TransaksiController {

    // Encapsulation: DAO disimpan private
    private final TransaksiDAO dao;

    public TransaksiController() {
        this.dao = new TransaksiDAO();
    }

    // ======================== TAMBAH ========================
    public boolean tambahTransaksi(String tanggalStr, String keterangan,
                                    String kategori, String jenis, String jumlahStr) {
        try {
            LocalDate tanggal      = LocalDate.parse(tanggalStr);
            double jumlah          = Double.parseDouble(jumlahStr.replace(",", "").replace(".", ""));
            JenisTransaksi jenisEnum = JenisTransaksi.valueOf(jenis.toUpperCase());

            if (keterangan.isBlank() || kategori.isBlank()) return false;
            if (jumlah <= 0) return false;

            Transaksi t = new Transaksi(tanggal, keterangan, kategori, jenisEnum, jumlah);
            return dao.insert(t);
        } catch (Exception e) {
            System.err.println("Controller - tambahTransaksi: " + e.getMessage());
            return false;
        }
    }

    // ======================== AMBIL SEMUA ========================
    public List<Transaksi> getAllTransaksi() {
        return dao.findAll();
    }

    // ======================== AMBIL BY ID ========================
    public Transaksi getById(int id) {
        return dao.findById(id);
    }

    // ======================== FILTER BY JENIS ========================
    public List<Transaksi> filterByJenis(String jenis) {
        if (jenis == null || jenis.equals("SEMUA")) return dao.findAll();
        return dao.findByJenis(jenis.toUpperCase());
    }

    // ======================== CARI ========================
    public List<Transaksi> cari(String keyword) {
        if (keyword == null || keyword.isBlank()) return dao.findAll();
        return dao.search(keyword);
    }

    // ======================== UPDATE ========================
    public boolean updateTransaksi(int id, String tanggalStr, String keterangan,
                                    String kategori, String jenis, String jumlahStr) {
        try {
            LocalDate tanggal        = LocalDate.parse(tanggalStr);
            double jumlah            = Double.parseDouble(jumlahStr.replace(",", "").replace(".", ""));
            JenisTransaksi jenisEnum = JenisTransaksi.valueOf(jenis.toUpperCase());

            if (keterangan.isBlank() || kategori.isBlank()) return false;
            if (jumlah <= 0) return false;

            Transaksi t = new Transaksi(id, tanggal, keterangan, kategori, jenisEnum, jumlah);
            return dao.update(t);
        } catch (Exception e) {
            System.err.println("Controller - updateTransaksi: " + e.getMessage());
            return false;
        }
    }

    // ======================== HAPUS ========================
    public boolean hapusTransaksi(int id) {
        return dao.delete(id);
    }

    // ======================== RINGKASAN KEUANGAN ========================
    public double getTotalPemasukan()  { return dao.getTotalPemasukan(); }
    public double getTotalPengeluaran(){ return dao.getTotalPengeluaran(); }
    public double getSaldo()           { return dao.getSaldo(); }
}
