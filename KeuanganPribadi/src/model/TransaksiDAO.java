package model;

import database.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TransaksiDAO - Data Access Object untuk operasi CRUD

public class TransaksiDAO {

    private final Connection conn;

    public TransaksiDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // create
    public boolean insert(Transaksi t) {
        String sql = "INSERT INTO transaksi (tanggal, keterangan, kategori, jenis, jumlah) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, Date.valueOf(t.getTanggal()));
            ps.setString(2, t.getKeterangan());
            ps.setString(3, t.getKategori());
            ps.setString(4, t.getJenis());
            ps.setDouble(5, t.getJumlah());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) t.setId(rs.getInt(1));
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Gagal insert: " + e.getMessage());
        }
        return false;
    }

    // read all
    public List<Transaksi> findAll() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi ORDER BY tanggal DESC, created_at DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data: " + e.getMessage());
        }
        return list;
    }

    // read by id
    public Transaksi findById(int id) {
        String sql = "SELECT * FROM transaksi WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("Gagal mencari data: " + e.getMessage());
        }
        return null;
    }

    // read by jenis
    public List<Transaksi> findByJenis(String jenis) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE jenis = ? ORDER BY tanggal DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jenis);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Gagal filter data: " + e.getMessage());
        }
        return list;
    }

    // search
    public List<Transaksi> search(String keyword) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT * FROM transaksi WHERE keterangan LIKE ? OR kategori LIKE ? ORDER BY tanggal DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("Gagal search: " + e.getMessage());
        }
        return list;
    }

    // update
    public boolean update(Transaksi t) {
        String sql = "UPDATE transaksi SET tanggal=?, keterangan=?, kategori=?, jenis=?, jumlah=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(t.getTanggal()));
            ps.setString(2, t.getKeterangan());
            ps.setString(3, t.getKategori());
            ps.setString(4, t.getJenis());
            ps.setDouble(5, t.getJumlah());
            ps.setInt(6, t.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal update: " + e.getMessage());
        }
        return false;
    }

    // delete
    public boolean delete(int id) {
        String sql = "DELETE FROM transaksi WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal delete: " + e.getMessage());
        }
        return false;
    }

    // ringkasan 
    public double getTotalPemasukan() {
        return getTotal("PEMASUKAN");
    }

    public double getTotalPengeluaran() {
        return getTotal("PENGELUARAN");
    }

    public double getSaldo() {
        return getTotalPemasukan() - getTotalPengeluaran();
    }

    private double getTotal(String jenis) {
        String sql = "SELECT COALESCE(SUM(jumlah), 0) FROM transaksi WHERE jenis = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, jenis);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("Gagal hitung total: " + e.getMessage());
        }
        return 0;
    }

    // Mapper -> penerjemah 
    private Transaksi mapRow(ResultSet rs) throws SQLException {
        int id            = rs.getInt("id");
        LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
        String keterangan = rs.getString("keterangan");
        String kategori   = rs.getString("kategori");
        String jenis      = rs.getString("jenis");
        double jumlah     = rs.getDouble("jumlah");

        if ("PEMASUKAN".equals(jenis)) {
            return new Pemasukan(id, tanggal, keterangan, kategori, jumlah, "Umum");
        } else {
            return new Pengeluaran(id, tanggal, keterangan, kategori, jumlah, "Umum");
        }
    }
}
