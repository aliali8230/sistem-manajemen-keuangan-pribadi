package view;

import controller.TransaksiController;
import model.Transaksi;
import util.FormatUtil;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

///**
// * MainView - View utama dalam pola MVC
// * Mengatur tampilan antarmuka pengguna (Swing)
// * Menerapkan Layout Management yang rapi
// */
public class MainView extends JFrame {

    // ===== Warna Tema =====
    private static final Color HIJAU_TUA   = new Color(16, 124, 65);
    private static final Color HIJAU_MUDA  = new Color(209, 250, 229);
    private static final Color MERAH_TUA   = new Color(185, 28, 28);
    private static final Color MERAH_MUDA  = new Color(254, 226, 226);
    private static final Color BIRU_TUA    = new Color(29, 78, 216);
    private static final Color BIRU_MUDA   = new Color(219, 234, 254);
    private static final Color BG_UTAMA    = new Color(248, 250, 252);
    private static final Color BG_SIDEBAR  = new Color(15, 23, 42);
    private static final Color TEKS_SIDEBAR= new Color(226, 232, 240);
    private static final Color AKSEN       = new Color(99, 102, 241);

    // ===== Font =====
    private static final Font FONT_JUDUL  = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_LABEL  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BOLD   = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONT_ANGKA  = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_KECIL  = new Font("Segoe UI", Font.PLAIN, 11);

    // ===== Controller (MVC) =====
    private final TransaksiController controller;

    // ===== Komponen UI =====
    private JTable tabelTransaksi;
    private DefaultTableModel tableModel;

    // Panel kartu ringkasan
    private JLabel lblSaldo, lblPemasukan, lblPengeluaran;

    // Form input
    private JTextField txtTanggal, txtKeterangan, txtJumlah, txtCari;
    private JComboBox<String> cmbKategori, cmbJenis;
    private JButton btnSimpan, btnBatal, btnHapus, btnEdit;

    // State
    private int selectedId = -1;
    private boolean modeEdit = false;

    // ===== KONSTRUKTOR =====
    public MainView() {
        this.controller = new TransaksiController();
        initUI();
        loadData();
    }

    // ===== INISIALISASI UI =====
    private void initUI() {
        setTitle("Sistem Catatan Keuangan Pribadi");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_UTAMA);

        // Komponen utama
        add(buatHeader(), BorderLayout.NORTH);
        add(buatSidebar(), BorderLayout.WEST);
        add(buatKontenUtama(), BorderLayout.CENTER);

        setVisible(true);
    }

    // ===== HEADER =====
    private JPanel buatHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_SIDEBAR);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblJudul = new JLabel("Keuangan Pribadi");
        lblJudul.setFont(FONT_JUDUL);
        lblJudul.setForeground(Color.WHITE);

        JLabel lblTanggalHari = new JLabel(
                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy",
                        new java.util.Locale("id", "ID"))));
        lblTanggalHari.setFont(FONT_KECIL);
        lblTanggalHari.setForeground(TEKS_SIDEBAR);

        JPanel kiri = new JPanel(new GridLayout(2, 1));
        kiri.setOpaque(false);
        kiri.add(lblJudul);
        kiri.add(lblTanggalHari);

        header.add(kiri, BorderLayout.WEST);
        return header;
    }

    // ===== SIDEBAR =====
    private JPanel buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // ---- Kartu Ringkasan ----
        sidebar.add(buatKartuSaldo("SALDO", "Rp 0", new Color(99, 102, 241)));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(buatKartuSaldo("PEMASUKAN", "Rp 0", HIJAU_TUA));
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(buatKartuSaldo("PENGELUARAN", "Rp 0", MERAH_TUA));
        sidebar.add(Box.createVerticalStrut(20));

        // ---- Tombol Tambah Transaksi ----
        JButton btnTambah = buatTombol("+ Tambah Transaksi", AKSEN, Color.WHITE);
        btnTambah.addActionListener(e -> tampilkanFormDialog(null));
        sidebar.add(btnTambah);

        return sidebar;
    }

    // Kartu ringkasan di sidebar
    private JPanel buatKartuSaldo(String judul, String nilai, Color warna) {
        JPanel kartu = new JPanel(new GridLayout(2, 1, 0, 4));
        kartu.setBackground(new Color(30, 41, 59));
        kartu.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(warna, 1, true),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        kartu.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel lblJ = new JLabel(judul);
        lblJ.setFont(FONT_KECIL);
        lblJ.setForeground(new Color(148, 163, 184));

        JLabel lblN = new JLabel(nilai);
        lblN.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblN.setForeground(warna);

        // Simpan referensi label untuk update nanti
        if (judul.contains("SALDO"))      lblSaldo      = lblN;
        if (judul.contains("PEMASUKAN"))  lblPemasukan  = lblN;
        if (judul.contains("PENGELUARAN"))lblPengeluaran = lblN;

        kartu.add(lblJ);
        kartu.add(lblN);
        return kartu;
    }

    // ===== KONTEN UTAMA =====
    private JPanel buatKontenUtama() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_UTAMA);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ---- Toolbar (Pencarian & Filter) ----
        panel.add(buatToolbar(), BorderLayout.NORTH);

        // ---- Tabel Transaksi ----
        panel.add(buatPanelTabel(), BorderLayout.CENTER);

        // ---- Tombol Aksi ----
        panel.add(buatPanelAksi(), BorderLayout.SOUTH);

        return panel;
    }

    // Toolbar pencarian & filter
    private JPanel buatToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toolbar.setBackground(BG_UTAMA);

        // Search box
        txtCari = new JTextField(20);
        txtCari.setFont(FONT_LABEL);
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        txtCari.putClientProperty("JTextField.placeholderText", "🔍 Cari transaksi...");

        JButton btnCari = buatTombolKecil("Cari", BIRU_TUA, Color.WHITE);
        btnCari.addActionListener(e -> cariTransaksi());

        // Filter jenis
        JLabel lblFilter = new JLabel("Filter:");
        lblFilter.setFont(FONT_LABEL);

        String[] opsiFilter = {"SEMUA", "PEMASUKAN", "PENGELUARAN"};
        JComboBox<String> cmbFilter = new JComboBox<>(opsiFilter);
        cmbFilter.setFont(FONT_LABEL);
        cmbFilter.addActionListener(e -> {
            String selected = (String) cmbFilter.getSelectedItem();
            filterTransaksi(selected);
        });

        JButton btnRefresh = buatTombolKecil("↻ Refresh", new Color(100, 116, 139), Color.WHITE);
        btnRefresh.addActionListener(e -> loadData());

        toolbar.add(txtCari);
        toolbar.add(btnCari);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(lblFilter);
        toolbar.add(cmbFilter);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(btnRefresh);

        return toolbar;
    }

    // Panel tabel transaksi
    private JScrollPane buatPanelTabel() {
        String[] kolom = {"ID", "Tanggal", "Keterangan", "Kategori", "Jenis", "Jumlah"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelTransaksi = new JTable(tableModel);
        tabelTransaksi.setFont(FONT_LABEL);
        tabelTransaksi.setRowHeight(36);
        tabelTransaksi.setShowGrid(false);
        tabelTransaksi.setIntercellSpacing(new Dimension(0, 0));
        tabelTransaksi.setSelectionBackground(new Color(224, 231, 255));
        tabelTransaksi.setSelectionForeground(Color.BLACK);
        tabelTransaksi.setBackground(Color.WHITE);

        // Styling header tabel
        JTableHeader header = tabelTransaksi.getTableHeader();
        header.setBackground(new Color(15, 23, 42));
        header.setForeground(Color.WHITE);
        header.setFont(FONT_BOLD);
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // Lebar kolom
        int[] lebarKolom = {50, 100, 240, 120, 120, 130};
        for (int i = 0; i < lebarKolom.length; i++) {
            tabelTransaksi.getColumnModel().getColumn(i).setPreferredWidth(lebarKolom[i]);
        }

        // Custom renderer - warna berdasarkan jenis
        tabelTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

                if (!isSelected) {
                    String jenis = (String) table.getValueAt(row, 4);
                    if ("PEMASUKAN".equals(jenis)) {
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 253, 244));
                        if (col == 5) setForeground(HIJAU_TUA);
                        else setForeground(Color.BLACK);
                    } else {
                        setBackground(row % 2 == 0 ? Color.WHITE : new Color(255, 245, 245));
                        if (col == 5) setForeground(MERAH_TUA);
                        else setForeground(Color.BLACK);
                    }
                } else {
                    setBackground(new Color(199, 210, 254));
                    setForeground(Color.BLACK);
                }
                return this;
            }
        });

        // Listener seleksi baris
        tabelTransaksi.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tabelTransaksi.getSelectedRow();
                if (row >= 0) {
                    selectedId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    btnHapus.setEnabled(true);
                    btnEdit.setEnabled(true);
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabelTransaksi);
        scroll.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    // Panel tombol aksi bawah
    private JPanel buatPanelAksi() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(BG_UTAMA);

        btnEdit = buatTombolKecil("Edit", BIRU_TUA, Color.WHITE);
        btnHapus = buatTombolKecil("Hapus", MERAH_TUA, Color.WHITE);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);

        btnEdit.addActionListener(e -> editTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());

        panel.add(btnEdit);
        panel.add(btnHapus);
        return panel;
    }

    // ===== DIALOG FORM =====
    private void tampilkanFormDialog(Transaksi transaksiEdit) {
        JDialog dialog = new JDialog(this, transaksiEdit == null ? "Tambah Transaksi" : "Edit Transaksi", true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(BG_UTAMA);

        // ---- Header Dialog ----
        JPanel headerD = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerD.setBackground(BG_SIDEBAR);
        headerD.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel lblJd = new JLabel(transaksiEdit == null ? "Tambah Transaksi Baru" : "Edit Transaksi");
        lblJd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJd.setForeground(Color.WHITE);
        headerD.add(lblJd);
        dialog.add(headerD, BorderLayout.NORTH);

        // ---- Panel Form ----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_UTAMA);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris form
        String[] labels = {"Tanggal (YYYY-MM-DD):", "Keterangan:", "Jenis:", "Kategori:", "Jumlah (Rp):"};

        // Inisialisasi field
        txtTanggal    = buatTextField();
        txtKeterangan = buatTextField();
        txtJumlah     = buatTextField();

        String[] opsiJenis = {"PEMASUKAN", "PENGELUARAN"};
        cmbJenis   = new JComboBox<>(opsiJenis);
        cmbKategori = new JComboBox<>(FormatUtil.getKategoriPemasukan());
        styleCmb(cmbJenis);
        styleCmb(cmbKategori);

        // Update kategori saat jenis berubah
        cmbJenis.addActionListener(e -> {
            String j = (String) cmbJenis.getSelectedItem();
            cmbKategori.removeAllItems();
            String[] cats = "PEMASUKAN".equals(j)
                    ? FormatUtil.getKategoriPemasukan()
                    : FormatUtil.getKategoriPengeluaran();
            for (String c : cats) cmbKategori.addItem(c);
        });

        // Isi form jika edit
        if (transaksiEdit != null) {
            txtTanggal.setText(transaksiEdit.getTanggal().toString());
            txtKeterangan.setText(transaksiEdit.getKeterangan());
            txtJumlah.setText(String.valueOf((long) transaksiEdit.getJumlah()));
            cmbJenis.setSelectedItem(transaksiEdit.getJenis());
            cmbKategori.setSelectedItem(transaksiEdit.getKategori());
        } else {
            txtTanggal.setText(LocalDate.now().toString());
        }

        Component[] fields = {txtTanggal, txtKeterangan, cmbJenis, cmbKategori, txtJumlah};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.35;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(FONT_LABEL);
            form.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 0.65;
            form.add(fields[i], gbc);
        }

        dialog.add(form, BorderLayout.CENTER);

        // ---- Tombol Dialog ----
        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        tombolPanel.setBackground(BG_UTAMA);
        tombolPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        JButton btnBatalD = buatTombolKecil("Batal", new Color(100, 116, 139), Color.WHITE);
        JButton btnSimpanD = buatTombolKecil(transaksiEdit == null ? "Simpan" : "Update", AKSEN, Color.WHITE);

        btnBatalD.addActionListener(e -> dialog.dispose());
        btnSimpanD.addActionListener(e -> {
            boolean berhasil;
            if (transaksiEdit == null) {
                berhasil = controller.tambahTransaksi(
                        txtTanggal.getText(), txtKeterangan.getText(),
                        (String) cmbKategori.getSelectedItem(),
                        (String) cmbJenis.getSelectedItem(), txtJumlah.getText());
            } else {
                berhasil = controller.updateTransaksi(
                        transaksiEdit.getId(), txtTanggal.getText(), txtKeterangan.getText(),
                        (String) cmbKategori.getSelectedItem(),
                        (String) cmbJenis.getSelectedItem(), txtJumlah.getText());
            }

            if (berhasil) {
                dialog.dispose();
                loadData();
                JOptionPane.showMessageDialog(this,
                        "✅ Transaksi berhasil " + (transaksiEdit == null ? "ditambahkan!" : "diperbarui!"),
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "❌ Gagal menyimpan. Periksa kembali isian form.",
                        "Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        tombolPanel.add(btnBatalD);
        tombolPanel.add(btnSimpanD);
        dialog.add(tombolPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // ===== AKSI =====
    private void loadData() {
        updateTabel(controller.getAllTransaksi());
        updateRingkasan();
        selectedId = -1;
        if (btnHapus != null) { btnHapus.setEnabled(false); btnEdit.setEnabled(false); }
    }

    private void cariTransaksi() {
        updateTabel(controller.cari(txtCari.getText()));
    }

    private void filterTransaksi(String jenis) {
        updateTabel(controller.filterByJenis(jenis));
    }

    private void editTransaksi() {
        if (selectedId < 0) return;
        Transaksi t = controller.getById(selectedId);
        if (t != null) tampilkanFormDialog(t);
    }

    private void hapusTransaksi() {
        if (selectedId < 0) return;
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus transaksi ini?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (konfirmasi == JOptionPane.YES_OPTION) {
            boolean ok = controller.hapusTransaksi(selectedId);
            if (ok) {
                loadData();
                JOptionPane.showMessageDialog(this, "✅ Transaksi berhasil dihapus!", "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Gagal menghapus transaksi.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== UPDATE UI =====
    private void updateTabel(List<Transaksi> list) {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Transaksi t : list) {
            tableModel.addRow(new Object[]{
                t.getId(),
                t.getTanggal().format(fmt),
                t.getKeterangan(),
                t.getKategori(),
                t.getJenis(),
                FormatUtil.formatRupiah(t.getJumlah())
            });
        }
    }

    private void updateRingkasan() {
        lblSaldo.setText(FormatUtil.formatRupiah(controller.getSaldo()));
        lblPemasukan.setText(FormatUtil.formatRupiah(controller.getTotalPemasukan()));
        lblPengeluaran.setText(FormatUtil.formatRupiah(controller.getTotalPengeluaran()));

        // Warna saldo berdasarkan nilai
        double saldo = controller.getSaldo();
        lblSaldo.setForeground(saldo >= 0 ? new Color(99, 102, 241) : MERAH_TUA);
    }

    // ===== FACTORY METHOD untuk komponen UI =====
    private JButton buatTombol(String teks, Color bg, Color fg) {
        JButton btn = new JButton(teks);
        btn.setFont(FONT_BOLD);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private JButton buatTombolKecil(String teks, Color bg, Color fg) {
        JButton btn = new JButton(teks);
        btn.setFont(FONT_LABEL);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JTextField buatTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_LABEL);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(203, 213, 225), 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        tf.setPreferredSize(new Dimension(200, 34));
        return tf;
    }

    private void styleCmb(JComboBox<String> cmb) {
        cmb.setFont(FONT_LABEL);
        cmb.setBackground(Color.WHITE);
        cmb.setPreferredSize(new Dimension(200, 34));
    }
}
